package de.felixbrandt.ceva.config;

import de.felixbrandt.support.ParameterMap;

/**
 * Application database configuration.
 */
public class DBConfiguration
{
  private String dbtype;
  private String host;
  private int port;
  private String dbname;
  private String username;
  private String password;

  public static final int DEFAULT_PORT = 0;

  public DBConfiguration()
  {
    init(new ParameterMap(null));
  }

  public DBConfiguration(final ParameterMap params)
  {
    init(params);
  }

  private void init (final ParameterMap params)
  {
    dbtype = params.getStringParam("type", "h2");
    host = params.getStringParam("host", "localhost");
    port = params.getIntParam("port", DEFAULT_PORT);
    dbname = params.getStringParam("name", "ceva");
    username = params.getStringParam("username", "ceva");
    password = params.getStringParam("password", "");
  }

  public final String getType ()
  {
    return dbtype;
  }

  public final String getHost ()
  {
    return host;
  }

  public final int getPort ()
  {
    return port;
  }

  public final String getDatabase ()
  {
    return dbname;
  }

  public final String getUsername ()
  {
    return username;
  }

  public final String getPassword ()
  {
    return password;
  }

  public final void setType (final String _dbtype)
  {
    this.dbtype = _dbtype;
  }

  public final void setHost (final String _host)
  {
    this.host = _host;
  }

  public final void setPort (final int _port)
  {
    this.port = _port;
  }

  public final void setDatabase (final String _dbname)
  {
    this.dbname = _dbname;
  }

  public final void setUsername (final String _username)
  {
    this.username = _username;
  }

  public final void setPassword (final String _password)
  {
    this.password = _password;
  }

}
