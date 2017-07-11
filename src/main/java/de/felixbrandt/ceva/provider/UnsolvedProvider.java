package de.felixbrandt.ceva.provider;

import java.util.HashSet;

import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Get IDs of unsolved sources for the given rule.
 */
public interface UnsolvedProvider
{
  HashSet<Integer> getUnsolved (final Executable executable, final int version);
}
