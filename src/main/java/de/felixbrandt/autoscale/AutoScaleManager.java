package de.felixbrandt.autoscale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

import de.felixbrandt.ceva.config.AutoScaleConfiguration;
import de.felixbrandt.ceva.config.QueueConfiguration;

/**
 * Manage CEVA workers in AWS.
 */
public class AutoScaleManager implements Runnable
{
  private static final Logger LOGGER = LogManager.getLogger();
  private InstanceManager instance_manager;
  private ScalingPolicy policy;
  private int check_interval;
  private boolean stop = false;
  public static final int MICROSECONDS_PER_SECOND = 1000;

  public AutoScaleManager(final AutoScaleConfiguration scale_config,
          final QueueConfiguration queue_config)
  {
    check_interval = scale_config.getCheckInterval();

    Sensor queue_sensor = new GearmanQueueLengthSensor(queue_config.getHost(),
            queue_config.getPort(), queue_config.getJobQueueName());

    BasicAWSCredentials aws_credentials = new BasicAWSCredentials(
            scale_config.getAWSKey(), scale_config.getAWSSecret());
    AmazonEC2 aws_client = AmazonEC2ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(aws_credentials))
            .withRegion(scale_config.getAWSRegion()).build();

    final String user_data = assembleUserData(scale_config, queue_config);

    instance_manager = new InstanceManager(aws_client,
            scale_config.getAWSSubnet(), scale_config.getImageId(),
            scale_config.getInstanceType(), scale_config.getKeyName(),
            scale_config.getSecurityGroup(), user_data);

    policy = new ScalingPolicy(queue_sensor, instance_manager,
            scale_config.getStartSize(), scale_config.getFactor(),
            scale_config.getMaxSize(), scale_config.getStep());
  }

  public final void stop ()
  {
    stop = true;
  }

  public final void run ()
  {
    LOGGER.info("Starting autoscaling daemon (interval {}", check_interval);

    while (!stop) {
      policy.check();
      try {
        Thread.sleep(check_interval * MICROSECONDS_PER_SECOND);
      } catch (InterruptedException e) {
        stop = true;
      }
    }

    LOGGER.info("Stopping autoscaling, shutting down all instances");
    instance_manager.stopAll();
  }

  public final String assembleUserData (
          final AutoScaleConfiguration scale_config,
          final QueueConfiguration queue_config)
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("#!/bin/bash\n");

    if (scale_config.getAWSFilesystem() != null) {
      sb.append("mkdir -p /data\n");
      sb.append(
              "mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2 "
                      + scale_config.getAWSFilesystem() + ":/ /data\n");
      sb.append("cd /data\n");
    }

    if (scale_config.getAWSCommand() != null) {
      sb.append(scale_config.getAWSCommand());
    }

    if (scale_config.getAWSRunCeva()) {
      sb.append("echo \"queue:\n");
      sb.append("  mode: slave\n");
      sb.append("  host: " + queue_config.getHost() + "\n");
      sb.append("  port: " + queue_config.getPort() + "\n");
      sb.append("  job_queue: " + queue_config.getJobQueueName() + "\n");
      sb.append("  worker: " + scale_config.getWorkersPerInstance() + "\n");
      if (scale_config.getIdleTimeout() > 0) {
        sb.append("  idle_timeout: " + scale_config.getIdleTimeout() + "\n");
      }
      sb.append("\" > /tmp/ceva.slave.yml\n");

      sb.append("echo \"running CEVA with config\"\n");
      sb.append("cat /tmp/ceva.slave.yml\n");
      sb.append("java -jar " + scale_config.getCevaJar()
              + " /tmp/ceva.slave.yml\n");
    }

    if (scale_config.getAWSAutoShutdown()) {
      sb.append("echo \"shutting down VM\"\n");
      sb.append("shutdown -h\n");
    }

    sb.append("echo \"continue cloud init\"");

    return sb.toString();
  }
}
