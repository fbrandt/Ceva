package de.felixbrandt.ceva.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Application configuration of problem instances.
 */
public class InstanceConfiguration
{
  private final List<?> instance_configs;

  public InstanceConfiguration()
  {
    this.instance_configs = new ArrayList<Object>();
  }

  public InstanceConfiguration(final List<?> params)
  {
    this.instance_configs = params;
  }

  public final List<InstanceFile> getInstances ()
  {
    final List<InstanceFile> list = new ArrayList<InstanceFile>();

    for (int i = 0; i < instance_configs.size(); ++i) {
      final String path = (String) instance_configs.get(i);
      list.addAll(getInstanceFolder(path));
    }

    return list;
  }

  public final List<InstanceFile> getInstanceFolder (final String path)
  {
    final List<InstanceFile> list = new ArrayList<InstanceFile>();

    final File base = new File(path);
    final ArrayList<File> file_list = new ArrayList<File>();
    file_list.add(base);
    File[] files = file_list.toArray(new File[file_list.size()]);

    if (base.listFiles() != null) {
      files = base.listFiles();
    }

    if (files != null) {
      for (final File file : files) {
        if (file.isDirectory()) {
          list.addAll(getInstanceFolder(file.getPath()));
        } else {
          list.add(getInstanceFile(file.getPath()));
        }
      }
    }

    return list;
  }

  public final InstanceFile getInstanceFile (final String path)
  {
    return new InstanceFileImpl(path);
  }
}
