package de.felixbrandt.autoscale;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

import de.felixbrandt.ceva.config.AutoScaleConfiguration;
import de.felixbrandt.ceva.config.QueueConfiguration;

public class AutoScaleManager implements Runnable
{
  private InstanceManager instance_manager;
  private ScalingPolicy policy;
  private int check_interval;
  private boolean stop = false;

  public AutoScaleManager(AutoScaleConfiguration scale_config, QueueConfiguration queue_config)
  {
    check_interval = scale_config.getCheckInterval();

    Sensor queue_sensor = new GearmanQueueLengthSensor(queue_config.getHost(),
            queue_config.getPort(), queue_config.getJobQueueName());

    BasicAWSCredentials aws_credentials = new BasicAWSCredentials(scale_config.getAWSKey(),
            scale_config.getAWSSecret());
    AmazonEC2 aws_client = AmazonEC2ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(aws_credentials))
            .withRegion(scale_config.getAWSRegion()).build();

    String user_data = assembleUserData(scale_config, queue_config);

    instance_manager = new InstanceManager(aws_client, scale_config.getImageId(),
            scale_config.getInstanceType(), scale_config.getKeyName(),
            scale_config.getSecurityGroup(), user_data);

    policy = new ScalingPolicy(queue_sensor, instance_manager, scale_config.getStartSize(),
            scale_config.getFactor(), scale_config.getMaxSize(), scale_config.getStep());
  }

  public void stop ()
  {
    stop = true;
  }

  public void run ()
  {
    while (!stop) {
      policy.check();
      try {
        Thread.sleep(check_interval * 1000);
      } catch (InterruptedException e) {
        // do nothing
      }
    }
    instance_manager.stopAll();
  }

  public String assembleUserData (AutoScaleConfiguration scale_config,
          QueueConfiguration queue_config)
  {
    StringBuilder sb = new StringBuilder();
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
      sb.append("\" >> /tmp/ceva.slave.yml\n");

      sb.append("echo \"running CEVA with config\"\n");
      sb.append("cat /tmp/ceva.slave.yml\n");
      sb.append("java -jar ceva.jar /tmp/ceva.slave.yml\n");
    }

    if (scale_config.getAWSAutoShutdown()) {
      sb.append("echo \"shutting down VM\"\n");
      sb.append("shutdown -h\n");
    }

    sb.append("echo \"continue cloud init\"");

    return sb.toString();
  }
}
