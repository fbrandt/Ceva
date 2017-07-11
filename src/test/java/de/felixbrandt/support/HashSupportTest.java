package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import de.felixbrandt.support.HashSupport;

public class HashSupportTest
{

  @Test
  public void test ()
  {
    assertEquals("098f6bcd4621d373cade4e832627b4f6", HashSupport.doHash("test"));
    assertNotEquals("098f6bcd4621d373cade4e832627b4f6", HashSupport.doHash("test2"));
  }

}
