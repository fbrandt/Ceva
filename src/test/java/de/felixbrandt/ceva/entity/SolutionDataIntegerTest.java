package de.felixbrandt.ceva.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.SolutionDataInteger;

public class SolutionDataIntegerTest
{
  SolutionDataInteger data;

  @Before
  public void setUp () throws Exception
  {
    data = new SolutionDataInteger();
  }

  @Test
  public void testSerialize () throws IOException
  {
    final ObjectOutputStream serializer = new ObjectOutputStream(new ByteArrayOutputStream());
    serializer.writeObject(data);
  }
}
