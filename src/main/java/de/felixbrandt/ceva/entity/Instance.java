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

  public Instance()
  {
    this(0, "", "", "", true);
  }

  public Instance(final String name)
  {
    this(0, name, "", "", true);
  }

  public Instance(final int _instance, final String _filename, final String _checksum,
          final String _content, final boolean _active)
  {
    instance = _instance;
    filename = _filename;
    checksum = _checksum;
    content = _content;
    active = _active;
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
}
