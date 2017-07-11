package de.felixbrandt.ceva.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * An Algorithm to be run on Instances.
 */
@SuppressWarnings("designforextension")
public class Algorithm extends Rule
{
  private static final long serialVersionUID = 1L;
  private int repeat;
  private HashMap<String, List<String>> parameters;

  public Algorithm()
  {
    super();
    init();
  }

  public Algorithm(final String string)
  {
    super(string);
    init();
  }

  private void init ()
  {
    setRepeat(1);
    setParameters(new HashMap<String, List<String>>());
  }

  public int getRepeat ()
  {
    return repeat;
  }

  public void setRepeat (final int new_repeat)
  {
    this.repeat = new_repeat;
  }

  public final HashMap<String, List<String>> getParameters ()
  {
    return parameters;
  }

  public final void setParameters (final HashMap<String, List<String>> params)
  {
    this.parameters = params;
  }

  public String getParameterString ()
  {
    return new Yaml().dump(parameters);
  }

  /**
   * Hibernate needs a setter, if you want to save an attribute to the DB that is derived by a
   * parsed attribute of the class. For 'Algorithm' has no attribute parameters_string, the
   * setter has to be private. See: http://stackoverflow.com/questions/2676689
   * /does-hibernate-always-need-a-setter-when-there-is-a-getter
   *
   * @param _parameters_string
   */
  @SuppressWarnings("unchecked")
  public void setParameterString (final String _parameters_string)
  {
    this.parameters.clear();

    Map<String, ?> yaml_tree = (Map<String, ?>) new Yaml().load(_parameters_string);
    for (Map.Entry<String, ?> entry : yaml_tree.entrySet()) {
      this.parameters.put(entry.getKey(), (List<String>) entry.getValue());
    }
  }

  @Override
  public final void updateRuleDetails (final Rule from)
  {
    if (from instanceof Algorithm) {
      final Algorithm from_algorithm = (Algorithm) from;
      updateAlgorithmDetails(from_algorithm);
    }
  }

  public final void updateAlgorithmDetails (final Algorithm from_algorithm)
  {
    setRepeat(from_algorithm.getRepeat());
    setParameters(from_algorithm.getParameters());
  }
}
