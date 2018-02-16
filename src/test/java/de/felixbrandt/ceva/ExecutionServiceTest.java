package de.felixbrandt.ceva;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.config.ExecutionConfiguration;
import de.felixbrandt.ceva.config.RuleExecutionConfiguration;
import de.felixbrandt.ceva.controller.DataSourceFilter;
import de.felixbrandt.ceva.provider.DataSourceProvider;
import de.felixbrandt.ceva.provider.ExecutableFilter;
import de.felixbrandt.ceva.provider.ExecutableMemoryProvider;
import de.felixbrandt.ceva.provider.ExecutableProvider;
import de.felixbrandt.ceva.storage.base.Storage;
import de.felixbrandt.support.ParameterMap;

public class ExecutionServiceTest
{
  class MockExecutionStrategy implements ExecutionStrategy
  {
    public boolean called = false;

    @Override
    public void run (ExecutableProvider executables, DataSourceProvider sources,
            Storage storage, DataSourceFilter filter)
    {
      called = true;
    }
  }

  MockExecutionStrategy strategy;
  ExecutionService service;

  @Before
  public void setup ()
  {
    strategy = new MockExecutionStrategy();
    service = new ExecutionService(null, strategy, new ExecutionConfiguration());
  }

  @Test
  public void testSkipStrategy ()
  {
    service.doRun(null, null, null, null);
    assertFalse(strategy.called);
  }

  @Test
  public void testCallStrategy ()
  {
    service.doRun(new ExecutableMemoryProvider(), null, null, null);
    assertTrue(strategy.called);
  }

  @Test
  public void testSetupExecutableProviderDisabled ()
  {
    RuleExecutionConfiguration config = new RuleExecutionConfiguration(false);
    ExecutableProvider provider = service.setupExecutableProvider(config,
            new ExecutableMemoryProvider());
    assertNull(provider);
  }

  @Test
  public void testSetupExecutableProviderEnabled ()
  {
    Map<String, Object> data = new HashMap<String, Object>();
    List<String> whitelist = new ArrayList<String>();
    whitelist.add("algo");
    data.put("include", whitelist);

    RuleExecutionConfiguration config = new RuleExecutionConfiguration(true,
            new ParameterMap(data));
    ExecutableProvider provider = service.setupExecutableProvider(config,
            new ExecutableMemoryProvider());
    assertTrue(provider instanceof ExecutableFilter);
    assertEquals(whitelist, ((ExecutableFilter) provider).getWhitelist());
  }
}
