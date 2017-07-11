# CEVA
CEVA is a tool for automating your computational experiments and to keep track of the results while your software is evolving. The name CEVA is short for **C**ontinuous **EVA**luation. The idea of CEVA is similar to [continuous integration][CI], where software is tested regularly and automatically to detect errors as early as possible. CEVA on the other hand focuses on the quality of the solutions your software produces. Therefore, CEVA executes your project's benchmark problem instances on your algorithms and collects metrics from the returned solutions. CEVA works with any executables that can be run via the command line and communicates with the processes through their standard input and output streams. It is highly configurable and not bound to projects of a certain programming language. All algorithms and metrics are configured by their command line call for maximum freedom. CEVA employs a full factorial experiment design. It keeps track of all experiments that need to be done and stores all results in a database. The evaluation can be parallelized and distributed across multiple machines by CEVA. Run CEVA regularly and automatically, ideally after your CI build, so you can see if/how changes in your project impact solution quality.

## Documentation
  * [Basic principles](doc/basics.md)
  * [Configuration](doc/config.md)
  * [Importing Data](doc/import.md)
  * [Exporting Data](doc/export.md)
  * [Frequently Asked Questions](doc/faq.md)

## Tutorials
  * [Basic CEVA configuration and workflow](doc/howto/basics.md)
  * [Distributed execution of experiments](doc/howto/gearman.md)

## Building CEVA
CEVA is built with [Maven][MVN]. Simply clone the repository and run the Maven `assembly:single` target. It builds a jar file that can be run with Java.
```sh
$ git clone https://github.com/fbrandt/ceva/
$ mvn clean compile test assembly:single
```

You find the jar in the folder `target`.

## Running CEVA
To run CEVA the jar must be started with Java. A configuration file can be given as first command line parameter. By default the configuration is read from a file named `ceva.yml`.
```sh
$ java -jar ceva.jar ceva.yml
```
If executed this way, CEVA will update the configuration in the database, import new instances if found, and run missing experiments and metrics. Further execution modes to import/export data from the database exist, see above.

[CI]: https://en.wikipedia.org/wiki/Continuous_integration
[MVN]: https://maven.apache.org/
