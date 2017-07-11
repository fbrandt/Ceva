package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import de.felixbrandt.support.StreamSupport;
import de.felixbrandt.support.StreamedString;

public class StreamedStringTest
{
  @Test
  public void test () throws InterruptedException
  {
    final InputStream in = StreamSupport.createInputStream("Hello World");
    final StreamedString hello = StreamedString.create(in);
    hello.join(1000);
    assertEquals("Hello World", hello.getString());
  }

}
