package de.felixbrandt.ceva.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Integer result of a Metric execution.
 */
@SuppressWarnings("designforextension")
public class DataInteger<RuleType, SourceType> extends Data<RuleType, SourceType>
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger();
  private Long value;

  public DataInteger()
  {
    super();
  }

  public DataInteger(final SourceType _source, final RuleType _rule, final int _version,
          final long _value)
  {
    super(_source, _rule, _version);
    value = _value;
  }

  @Override
  public final void setRawValue (final String input)
  {
    try {
      value = Long.parseLong(input.trim());
    } catch (final Exception e) {
      LOGGER.error("unable to parse result to integer metric: \"{}\"", input);
    }
  }

  public void setValue (final Long _value)
  {
    value = _value;
  }

  @Override
  public Long getValue ()
  {
    return value;
  }
}
