package de.felixbrandt.ceva.provider;

import de.felixbrandt.ceva.entity.Algorithm;

/**
 * Provides a preconfigured set of instances.
 */
public class FixedAlgorithmProvider implements AlgorithmProvider
{
  private String last_name;
  private Algorithm algorithm;

  public FixedAlgorithmProvider(final Algorithm _algorithm)
  {
    setAlgorithm(_algorithm);
  }

  public final void setAlgorithm (final Algorithm _algorithm)
  {
    algorithm = _algorithm;
  }

  public final Algorithm findByName (final String name)
  {
    last_name = name;
    return algorithm;
  }

  public final String getLastName ()
  {
    return last_name;
  }

}
