package de.felixbrandt.autoscale;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

import de.felixbrandt.ceva.config.AutoScaleConfiguration;
import de.felixbrandt.ceva.config.QueueConfiguration;

public class AutoScaleManager implements Runnable
{
  private AutoScaleConfiguration scale_config;
  private InstanceManager instance_manager;
  private ScalingPolicy policy;
  private boolean stop = false;

  public AutoScaleManager(AutoScaleConfiguration _scale_config,
          QueueConfiguration queue_config)
  {
    scale_config = _scale_config;

    Sensor queue_sensor = new GearmanQueueLengthSensor(queue_config.getHost(),
            queue_config.getPort(), queue_config.getJobQueueName());

    BasicAWSCredentials aws_credentials = new BasicAWSCredentials(scale_config.getAWSKey(),
            scale_config.getAWSSecret());
    AmazonEC2 aws_client = AmazonEC2ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(aws_credentials))
            .withRegion(scale_config.getAWSRegion()).build();

    String user_data = "";

    instance_manager = new InstanceManager(aws_client, scale_config.getImageId(),
            scale_config.getInstanceType(), scale_config.getKeyName(),
            scale_config.getSecurityGroup(), user_data);

    policy = new ScalingPolicy(queue_sensor, instance_manager, scale_config.getMaxSize());
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
        Thread.sleep(scale_config.getCheckInterval() * 1000);
      } catch (InterruptedException e) {
        // do nothing
      }
    }
    instance_manager.stopAll();
  }
}
