package de.felixbrandt.ceva.entity;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.felixbrandt.ceva.entity.Rule;

public class RuleTest
{
  Rule rule;

  class TestRule extends Rule
  {
    private static final long serialVersionUID = 1L;

    @Override
    public HashMap<String, List<String>> getParameters ()
    {
      return new HashMap<String, List<String>>();
    }
  };

  @Before
  public void setUp () throws Exception
  {
    rule = new TestRule();
    rule.setBasePath(null);
  }

  @Test
  public void testGetFullVersionPath ()
  {
    assertEquals("", rule.getFullVersionPath());
    rule.setVersionPath(null);
    assertEquals("", rule.getFullVersionPath());
  }

  @Test
  public void testGetFullRunPath ()
  {
    assertEquals("", rule.getFullRunPath());
    rule.setRunPath(null);
    assertEquals("", rule.getFullRunPath());
  }

  @Test
  public void testGetName ()
  {
    rule.setName(null);
    assertEquals("", rule.getName());
  }

  @Test
  public void testGetDescription ()
  {
    rule.setDescription(null);
    assertEquals("", rule.getDescription());
  }

  @Test
  public void testGetBasePath ()
  {
    assertEquals("", rule.getBasePath());
  }

  @Test
  public void testGetVersionPath ()
  {
    rule.setVersionPath(null);
    assertEquals("", rule.getVersionPath());
  }

  @Test
  public void testGetRunPath ()
  {
    rule.setRunPath(null);
    assertEquals("", rule.getRunPath());
  }

  @Test
  public void testUpdateFrom ()
  {
    final TestRule update = new TestRule();
    update.setName("newname");
    update.setDescription("newdescription");
    update.setBasePath("newbasepath");
    update.setVersionPath("newversionpath");
    update.setRunPath("newrunpath");
    update.setActive(true);
    update.setId(23);

    final Rule returned = rule.updateFrom(update);
    assertEquals(rule, returned);

    assertEquals(0, rule.getId());
    assertEquals("newname", rule.getName());
    assertEquals("newdescription", rule.getDescription());
    assertEquals("newbasepath", rule.getBasePath());
    assertEquals("newversionpath", rule.getVersionPath());
    assertEquals("newrunpath", rule.getRunPath());
    assertEquals(true, rule.isActive());
  }

}
