package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.controller.base.DataSource;

/**
 * Interface to a collection of datasources
 */
public interface DataSourceProvider
{
  Collection<? extends DataSource> getDataSources ();
}
