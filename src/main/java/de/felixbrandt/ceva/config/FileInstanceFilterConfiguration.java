package de.felixbrandt.ceva.config;

import java.util.List;

public class FileInstanceFilterConfiguration implements InstanceFilterConfiguration
{
  private List<String> filenames;

  public FileInstanceFilterConfiguration(List<String> filenames)
  {
    this.filenames = filenames;
  }

  public String getType ()
  {
    return "file";
  }

  public List<String> getFilenames ()
  {
    return filenames;
  }
}
