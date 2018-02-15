package de.felixbrandt.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parameter container with supporting types and default options.
 */
public class ParameterMap
{
  private Map<String, ?> data;

  public ParameterMap()
  {
    data = new HashMap<String, Object>();
  }

  public ParameterMap(final Map<String, ?> params)
  {
    if (params == null) {
      data = new HashMap<String, Object>();
    } else {
      data = params;
    }
  }

  public final int size ()
  {
    return data.size();
  }

  @Override
  public boolean equals (Object other)
  {
    if (other instanceof ParameterMap) {
      return data.equals(((ParameterMap) other).data);
    }
    return false;
  }

  public final String getStringParam (final String name)
  {
    return getStringParam(name, "");
  }

  public final String getStringParam (final String name, final String default_value)
  {
    if (data.containsKey(name)) {
      return String.valueOf(data.get(name));
    }

    return default_value;
  }

  public final int getIntParam (final String name)
  {
    return getIntParam(name, 0);
  }

  public final int getIntParam (final String name, final int default_value)
  {
    if (data.containsKey(name)) {
      final Object value = data.get(name);
      if (value instanceof Integer) {
        return (Integer) value;
      }

      return Integer.parseInt((String) data.get(name));
    }

    return default_value;
  }

  public final boolean getBoolParam (final String name)
  {
    return getBoolParam(name, false);
  }

  public final boolean getBoolParam (final String name, final boolean default_value)
  {
    if (data.containsKey(name)) {
      final Object value = data.get(name);
      if (value instanceof Boolean) {
        return (Boolean) value;
      } else {
        return Boolean.parseBoolean((String) value);
      }
    }

    return default_value;
  }

  public final ParameterMap getMapParam (final String name)
  {
    return getMapParam(name, new ParameterMap());
  }

  public final ParameterMap getMapParam (final String name, final ParameterMap default_map)
  {
    if (data.containsKey(name)) {
      final Object value = data.get(name);
      if (value instanceof String) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        String value_no_spaces = ((String) value).replaceAll(" ", "");
        String[] pairs = value_no_spaces.split("\n");
        for (int i = 0; i < pairs.length; i++) {
          String pair = pairs[i];
          String[] key_value = pair.split(":");
          result.put(key_value[0], Arrays.asList(key_value[1].split(",")));
        }
        return new ParameterMap(result);
      }
      return new ParameterMap((Map<String, ?>) data.get(name));
    }

    return default_map;
  }

  public final List<?> getListParam (final String name)
  {
    return getListParam(name, new ArrayList<Object>());
  }

  public final List<?> getListParam (final String name, final List<?> default_list)
  {
    if (data.containsKey(name)) {
      if (data.get(name) instanceof List) {
        return (List<?>) data.get(name);
      }
      return Arrays.asList(data.get(name));
    }

    return default_list;
  }

  public final boolean has (final String key)
  {
    return data.containsKey(key);
  }

  public final Map<String, ?> getRaw ()
  {
    return data;
  }

  public final Set<String> keySet ()
  {
    return data.keySet();
  }

  public boolean isMapParam (String key)
  {
    return data.containsKey(key) && data.get(key) instanceof Map;
  }
}
