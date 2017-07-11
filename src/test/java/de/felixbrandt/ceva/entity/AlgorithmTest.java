package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Algorithm;
import de.felixbrandt.ceva.entity.Rule;

public class AlgorithmTest
{

  Algorithm algo;

  @Before
  public void Setup ()
  {
    algo = new Algorithm();
  }

  @Test
  public void defaultRepeatValue ()
  {
    assertEquals(1, algo.getRepeat());
  }

  @Test
  public void repeatValue ()
  {
    algo.setRepeat(2);
    assertEquals(2, algo.getRepeat());
  }
  
  @Test 
  public void defaultParameters(){
    assertEquals(0, algo.getParameters().size());
  }
  
  @Test
  public void parameters(){
    HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
    parameters.put("a", Arrays.asList("a", "b"));
    parameters.put("b", Arrays.asList("a", "b", "c"));

    algo.setParameters(parameters);
    
    assertEquals(2, algo.getParameters().size());
    assertEquals(2, algo.getParameters().get("a").size());
    assertEquals(3, algo.getParameters().get("b").size());
  }

  @Test
  public void testUpdateFrom ()
  {
    final Algorithm update = new Algorithm();
    update.setRepeat(2);

    final Rule returned = algo.updateFrom(update);
    assertEquals(algo, returned);

    assertEquals(2, algo.getRepeat());
  }
}
