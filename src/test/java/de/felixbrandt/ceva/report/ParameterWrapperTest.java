package de.felixbrandt.ceva.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.report.GenericReportService;
import de.felixbrandt.ceva.report.MockGenericReportService;
import de.felixbrandt.ceva.report.ParameterWrapper;

public class ParameterWrapperTest
{
  MockGenericReportService mockService;
  GenericReportService service;
  ParameterWrapper wrapper;

  @Before
  public void setUp ()
  {
    mockService = new MockGenericReportService();
    service = mockService;
    wrapper = new ParameterWrapper(service);
  }

  @Test
  public void testGenericServiceIsRun ()
  {
    wrapper.run(Arrays.asList("-noDefaultAndRequired", "noDefaultAndRequiredTest"), 0);
    assertTrue(mockService.executed());
  }

  @Test
  public void allParametersCorrectlyParsedTest ()
  {
    wrapper.run(Arrays.asList("-noDefaultAndNonRequired", "A", "-noDefaultAndRequired", "B",
            "-defaultAndNonRequired", "C"), 0);
    assertEquals("A", mockService.getNoDefaultAndNonRequired());
    assertEquals("B", mockService.getNoDefaultAndRequired());
    assertEquals("C", mockService.getDefaultAndNonRequired());
  }

  @Test
  public void noDefaultAndNonRequiredParametersTest ()
  {
    wrapper.run(Arrays.asList("-noDefaultAndRequired", "A", "-defaultAndNonRequired", "B"), 0);
    assertEquals("A", mockService.getNoDefaultAndRequired());
    assertEquals("B", mockService.getDefaultAndNonRequired());
  }

  @Test
  public void defaultValueCorrectlySetTest ()
  {
    wrapper.run(Arrays.asList("-noDefaultAndRequired", "noDefaultAndRequiredTest"), 0);
    assertEquals("defaultAndNonRequired", mockService.getDefaultAndNonRequired());
  }

  @Test(expected = com.beust.jcommander.ParameterException.class)
  public void requiredParameterWithoutDefaultNotGivenTest ()
  {
    wrapper.run(new ArrayList<String>(), 0);
  }
}
