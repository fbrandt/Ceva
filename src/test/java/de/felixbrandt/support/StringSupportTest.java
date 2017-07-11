package de.felixbrandt.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.support.StringSupport;

public class StringSupportTest
{
  HashMap<String, List<String>> list_map;
  HashMap<String, String> param_map;
  HashMap<String, String> tuple_one;
  HashMap<String, String> tuple_two;

  @Before
  public void setUp ()
  {
    param_map = new HashMap<String, String>();
    param_map.put("paramA", "val1");
    param_map.put("paramB", "val2");

    list_map = new HashMap<String, List<String>>();
    list_map.put("paramA", Arrays.asList("val1", "val2"));
    list_map.put("paramB", Arrays.asList("val3"));

    tuple_one = new HashMap<String, String>();
    tuple_one.put("paramA", "val1");
    tuple_one.put("paramB", "val3");

    tuple_two = new HashMap<String, String>();
    tuple_two.put("paramA", "val2");
    tuple_two.put("paramB", "val3");
  }

  @Test
  public void testGetMapAsString ()
  {
    assertEquals("paramA: val1;paramB: val2;", StringSupport.getMapAsString(param_map));
  }

  @Test
  public void testGetMapAsStringEmptyMap ()
  {
    HashMap<String, String> empty_map = new HashMap<String, String>();
    assertEquals("", StringSupport.getMapAsString(empty_map));
  }

  @Test
  public void testGetMapAsStringNullMap ()
  {
    HashMap<String, String> null_map = null;
    assertEquals("", StringSupport.getMapAsString(null_map));
  }

  @Test
  public void testGetMapAsStringNull ()
  {
    param_map.put("paramC", null);
    assertEquals("paramA: val1;paramB: val2;paramC: ;",
            StringSupport.getMapAsString(param_map));
  }

  @Test
  public void testGetMapAsStringEmpty ()
  {
    param_map.put("paramC", "");
    assertEquals("paramA: val1;paramB: val2;paramC: ;",
            StringSupport.getMapAsString(param_map));
  }

  @Test
  public void testRenderString ()
  {
    String rendered = StringSupport.renderString("cmd1 {paramA} | cmd2 {paramB}", param_map);
    assertEquals("cmd1 val1 | cmd2 val2", rendered);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenderStringTemplateNull ()
  {
    StringSupport.renderString(null, param_map);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenderStringParametersNullTemplateWithTokens ()
  {
    StringSupport.renderString("cmd1 {paramA} | cmd2 {paramB}", null);
  }

  public void testRenderStringParametersNullTemplateWithoutTokens ()
  {
    String rendered = StringSupport.renderString("cmd1 | cmd2", null);
    assertEquals("cmd1 | cmd2", rendered);
  }

  public void testRenderStringTemplateEmpty ()
  {
    String rendered = StringSupport.renderString("", param_map);
    assertEquals("", rendered);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenderStringParametersEmptyTemplateWithTokens ()
  {
    String rendered = StringSupport.renderString("cmd1 {paramA} | cmd2 {paramB}",
            new HashMap<String, String>());
    assertEquals("cmd1 | cmd2 ", rendered);
  }

  @Test
  public void testRenderStringParametersEmptyTemplateWithoutTokens ()
  {
    String rendered = StringSupport.renderString("cmd1 | cmd2", new HashMap<String, String>());
    assertEquals("cmd1 | cmd2", rendered);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenderStringOnlyUndefinedTokens ()
  {
    StringSupport.renderString("cmd1 {paramC} | cmd2 {paramD}", param_map);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenderStringSomeUndefinedTokens ()
  {
    StringSupport.renderString("cmd1 {paramA} | cmd2 {paramC}", param_map);
  }

  @Test
  public void testRenderStringMoreParametersThanTokens ()
  {

    param_map.put("paramC", "val3");
    String rendered = StringSupport.renderString("cmd1 {paramA} | cmd2 {paramB}", param_map);

    assertEquals("cmd1 val1 | cmd2 val2", rendered);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRenderStringEmptyTokens(){
    StringSupport.renderString("cmd {}", param_map);
  }

  @Test
  public void testNumberOfPossibleTuples ()
  {

    assertEquals(2L, StringSupport.numberOfPossibleTuples(list_map));
  }

  @Test
  public void testNumberOfPossibleTuplesWithEmptyParameterList ()
  {
    list_map.put("paramC", new ArrayList<String>());
    assertEquals(2L, StringSupport.numberOfPossibleTuples(list_map));
  }

  @Test
  public void testNumberOfPossibleTuplesWithNullParameterList ()
  {
    list_map.put("paramC", null);
    assertEquals(2L, StringSupport.numberOfPossibleTuples(list_map));
  }

  @Test(expected = BufferOverflowException.class)
  public void testNumberOfPossibleTuplesBufferOverflow ()
  {
    list_map = new HashMap<String, List<String>>();
    for (int i = 0; i < 32; i++) {
      list_map.put("param" + i, Arrays.asList("val1", "val2"));
    }

    StringSupport.numberOfPossibleTuples(list_map);
  }

  @Test
  public void testNumberOfPossibleTuplesMapIsNull ()
  {
    list_map = null;
    assertEquals(0L, StringSupport.numberOfPossibleTuples(list_map));
  }

  @Test
  public void testNumberOfPossibleTuplesMapIsEmpty ()
  {
    list_map = new HashMap<String, List<String>>();
    assertEquals(0L, StringSupport.numberOfPossibleTuples(list_map));
  }

  @Test
  public void testFindUndefinedTokensOneUndefined ()
  {
    Set<String> undefined_tokens = StringSupport
            .findUndefinedTokens("cmd1 {paramA} | cmd2 {paramD}", param_map.keySet());
    assertEquals(1, undefined_tokens.size());
    assertTrue(undefined_tokens.contains("paramD"));
  }

  @Test
  public void testFindUndefinedTokensNoneUndefined ()
  {
    Set<String> undefined_tokens = StringSupport
            .findUndefinedTokens("cmd1 {paramA} | cmd2 {paramB}", param_map.keySet());
    assertEquals(0, undefined_tokens.size());
  }

  @Test
  public void testGetTokens ()
  {
    Set<String> tokens = StringSupport.getTokens("cmd1 {paramA} | cmd2 {paramB}");
    assertEquals(2, tokens.size());
    assertTrue(tokens.contains("paramA"));
    assertTrue(tokens.contains("paramB"));
  }

  @Test
  public void testGetTokensNoTokens ()
  {
    Set<String> tokens = StringSupport.getTokens("cmd1 | cmd2");
    assertEquals(0, tokens.size());
  }

  @Test
  public void testGenerateAllPossibleTuples ()
  {
    List<Map<String, String>> tuples = StringSupport.generateAllPossibleTuples(list_map);

    assertEquals(2, tuples.size());

    assertTrue(tuples.contains(tuple_one));
    assertTrue(tuples.contains(tuple_two));
  }

  @Test
  public void testGenerateAllPossibleTuplesHashMapEmpty ()
  {
    List<Map<String, String>> tuples = StringSupport
            .generateAllPossibleTuples(new HashMap<String, List<String>>());

    assertTrue(tuples.isEmpty());
  }

  @Test
  public void testGenerateAllPossibleTuplesHashMapNull ()
  {
    List<Map<String, String>> tuples = StringSupport.generateAllPossibleTuples(null);

    assertTrue(tuples.isEmpty());
  }

  @Test
  public void testGenerateAllPossibleTuplesListEmpty ()
  {
    list_map.put("paramC", new ArrayList<String>());
    List<Map<String, String>> tuples = StringSupport.generateAllPossibleTuples(list_map);

    assertEquals(2, tuples.size());

    tuple_one.put("paramC", "");
    tuple_two.put("paramC", "");

    assertTrue(tuples.contains(tuple_one));
    assertTrue(tuples.contains(tuple_two));
  }

  @Test
  public void testGenerateAllPossibleTuplesListNull ()
  {
    list_map.put("paramC", null);
    List<Map<String, String>> tuples = StringSupport.generateAllPossibleTuples(list_map);

    assertEquals(2, tuples.size());

    tuple_one.put("paramC", "");
    tuple_two.put("paramC", "");

    assertTrue(tuples.contains(tuple_one));
    assertTrue(tuples.contains(tuple_two));
  }

  @Test
  public void testGenerateAllPossibleTuplesStringEmpty ()
  {

    list_map.put("paramC", Arrays.asList(""));
    List<Map<String, String>> tuples = StringSupport.generateAllPossibleTuples(list_map);

    assertEquals(2, tuples.size());

    tuple_one.put("paramC", "");
    tuple_two.put("paramC", "");

    assertTrue(tuples.contains(tuple_one));
    assertTrue(tuples.contains(tuple_two));
  }

  @Test
  public void testGenerateAllPossibleTuplesStringNull ()
  {
    List<String> paramlist = new ArrayList<String>();
    paramlist.add(null);

    list_map.put("paramC", paramlist);

    List<Map<String, String>> tuples = StringSupport.generateAllPossibleTuples(list_map);

    assertEquals(2, tuples.size());

    tuple_one.put("paramC", "");
    tuple_two.put("paramC", "");

    assertTrue(tuples.contains(tuple_one));
    assertTrue(tuples.contains(tuple_two));
  }

  @Test
  public void testCheckParamtersForStringRendering ()
  {
    StringSupport.checkParametersForStringRendering("cmd1 {paramA} | cmd2 {paramB}",
            param_map);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckParametersForStringRenderingTemplateNull ()
  {
    StringSupport.checkParametersForStringRendering(null, param_map);
  }

  @Test
  public void testCheckParametersForStringRenderingTemplateEmpty ()
  {
    StringSupport.checkParametersForStringRendering("", param_map);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckParametersForStringRenderingParametersNull ()
  {
    StringSupport.checkParametersForStringRendering("cmd1 {paramA} | cmd2 {paramB}", null);
  }

  @Test
  public void testCheckParametersForStringRenderingParametersAndTemplateEmpty ()
  {
    StringSupport.checkParametersForStringRendering("", new HashMap<String, String>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckParametersForStringRenderingParametersInvalidTokens ()
  {
    StringSupport.checkParametersForStringRendering("cmd1 {paramC} | cmd2 {paramB}",
            param_map);
  }

  @Test
  public void testUpdateListOfMaps ()
  {
    List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
    for (Entry<String, List<String>> entry : list_map.entrySet()) {
      parameters = StringSupport.updateListOfMaps(parameters, entry);
    }

    assertEquals(2, parameters.size());
    assertTrue(parameters.contains(tuple_one));
    assertTrue(parameters.contains(tuple_two));
  }

  @SuppressWarnings("unused")
  @Test(expected = IllegalArgumentException.class)
  public void testUpdateListOfMapsParametersNull ()
  {
    List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
    for (Entry<String, List<String>> entry : list_map.entrySet()) {
      parameters = StringSupport.updateListOfMaps(null, entry);
    }
  }

  @Test
  public void testUpdateListOfMapsParametersNonEmpty ()
  {
    List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
    list_map.put("paramC", Arrays.asList("val4"));

    for (Entry<String, List<String>> entry : list_map.entrySet()) {
      parameters = StringSupport.updateListOfMaps(parameters, entry);
    }

    assertEquals(2, parameters.size());

    tuple_one.put("paramC", "val4");
    tuple_two.put("paramC", "val4");

    assertTrue(parameters.contains(tuple_one));
    assertTrue(parameters.contains(tuple_two));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateListOfMapsEntryNull ()
  {
    List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
    StringSupport.updateListOfMaps(parameters, null);
  }
}
