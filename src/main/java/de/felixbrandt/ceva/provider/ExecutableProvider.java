package de.felixbrandt.ceva.provider;

import java.util.Collection;

import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Retrieve executables to run.
 */
public interface ExecutableProvider
{
  Collection<? extends Executable> getExecutables ();
}
