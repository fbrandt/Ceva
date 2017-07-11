package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;

import de.felixbrandt.ceva.entity.Data;
import de.felixbrandt.ceva.entity.Rule;

/**
 * Provides a preconfigured Data object.
 */
public class FixedDataProvider extends DataProvider
{
  private Collection<Rule> last_rules = new ArrayList<Rule>();
  private Collection<?> last_sources;
  private Data<?, ?> fixture;

  public FixedDataProvider(final Data<?, ?> _fixture)
  {
    fixture = _fixture;
  }

  @Override
  public final Collection<Data<?, ?>> getData (final Rule rule, final Collection<?> sources)
  {
    last_rules.add(rule);
    last_sources = sources;

    final Collection<Data<?, ?>> result = new ArrayList<Data<?, ?>>();
    result.add(fixture);

    return result;
  }

  public final Collection<Rule> getLastRules ()
  {
    return last_rules;
  }

  public final Collection<?> getLastSources ()
  {
    return last_sources;
  }
}
