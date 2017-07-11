package de.felixbrandt.ceva.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Integer result of a Metric execution.
 */
@SuppressWarnings("designforextension")
public class DataDouble<RuleType, SourceType> extends Data<RuleType, SourceType>
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LogManager.getLogger();
  private Double value;

  public DataDouble()
  {
    super();
  }

  public DataDouble(final SourceType _source, final RuleType _rule, final int _version,
          final double _value)
  {
    super(_source, _rule, _version);
    value = _value;
  }

  @Override
  public final void setRawValue (final String input)
  {
    try {
      value = Double.parseDouble(input.trim());
    } catch (final Exception e) {
      LOGGER.error("unable to parse result to integer metric: \"{}\"", input);
    }
  }

  public void setValue (final Double _value)
  {
    value = _value;
  }

  @Override
  public Double getValue ()
  {
    return value;
  }
}
