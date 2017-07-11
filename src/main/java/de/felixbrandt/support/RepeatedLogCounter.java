package de.felixbrandt.support;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts repetition of log entries
 */
public final class RepeatedLogCounter
{
  private Map<String, Integer> number_of_repeated_logs = new HashMap<String, Integer>();

  public void addLog (final String log)
  {
    if (number_of_repeated_logs.containsKey(log)) {
      number_of_repeated_logs.put(log, number_of_repeated_logs.get(log) + 1);
    } else {
      number_of_repeated_logs.put(log, 1);
    }
  }

  public int countReptitionsOfLog (final String log)
  {
    return number_of_repeated_logs.get(log);
  }

}
