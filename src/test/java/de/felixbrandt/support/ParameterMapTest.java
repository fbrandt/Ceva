package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.support.ParameterMap;

public class ParameterMapTest
{
  HashMap data;
  ParameterMap params;

  @Before
  public void setup ()
  {
    data = new HashMap();
    params = new ParameterMap(data);
  }

  @Test
  public void testGetStringParam ()
  {
    data.put("key", "value");
    assertEquals("value", params.getStringParam("key"));
  }

  @Test
  public void testGetStringParamDefault ()
  {
    assertEquals("", params.getStringParam("key"));
    assertEquals("default", params.getStringParam("key", "default"));
  }

  @Test
  public void testGetIntParam ()
  {
    data.put("key", "42");
    assertEquals(42, params.getIntParam("key"));

    data.put("intkey", 42);
    assertEquals(42, params.getIntParam("intkey"));
  }

  @Test
  public void testGetIntParamDefault ()
  {
    assertEquals(0, params.getIntParam("key"));
    assertEquals(42, params.getIntParam("key", 42));
  }

  @Test
  public void testGetBoolParamString ()
  {
    data.put("key", "true");
    assertEquals(true, params.getBoolParam("key"));
  }

  @Test
  public void testGetBoolParamBool ()
  {
    data.put("key", true);
    assertEquals(true, params.getBoolParam("key"));
  }

  @Test
  public void testGetBoolParamDefault ()
  {
    assertEquals(false, params.getBoolParam("key"));
    assertEquals(true, params.getBoolParam("key", true));
  }

  @Test
  public void testGetMapParam ()
  {
    final Map mymap = new HashMap();
    data.put("key", mymap);
    assertEquals(mymap, params.getMapParam("key"));
  }

  @Test
  public void testGetMapParamString ()
  {
    final String entries = "entrieA:valA\nentrieB:valB1,valB2";
    data.put("mymap", entries);
    assertEquals(2, params.getMapParam("mymap").size());
    assertEquals(Arrays.asList("valA"), params.getMapParam("mymap").get("entrieA"));
    assertEquals(Arrays.asList("valB1", "valB2"), params.getMapParam("mymap").get("entrieB"));
  }

  @Test
  public void testGetMapParamDefault ()
  {
    final Map mymap = new HashMap();
    assertEquals(mymap, params.getMapParam("key"));
    mymap.put("key", "value");
    assertEquals(mymap, params.getMapParam("key", mymap));
  }

  @Test
  public void testGetListParam ()
  {
    final List mylist = new ArrayList();
    data.put("key", mylist);
    assertEquals(mylist, params.getListParam("key"));
  }

  @Test
  public void testGetListParamDefault ()
  {
    final List mylist = new ArrayList();
    assertEquals(mylist, params.getListParam("key"));
    mylist.add(42);
    assertEquals(mylist, params.getListParam("key", mylist));
  }

}
