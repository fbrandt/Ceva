package de.felixbrandt.ceva.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.felixbrandt.ceva.controller.base.Executable;

/**
 * Wrapper for ExecutableProvider filtering Executables by Blacklist/Whitelist
 */
public class ExecutableFilter implements ExecutableProvider
{
  private final ExecutableProvider wrapped_provider;
  private final List<String> whitelist;
  private final List<String> blacklist;

  public ExecutableFilter(final ExecutableProvider wrapped_provider)
  {
    this(wrapped_provider, null, null);
  }

  public ExecutableFilter(final ExecutableProvider wrapped_provider,
          final List<String> whitelist, final List<String> blacklist)
  {
    this.wrapped_provider = wrapped_provider;
    this.whitelist = whitelist;
    this.blacklist = blacklist;
  }

  public final Collection<? extends Executable> getExecutables ()
  {
    Collection<? extends Executable> result = wrapped_provider.getExecutables();
    if (whitelist != null) {
      result = filterByWhitelist(result);
    }
    if (blacklist != null) {
      result = filterByBlacklist(result);
    }

    return result;
  }

  public final Collection<? extends Executable> filterByWhitelist (
          Collection<? extends Executable> executables)
  {
    ArrayList<Executable> result = new ArrayList<Executable>();
    for (Executable executable : executables) {
      if (whitelist.stream().anyMatch(executable.getName()::contains)) {
        result.add(executable);
      }
    }

    return result;
  }

  public final Collection<? extends Executable> filterByBlacklist (
          Collection<? extends Executable> executables)
  {
    ArrayList<Executable> result = new ArrayList<Executable>();
    for (Executable executable : executables) {
      if (!blacklist.stream().anyMatch(executable.getName()::contains)) {
        result.add(executable);
      }
    }

    return result;
  }
}
