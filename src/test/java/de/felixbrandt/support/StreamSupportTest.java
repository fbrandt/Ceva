package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.support.StreamSupport;

public class StreamSupportTest
{
  InputStream multiline;

  @Before
  public void setUp () throws Exception
  {
    multiline = StreamSupport.createInputStream("Hello\nWorld\n");
  }

  @Test
  public void testGetStringFromInputStream ()
  {
    final String result = StreamSupport.getStringFromInputStream(multiline);
    assertEquals("Hello\nWorld\n", result);
  }

}
