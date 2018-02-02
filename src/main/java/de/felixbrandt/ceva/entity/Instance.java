package de.felixbrandt.ceva.entity;

/**
 * Problem Instance to solve.
 */
@SuppressWarnings("designforextension")
public class Instance implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;

  private int instance;
  private String filename;
  private String checksum;
  private String content;
  private boolean active;
  private int timelimit;

  public Instance()
  {
    this("");
  }

  public Instance(final String name)
  {
    this(0, name, "", "", true, 0);
  }

  public Instance(final int _instance, final String _filename, final String _checksum,
          final String _content, final boolean _active, final int _timelimit)
  {
    instance = _instance;
    filename = _filename;
    checksum = _checksum;
    content = _content;
    active = _active;
    timelimit = _timelimit;
  }

  public int getInstance ()
  {
    return instance;
  }

  public void setInstance (final int instance_id)
  {
    instance = instance_id;
  }

  public String getName ()
  {
    return filename;
  }

  public void setName (final String _filename)
  {
    filename = _filename;
  }

  public String getChecksum ()
  {
    return checksum;
  }

  public void setChecksum (final String _checksum)
  {
    checksum = _checksum;
  }

  public String getContent ()
  {
    return content;
  }

  public void setContent (final String _content)
  {
    content = _content;
  }

  public boolean isActive ()
  {
    return active;
  }

  public void setActive (final boolean state)
  {
    active = state;
  }

  public void setTimelimit (final int _timelimit)
  {
    timelimit = _timelimit;
  }

  public int getTimelimit ()
  {
    return timelimit;
  }
}
