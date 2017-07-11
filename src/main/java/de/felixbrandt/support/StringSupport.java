package de.felixbrandt.support;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This helper class provides methods for string processing.
 */
public final class StringSupport
{

  /**
   * Indicates the token pattern that is used for rendering the run path given in the .yaml file.
   * [ -z|~]* defines (possibly empty) strings of printable ASCII symbols, without curly braces.
   */
  public static final Pattern TOKEN_PATTERN = Pattern.compile("\\{([ -z|~]*)\\}");

  /**
   * Prevents instantiation of this helper class outside of
   * the package.
   */
  private StringSupport()
  {
  }

  /**
   * Method creates a string representation of a HashMap<String, String>.
   * Before building the String, the HashMap is sorted by its keys.
   *
   * @param map Contains String tuples.
   * @return A String of kind key1: value1;key2: value2;...
   *
   */
  public static String getMapAsString (final Map<String, String> map)
  {
    if (map == null) {
      return "";
    }

    final List<String> keylist = new ArrayList<String>(map.keySet());
    Collections.sort(keylist);

    final int keylistsize = keylist.size();
    final StringBuffer buf = new StringBuffer();

    for (int i = 0; i < keylistsize; i++) {
      final String key = keylist.get(i);

      if (map.get(key) == null) {
        buf.append(key + ": ;");
      } else {
        buf.append(key + ": " + map.get(key) + ";");
      }

    }
    return buf.toString();
  }

  /**
   * Method replaces all tokens that match {@link #TOKEN_PATTERN} by
   * a parameter, which name matches the given pattern (Without the braces
   * of the group. See:
   * http://docs.oracle.com/javase/7/docs/api/java/util/regex/Matcher.html#group%28int%29)
   *
   * @param template A template String that may hold tokens.
   * @param parameters The parameters, that will replace the tokens.
   * @return The template String, with all tokens replaced by matching parameters.
   */
  public static String renderString (final String template,
          final Map<String, String> parameters) throws IllegalArgumentException
  {

    checkParametersForStringRendering(template, parameters);

    final StringBuffer rendered_string = new StringBuffer();
    final Matcher matcher = TOKEN_PATTERN.matcher(template);

    while (matcher.find()) {

      // Appends all non-token symbols since the last token replacement
      // and replaces the token with an empty string.
      matcher.appendReplacement(rendered_string, "");

      // group(1) refers to group 1 in the RegEx of tokens which is
      // characterized by parenthesis.
      // See http://docs.oracle.com/javase/7/docs/api/java/util/regex/Matcher.html#group%28int%29
      String parameter_token = matcher.group(1);

      // appends the replacement of a token.
      if (parameters.get(parameter_token) == null) {
        rendered_string.append("");
      } else {
        rendered_string.append(parameters.get(parameter_token));
      }

    }

    // appends the rest of the run path behind the last token.
    matcher.appendTail(rendered_string);
    return rendered_string.toString();
  }

  /**
   * Checks the validity of a string template (that may contain tokens) and a
   * parameters map.
   *
   * @param template A template String that may hold tokens.
   * @param parameters The parameters, that will replace the tokens.
   * @throws IllegalArgumentException iff the template, the parameters or the
   * combination of both is not valid.
   */
  public static void checkParametersForStringRendering (final String template,
          final Map<String, String> parameters) throws IllegalArgumentException
  {
    if (template == null) {
      throw new IllegalArgumentException(
              "StringSupport::renderString() parameter 'template' must not be null.");
    }

    if (parameters == null) {
      throw new IllegalArgumentException(
              "StringSupport::renderString() parameter 'parameters' must not be null.");
    }

    Set<String> undefined_tokens = findUndefinedTokens(template, parameters.keySet());

    if (undefined_tokens.size() > 0) {
      StringBuilder b = new StringBuilder();
      for (String s : undefined_tokens) {
        //Format the exception message, to enumerate empty tokens
        //as a readable string as well.
        if (s.trim().length() == 0) {
          b.append("Empty token;");
        } else {
          b.append(s + "; ");
        }
      }
      throw new IllegalArgumentException(
              "No parameters were found for tokens: " + b.toString());
    }

  }

  /**
   * Method searches for all undefined tokens in a given String.
   *
   * @param template A template String, that may hold tokens.
   * @param defined_tokens A set of tokens, which are valid.
   * @return A set of tokens, that were found in the template String, but are not valid.
   */
  public static Set<String> findUndefinedTokens (final String template,
          final Set<String> defined_tokens)
  {
    Set<String> undefined_tokens = getTokens(template);
    undefined_tokens.removeAll(defined_tokens);

    return undefined_tokens;
  }

  /**
   * Method searches for all tokens that match {@link #TOKEN_PATTERN} and returns them.
   *
   * @param template A template String, that may hold tokens.
   * @return A set of all found tokens.
   */
  public static Set<String> getTokens (final String template)
  {
    HashSet<String> params = new HashSet<String>();
    Matcher matcher = TOKEN_PATTERN.matcher(template);

    while (matcher.find()) {
      params.add(matcher.group(1));
    }
    return params;
  }

  /**
   * Method generates a list of maps that covers all possible tuples of elements of the lists in
   * the given maps.
   * If a given map should be empty or null, it will be treated as a list that contains
   * the empty string only.<br>
   * <H3>Examples</H3>
   * <ol>
   * <li> If the following map is given:<br>
   * { ("A", ["1", "2"]), ("B",["3"]) }<br>
   * The following list of maps will be created:<br>
   * [ {("A", "1"), ("B", "3") }, {("A", "1"), ("B", "3") }]<br>
   * <br>
   * <li> A special case would be, if a list in the given map is empty or null.
   * Then the following mapping will be used:<br>
   * { ("A", ["1"]), ("B",[]) } -> [ {("A", "1"), ("B", "") }]<br>
   * { ("A", ["1"]), ("B",<b>null</b>) } -> [ {("A", "1"), ("B", "") }]
   * </ol>
   * @param map_of_sets The map of the named String lists.
   * @return A list of maps that covers all possible tuples of the elements of the lists in
   * the given maps.
   */
  public static List<Map<String, String>> generateAllPossibleTuples (
          final Map<String, List<String>> map_of_sets)
  {
    /*
     * Defines and initializes an empty list of HashMaps (used for parameters) and adds an empty
     * one.
     */
    List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();

    if (map_of_sets == null || map_of_sets.isEmpty()) {
      return parameters;
    }

    /*
     * Iterate through all parameter entries and replace for each parameter value of the entry,
     * every HashMap H, that is already in the parameters list, a new HashMap H' that contains all
     * entries of H, plus the actual parameter value. In short: H' = H + (parameter_name,
     * parameter_value) If a list of values is null or empty, treat it like a list, that contains an
     * empty string.
     */
    parameters.add(new HashMap<String, String>());
    for (final Entry<String, List<String>> tmp_entry : map_of_sets.entrySet()) {
      parameters = updateListOfMaps(parameters, tmp_entry);
    }
    return parameters;
  }

  /**
   * Method extends every map of list 'parameters' by a value of list 'value' with key 'key'
   * and saves it to the list 'tmp_parameters'
   *
   * @param parameters List of Maps to be extended
   * @param new_parameters List of extended maps
   * @param key Key of the values to extend with.
   * @param value Values to extend with.
   */
  public static List<Map<String, String>> updateListOfMaps (
          final List<Map<String, String>> parameters, final Entry<String, List<String>> entry)
  {

    if (parameters == null || entry == null) {
      throw new IllegalArgumentException(
              "StringSupport::updateListOfMaps() does not take null pointer as arguments");
    }

    if (parameters.isEmpty()) {
      parameters.add(new HashMap<String, String>());
    }

    List<Map<String, String>> new_parameters = new ArrayList<Map<String, String>>();

    List<String> entry_value = entry.getValue();
    if (entry_value == null || entry_value.isEmpty()) {
      entry_value = Arrays.asList("");
    }

    for (final String s : entry_value) {
      for (final Map<String, String> map : parameters) {
        final Map<String, String> new_map = new HashMap<String, String>(map);

        if (s == null) {
          new_map.put(entry.getKey(), "");
        } else {
          new_map.put(entry.getKey(), s);
        }

        new_parameters.add(new_map);
      }
    }
    return new_parameters;
  }

  /**
  * Method calculates the number of possible tuples of the elements of the lists in
  * the given maps. See {@link #generateAllPossibleTuples}.
  *
  * @param map The map of the named String lists.
  * @return Number of possible tuples of elements of the lists in the given maps.
  */
  public static long numberOfPossibleTuples (final Map<String, List<String>> map)
  {
    if (map == null || map.isEmpty()) {
      return 0;
    }

    long number_tuples = 1;

    for (List<String> list : map.values()) {
      // Empty (or null) lists do not affect the number of possible combinations.
      if (list == null || list.isEmpty()) {
        continue;
      }

      // Check whether there are too many possible combination
      if (number_tuples > Integer.MAX_VALUE / list.size()) {
        throw new BufferOverflowException();
      }
      number_tuples *= list.size();
    }

    return number_tuples;
  }

}
