package de.felixbrandt.ceva.entity;

/**
 * Result of a Metric execution.
 */
@SuppressWarnings("designforextension")
public abstract class Data<RuleType, SourceType> implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private int instance_data;
  private SourceType source;
  private RuleType rule;
  private int version;

  public Data()
  {
    this(null, null, 0);
  }

  public Data(final SourceType _source, final RuleType _rule, final int _version)
  {
    source = _source;
    rule = _rule;
    version = _version;
  }

  public abstract void setRawValue (String input);

  public abstract Object getValue ();

  public int getData ()
  {
    return instance_data;
  }

  public void setData (final int id)
  {
    instance_data = id;
  }

  public Object getSource ()
  {
    return source;
  }

  public void setSource (final SourceType _source)
  {
    source = _source;
  }

  public RuleType getRule ()
  {
    return rule;
  }

  public void setRule (final RuleType _rule)
  {
    rule = _rule;
  }

  public int getVersion ()
  {
    return version;
  }

  public void setVersion (final int _version)
  {
    version = _version;
  }

  public final boolean matches (final Object _source, final Rule _rule, final int _version)
  {
    return source == _source && rule == _rule && version == _version;
  }
}
