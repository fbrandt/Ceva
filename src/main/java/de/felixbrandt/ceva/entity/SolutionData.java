package de.felixbrandt.ceva.entity;

/**
 * Helper class for setting up Hibernate, never used directly.
 */
public abstract class SolutionData extends Data<SolutionMetric, Solution>
{
  private static final long serialVersionUID = 1L;

  @Override
  public abstract void setRawValue (String input);

  @Override
  public abstract Object getValue ();
}
