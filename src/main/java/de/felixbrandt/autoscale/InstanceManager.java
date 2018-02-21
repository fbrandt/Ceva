package de.felixbrandt.autoscale;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;

/**
 * Manage (Start/Stop) multiple AWS EC2 instances with the same configuration.
 */
public class InstanceManager
{
  private static final Logger LOGGER = LogManager.getLogger();
  private AmazonEC2 aws_client;
  private String aws_subnet;
  private String aws_image_id;
  private String aws_instance_type;
  private String aws_key_name;
  private String aws_security_group;
  private String encoded_user_data;
  private List<String> running_instance_ids;

  public InstanceManager(final AmazonEC2 client, final String subnet,
          final String image_id, final String instance_type,
          final String key_name, final String security_group,
          final String user_data)
  {
    aws_client = client;
    aws_subnet = subnet;
    aws_image_id = image_id;
    aws_instance_type = instance_type;
    aws_key_name = key_name;
    aws_security_group = security_group;

    encoded_user_data = Base64.getEncoder()
            .encodeToString(user_data.getBytes(Charset.forName("UTF-8")));
    running_instance_ids = new ArrayList<String>();
  }

  public final void start (final int n)
  {
    RunInstancesRequest request = new RunInstancesRequest();

    request.withImageId(aws_image_id).withInstanceType(aws_instance_type)
            .withMinCount(1).withMaxCount(n).withKeyName(aws_key_name)
            .withInstanceInitiatedShutdownBehavior("terminate")
            .withUserData(encoded_user_data)
            .withSecurityGroups(aws_security_group);

    if (aws_subnet != null) {
      request.withSubnetId(aws_subnet);
    }

    LOGGER.warn("Starting {} instances ({}) on AWS", n, aws_instance_type);
    final RunInstancesResult result = aws_client.runInstances(request);
    for (Instance instance : result.getReservation().getInstances()) {
      LOGGER.warn("Instance: {}({}) is now {}", instance.getInstanceId(),
              instance.getPublicIpAddress(), instance.getState().getName());
      running_instance_ids.add(instance.getInstanceId());
    }
  }

  public final int size ()
  {
    return running_instance_ids.size();
  }

  public final void stopAll ()
  {
    if (running_instance_ids.size() > 0) {
      TerminateInstancesRequest request = new TerminateInstancesRequest(
              running_instance_ids);
      TerminateInstancesResult result = aws_client.terminateInstances(request);
      for (InstanceStateChange state : result.getTerminatingInstances()) {
        LOGGER.warn("Instance: {} is now {}", state.getInstanceId(),
                state.getCurrentState().getName());
      }
    }
  }
}
