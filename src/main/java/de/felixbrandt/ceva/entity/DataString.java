package de.felixbrandt.ceva.entity;

/**
 * String result of a Metric execution.
 */
@SuppressWarnings("designforextension")
public class DataString<RuleType, SourceType> extends Data<RuleType, SourceType>
{
  private static final long serialVersionUID = 1L;
  private String value;

  public DataString()
  {
    super();
  }

  public DataString(final SourceType _source, final RuleType _rule, final int _version,
          final String _value)
  {
    super(_source, _rule, _version);
    value = _value;
  }

  @Override
  public final void setRawValue (final String input)
  {
    setValue(input.trim());
  }

  public void setValue (final String _value)
  {
    value = _value;
  }

  @Override
  public String getValue ()
  {
    return value;
  }
}
