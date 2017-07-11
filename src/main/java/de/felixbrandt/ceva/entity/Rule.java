package de.felixbrandt.ceva.entity;

import java.util.HashMap;
import java.util.List;

/**
 * Rule how to perform actions (e.g. metrics or algorithms).
 */
@SuppressWarnings("designforextension")
public abstract class Rule implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  private String description;
  private String base_path;
  private String version_path;
  private String run_path;
  private boolean active;

  public Rule()
  {
    this(0, "", "", "", "", "", true);
  }

  public Rule(final String _name)
  {
    this(0, _name, "", "", "", "", true);
  }

  public Rule(final int _id, final String _name, final String _description,
          final String _base_path, final String _version_path, final String _run_path,
          final boolean _active)
  {
    super();
    id = _id;
    name = _name;
    description = _description;
    base_path = _base_path;
    version_path = _version_path;
    run_path = _run_path;
    active = _active;
  }

  public int getId ()
  {
    return id;
  }

  public void setId (final int rule_id)
  {
    id = rule_id;
  }

  public String getFullVersionPath ()
  {
    String space = "";
    if (!getBasePath().equals("") && !getVersionPath().equals("")) {
      space = " ";
    }

    return getBasePath() + space + getVersionPath();
  }

  public String getFullRunPath ()
  {
    String space = "";
    if (!getBasePath().equals("") && !getRunPath().equals("")) {
      space = " ";
    }

    return getBasePath() + space + getRunPath();
  }

  public boolean isActive ()
  {
    return active;
  }

  public String getName ()
  {
    if (name != null) {
      return name;
    }

    return "";
  }

  public void setName (final String _name)
  {
    name = _name;
  }

  public String getDescription ()
  {
    if (description != null) {
      return description;
    }

    return "";
  }

  public void setDescription (final String _description)
  {
    description = _description;
  }

  public String getBasePath ()
  {
    if (base_path != null) {
      return base_path;
    }

    return "";
  }

  public void setBasePath (final String _base_path)
  {
    base_path = _base_path;
  }

  public String getVersionPath ()
  {
    if (version_path != null) {
      return version_path;
    }

    return "";
  }

  public void setVersionPath (final String _version_path)
  {
    version_path = _version_path;
  }

  public String getRunPath ()
  {
    if (run_path != null) {
      return run_path;
    }

    return "";
  }

  public void setRunPath (final String _run_path)
  {
    run_path = _run_path;
  }

  public void setActive (final boolean state)
  {
    active = state;
  }

  public final Rule updateFrom (final Rule from)
  {
    setName(from.getName());
    setDescription(from.getDescription());
    setBasePath(from.getBasePath());
    setVersionPath(from.getVersionPath());
    setRunPath(from.getRunPath());
    setActive(from.isActive());

    updateRuleDetails(from);

    return this;
  }

  public abstract HashMap<String, List<String>> getParameters ();

  public void updateRuleDetails (final Rule from)
  {

  }
}
