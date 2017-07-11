package de.felixbrandt.ceva.metric;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.felixbrandt.ceva.controller.base.Command;
import de.felixbrandt.ceva.controller.base.DataSource;
import de.felixbrandt.ceva.controller.base.ResultFactory;
import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Metric;
import de.felixbrandt.support.StreamSupport;

/**
 * Abstract factory to create result of Metric calculation.
 */
public abstract class DataFactory<MetricType extends Metric, SourceType> implements
        ResultFactory
{
  private final MetricType metric;
  private SourceType source;
  private int version;

  private static final Logger LOGGER = LogManager.getLogger();

  public DataFactory(final MetricType _metric)
  {
    metric = _metric;
    source = null;
  }

  public final MetricType getMetric ()
  {
    return metric;
  }

  public final Data<MetricType, SourceType> create (final Command process)
  {
    final Data<MetricType, SourceType> data = createData(metric, source, version);
    data.setRawValue(StreamSupport.getStringFromInputStream(process.getStdout()));

    if (data.getValue() != null) {
      return data;
    }

    return null;
  }

  public final Data<MetricType, SourceType> createData (final MetricType _metric,
          final SourceType _source, final int _version)
  {
    final Data<MetricType, SourceType> data = doCreateData(_metric);

    if (data != null) {
      data.setRule(_metric);
      data.setSource(_source);
      data.setVersion(_version);
    }

    return data;
  }

  public abstract Data<MetricType, SourceType> doCreateData (final MetricType _metric);

  protected final Data<MetricType, SourceType> unkownType (final MetricType _metric)
  {
    LOGGER.error("unknown type of metric {}", _metric.getName());

    return null;
  }

  @SuppressWarnings("unchecked")
  public final void setSource (final DataSource data_source)
  {
    source = (SourceType) (data_source.getObject());
  }

  public final void setVersion (final int _version)
  {
    version = _version;
  }
}
