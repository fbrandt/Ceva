package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Instance;
import de.felixbrandt.ceva.entity.Solution;

/**
 * Search solutions by their attributes
 */
public interface SolutionProvider
{
  Solution find (final int id);

  Collection<Solution> find (final Instance instance, final Algorithm algorithm);

  Collection<Solution> find (final Instance instance, final Algorithm algorithm,
          final int version);
}
