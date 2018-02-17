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

public class InstanceManager
{
  private static final Logger LOGGER = LogManager.getLogger();
  private AmazonEC2 client;
  private String image_id;
  private String instance_type;
  private String key_name;
  private String security_group;
  private String encoded_user_data;
  private List<String> running_instance_ids;

  public InstanceManager(AmazonEC2 _client, String _image_id, String _instance_type,
          String _key_name, String _security_group, String user_data)
  {
    running_instance_ids = new ArrayList<String>();
    client = _client;
    image_id = _image_id;
    instance_type = _instance_type;
    key_name = _key_name;
    security_group = _security_group;

    encoded_user_data = Base64.getEncoder()
            .encodeToString(user_data.getBytes(Charset.forName("UTF-8")));
  }

  public void start (int n)
  {
    RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

    runInstancesRequest.withImageId(image_id).withInstanceType(instance_type).withMinCount(1)
            .withMaxCount(n).withKeyName(key_name)
            .withInstanceInitiatedShutdownBehavior("terminate").withUserData(encoded_user_data)
            .withSecurityGroups(security_group);

    RunInstancesResult result = client.runInstances(runInstancesRequest);
    for (Instance instance : result.getReservation().getInstances()) {
      LOGGER.warn("Instance: {}({}) is now {}", instance.getInstanceId(),
              instance.getPublicIpAddress(), instance.getState().getName());
      running_instance_ids.add(instance.getInstanceId());
    }
  }

  public int size ()
  {
    return running_instance_ids.size();
  }

  public void stopAll ()
  {
    TerminateInstancesRequest request = new TerminateInstancesRequest(running_instance_ids);
    TerminateInstancesResult result = client.terminateInstances(request);
    for (InstanceStateChange state : result.getTerminatingInstances()) {
      LOGGER.warn("Instance: {} is now {}", state.getInstanceId(),
              state.getCurrentState().getName());
    }
  }
}
