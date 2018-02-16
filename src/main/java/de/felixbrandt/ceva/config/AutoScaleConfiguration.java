package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

/**
 * Configure AWS auto-scaling of remote workers.
 */
public class AutoScaleConfiguration
{
  private boolean is_active;
  private String aws_key;
  private String aws_secret;
  private String aws_region;
  private String image_id;
  private String instance_type;
  private String key_name;
  private String security_group;
  private String aws_filesystem;
  private String aws_command;
  private int workers_per_instance;
  private int idle_timeout;
  private boolean aws_run_ceva;
  private boolean aws_auto_shutdown;

  private int start_size;
  private int max_size;
  private int step;
  private int factor;
  private int check_interval;

  public AutoScaleConfiguration()
  {
    init(new ParameterMap(null));
  }

  public AutoScaleConfiguration(final ParameterMap params)
  {
    init(params);
  }

  private void init (final ParameterMap params)
  {
    is_active = params.getBoolParam("active", false);
    aws_key = params.getStringParam("aws_key", "");
    aws_secret = params.getStringParam("aws_secret", "");
    aws_region = params.getStringParam("aws_region", "us-east-2");
    image_id = params.getStringParam("image_id", "");
    instance_type = params.getStringParam("instance_type", "t2.micro");
    key_name = params.getStringParam("key_name", "");
    security_group = params.getStringParam("security_group", "default");
    aws_filesystem = params.getStringParam("aws_filesystem", null);
    aws_command = params.getStringParam("aws_command", null);
    workers_per_instance = params.getIntParam("workers_per_instance", 1);
    idle_timeout = params.getIntParam("idle_timeout", 3600);
    aws_run_ceva = params.getBoolParam("aws_run_ceva", true);
    aws_auto_shutdown = params.getBoolParam("aws_auto_shutdown", true);
    start_size = params.getIntParam("start_size", 1);
    max_size = params.getIntParam("max_size", 1);
    step = params.getIntParam("step", 1);
    factor = params.getIntParam("factor", 1);
    check_interval = params.getIntParam("check_interval", 10);
  }

  public boolean isActive ()
  {
    return is_active;
  }

  public void setActive (boolean active)
  {
    is_active = active;
  }

  public String getAWSRegion ()
  {
    return aws_region;
  }

  public void setAWSRegion (String aws_region)
  {
    this.aws_region = aws_region;
  }

  public String getAWSKey ()
  {
    return aws_key;
  }

  public void setAWSKey (String aws_key)
  {
    this.aws_key = aws_key;
  }

  public String getAWSSecret ()
  {
    return aws_secret;
  }

  public void setAWSSecret (String aws_secret)
  {
    this.aws_secret = aws_secret;
  }

  public String getImageId ()
  {
    return image_id;
  }

  public void setImageId (String image_id)
  {
    this.image_id = image_id;
  }

  public String getInstanceType ()
  {
    return instance_type;
  }

  public void setInstanceType (String instance_type)
  {
    this.instance_type = instance_type;
  }

  public String getKeyName ()
  {
    return key_name;
  }

  public void setKeyName (String key_name)
  {
    this.key_name = key_name;
  }

  public String getSecurityGroup ()
  {
    return security_group;
  }

  public void setSecurityGroup (String security_group)
  {
    this.security_group = security_group;
  }

  public int getStartSize ()
  {
    return start_size;
  }

  public void setMaxSize (int max_size)
  {
    this.max_size = max_size;
  }

  public int getMaxSize ()
  {
    return max_size;
  }

  public void setStartSize (int start_size)
  {
    this.start_size = start_size;
  }

  public int getStep ()
  {
    return step;
  }

  public void setStep (int step)
  {
    this.step = step;
  }

  public int getFactor ()
  {
    return factor;
  }

  public void setFactor (int factor)
  {
    this.factor = factor;
  }

  public int getCheckInterval ()
  {
    return check_interval;
  }

  public void setCheckInterval (int check_interval)
  {
    this.check_interval = check_interval;
  }

  public String getAWSFilesystem ()
  {
    return aws_filesystem;
  }

  public int getWorkersPerInstance ()
  {
    return workers_per_instance;
  }

  public int getIdleTimeout ()
  {
    return idle_timeout;
  }

  public String getAWSCommand ()
  {
    return aws_command;
  }

  public boolean getAWSRunCeva ()
  {
    return aws_run_ceva;
  }

  public boolean getAWSAutoShutdown ()
  {
    return aws_auto_shutdown;
  }
}
