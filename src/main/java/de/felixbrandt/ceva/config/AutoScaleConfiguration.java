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

  public static final String DEFAULT_AWS_REGION = "us-east-2";
  public static final String DEFAULT_INSTANCE_TYPE = "t2.micro";
  public static final String DEFAULT_SECURITY_GROUP = "default";
  public static final int DEFAULT_IDLE_TIMEOUT_SECONDS = 3600;
  public static final int DEFAULT_CHECK_INTERVAL_SECONDS = 10;

  public AutoScaleConfiguration()
  {
    init(new ParameterMap(null));
  }

  public AutoScaleConfiguration(final ParameterMap params)
  {
    init(params);
  }

  private final void init (final ParameterMap params)
  {
    is_active = params.getBoolParam("active", false);
    aws_key = params.getStringParam("aws_key", "");
    aws_secret = params.getStringParam("aws_secret", "");
    aws_region = params.getStringParam("aws_region", DEFAULT_AWS_REGION);
    image_id = params.getStringParam("image_id", "");
    instance_type = params.getStringParam("instance_type", DEFAULT_INSTANCE_TYPE);
    key_name = params.getStringParam("key_name", "");
    security_group = params.getStringParam("security_group", DEFAULT_SECURITY_GROUP);
    aws_filesystem = params.getStringParam("aws_filesystem", null);
    aws_command = params.getStringParam("aws_command", null);
    workers_per_instance = params.getIntParam("workers_per_instance", 1);
    idle_timeout = params.getIntParam("idle_timeout", DEFAULT_IDLE_TIMEOUT_SECONDS);
    aws_run_ceva = params.getBoolParam("aws_run_ceva", true);
    aws_auto_shutdown = params.getBoolParam("aws_auto_shutdown", true);
    start_size = params.getIntParam("start_size", 1);
    max_size = params.getIntParam("max_size", 1);
    step = params.getIntParam("step", 1);
    factor = params.getIntParam("factor", 1);
    check_interval = params.getIntParam("check_interval", DEFAULT_CHECK_INTERVAL_SECONDS);
  }

  public final boolean isActive ()
  {
    return is_active;
  }

  public final void setActive (final boolean active)
  {
    is_active = active;
  }

  public final String getAWSRegion ()
  {
    return aws_region;
  }

  public final void setAWSRegion (final String aws_region)
  {
    this.aws_region = aws_region;
  }

  public final String getAWSKey ()
  {
    return aws_key;
  }

  public final void setAWSKey (final String aws_key)
  {
    this.aws_key = aws_key;
  }

  public final String getAWSSecret ()
  {
    return aws_secret;
  }

  public final void setAWSSecret (final String aws_secret)
  {
    this.aws_secret = aws_secret;
  }

  public final String getImageId ()
  {
    return image_id;
  }

  public final void setImageId (final String image_id)
  {
    this.image_id = image_id;
  }

  public final String getInstanceType ()
  {
    return instance_type;
  }

  public final void setInstanceType (final String instance_type)
  {
    this.instance_type = instance_type;
  }

  public final String getKeyName ()
  {
    return key_name;
  }

  public final void setKeyName (final String key_name)
  {
    this.key_name = key_name;
  }

  public final String getSecurityGroup ()
  {
    return security_group;
  }

  public final void setSecurityGroup (final String security_group)
  {
    this.security_group = security_group;
  }

  public final int getStartSize ()
  {
    return start_size;
  }

  public final void setMaxSize (final int max_size)
  {
    this.max_size = max_size;
  }

  public final int getMaxSize ()
  {
    return max_size;
  }

  public final void setStartSize (final int start_size)
  {
    this.start_size = start_size;
  }

  public final int getStep ()
  {
    return step;
  }

  public final void setStep (final int step)
  {
    this.step = step;
  }

  public final int getFactor ()
  {
    return factor;
  }

  public final void setFactor (final int factor)
  {
    this.factor = factor;
  }

  public final int getCheckInterval ()
  {
    return check_interval;
  }

  public final void setCheckInterval (final int check_interval)
  {
    this.check_interval = check_interval;
  }

  public final String getAWSFilesystem ()
  {
    return aws_filesystem;
  }

  public final int getWorkersPerInstance ()
  {
    return workers_per_instance;
  }

  public final int getIdleTimeout ()
  {
    return idle_timeout;
  }

  public final String getAWSCommand ()
  {
    return aws_command;
  }

  public final boolean getAWSRunCeva ()
  {
    return aws_run_ceva;
  }

  public final boolean getAWSAutoShutdown ()
  {
    return aws_auto_shutdown;
  }
}
