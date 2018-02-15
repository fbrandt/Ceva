package de.felixbrandt.ceva.provider;

import org.hibernate.Query;

public class HQLFilter
{
  /**
   * HQL segment to add into WHERE section.
   * 
   * @param prefix Unique identifier for this filter (to avoid column and parameter name
   *          clashes with other filters)
   */
  String getWhereClause (String prefix)
  {
    return "";
  }

  /**
   * Add parameters to hibernate query.
   * 
   * @param prefix Unique identifier for this filter (to avoid column and parameter name
   *          clashes with other filters)
   */
  void setParametersToQuery (Query query, String prefix)
  {
  }

}
