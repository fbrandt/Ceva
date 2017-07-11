package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.entity.Instance;

/**
 * Search instances by keywords
 */
public interface InstanceProvider
{
  Collection<Instance> findByKeyword (final String keyword);
}
