# Exports
The data collected and stored with CEVA can be exported in different ways. The instance and solution files can be exported in raw format. Furthermore, the collected metrics can be exported in a CSV format. 

## Table of contents
  * [Exporting instance files](#exporting-instance-files)
  * [Exporting instance data](#exporting-instance-data)
  * [Exporting solution files](#exporting-solution-files)
  * [Exporting solution data](#exporting-solution-data)

## Exporting instance files
The instance files managed by CEVA can by exported by the following command: 
```
java -jar ceva.jar CONFIGFILE instance [-i INSTANCE_KEYWORD]
```

#### Parameters
 * `INSTANCE_KEYWORD`: is either the unique internal CEVA id given to the instance or an unambiguous string pattern that matches the instance name. 

#### Output
The instance file is written to standard output, i.e., can just be written into a file. If multiple matching instances are found, a list of alternatives is printed to standard output.

## Exporting instance data
The instance metrics collected by CEVA can be exported in CSV format by the following command:
```
java -jar ceva.jar CONFIGFILE imetrics [-i INSTANCE_KEYWORD] [-m METRIC]
```

`INSTANCE_KEYWORD` and `METRIC` can be omitted, to return all value, or contain wildcards (*), to return the values of matching metrics only.

#### Parameters
 * `INSTANCE_KEYWORD`: is either the unique internal CEVA id given to the instance or an unambiguous string pattern that matches the instance name. 
 * `METRIC`: an instance metric name.

#### Output
The exported lines have the following format:
```
ID;INSTANCE;METRIC;VALUE
```
 * `ID`: CEVA's internal id of the value.
 * `INSTANCE`: the instance name.
 * `METRIC`: the metric name.
 * `VALUE`: the value (type depending on metric type).

## Exporting solution files

The solutions stored by CEVA can be exported either via the solution's `ID` or its related instance, algorithm, and version. If the version is omitted, you get the executed result with the highest version number.
```
 * java -jar ceva.jar CONFIGFILE solution -s ID
 * java -jar ceva.jar CONFIGFILE solution -i INSTANCE_KEYWORD -a ALGORITHM [-v VERSION]
```
 
 #### Parameters
 * `ID`: CEVA's internal id of the solution.
 * `INSTANCE_KEYWORD`: either the unique internal CEVA id given to the instance or an unambiguous string pattern that matches the instance name. 
 *  `ALGORITHM`: the algorithm name.
 *  `VERSION`: the solution version and defaults to the latest version if omitted.

#### Output
The found solution is written to standard output. If multiple matching solutions are found, a list of alternatives is printed to standard output.

## Exporting solution data
The solution metrics collected by CEVA can be exported in CSV format by the following command.
```
java -jar ceva.jar CONFIGFILE smetrics -a ALGORITHM [-v VERSION] [-i INSTANCE_KEYWORD] [-m METRIC]
```
`VERSION`, `INSTANCE` and `METRIC` can be omitted, to return all solution data of a specific algorithm. `INSTANCE` and `METRIC` may contain wildcards (*), to return matching values only. `VERSION` defaults to the highest version number of the metric.

#### Parameters
 * `ALGORITHM`: the algorithm name.
 * `VERSION`: the solution version and defaults to the latest version if omitted.
 * `INSTANCE_KEYWORD`: either to the internal CEVA id given to the instance or an unambiguous string pattern that matches the instance name. 
 * `METRIC`: the metrics name.

#### Output
The exported lines have the following CSV format:
```
ID;INSTANCE;ALGORITHM;PARAMETERS;VERSION;METRIC;VALUE;REPEAT
```

 * `ID`: CEVA's internal solution id.
 * `INSTANCE`: the instance name.
 * `ALGORITHM`: the algorithm name.
 * `PARAMETERS`: the algorithm parameter that were used to calculate the solution.
 * `VERSION`: the solution version.
 * `METRIC`: the metric name.
 * `VALUE`: the value of the solution.
 * `REPEAT` is the number of repeated calculations.
