package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

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

    start_size = params.getIntParam("start_size", 0);
    max_size = params.getIntParam("max_size", 1);
    step = params.getIntParam("step", 1);
    factor = params.getIntParam("factor", 0);
    check_interval = params.getIntParam("check_interval", 60);
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
}
