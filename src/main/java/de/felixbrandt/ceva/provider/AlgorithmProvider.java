package de.felixbrandt.ceva.provider;

import de.felixbrandt.ceva.entity.Algorithm;

/**
 * Lookup algorithms.
 */
public interface AlgorithmProvider
{
  Algorithm findByName (final String name);
}
