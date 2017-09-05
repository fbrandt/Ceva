# Tutorial: Basic CEVA configuration and workflow
This tutorial explains how to configure instances, algorithms, and metrics for use with CEVA.

## 1. The basic scenario
The following examples all base on the same scenario. Assume that you want to evaluate an application that creates a Minimum Spanning Tree ([MST]) for a given graph. Each graph is given in a text file. The first line of the file states the number of nodes of the graph. Each following line represents an edge given by its two nodes and the edge cost. A simple graph might look like this (`graph1.txt`):
```
4
0 1 1
0 2 1
1 3 2
2 3 1
0 3 3
```
It has 4 nodes (0 to 3) and 5 edges. The third line defines an edge between nodes 0 and 2 with cost 1.

The application (`mst.exe`) uses the system's [standard streams][IO] for input/output. It reads the graph from standard input stream, prints the edges of the MST to standard output stream. Furthermore, some debugging information is written to standard error stream.
```sh
$ mst.exe < graph1.txt
4
[STDERR] checking edge 0 1 1 using edge
0 1 1
[STDERR] checking edge 0 2 1 using edge
0 2 1
[STDERR] checking edge 2 3 1 using edge
2 3 1
[STDERR] checking edge 1 3 2 skipping edge
[STDERR] checking edge 0 3 3 skipping edge
```

Furthermore the `mst.exe` can extract some key figures from the graphs like the number of nodes, edges, and cost values:
```sh
$ mst.exe nodes < graph1.txt
4
$ mst.exe edges < graph1.txt
5
$ mst.exe cost < graph1.txt
8
```

## 2. Configuring CEVA
At first you need to create a CEVA configuration file in [YAML] format, say `ceva.yaml`.
Typically a configuration file specifies the database to use by CEVA as well as the instances, algorithms, and metrics to run.

#### Database
CEVA stores its configuration and all data in a database. Multiple database engines are supported: MySQL and PostgreSQL for server-based storage as well as H2 and SQLite for local file-based storage. Let us assume you have a MySQL database running on `yourserver.com` and an empty database `cevadb` that you have full access to via user `john`. You can configure it as:
```
database:
  type: mysql
  host: yourserver.com
  name: cevadb
  username: john
  password: secret
```
On the first run, CEVA will set up its database schema as needed and import its configuration. Subsequent CEVA runs only require this database configuration.

#### Instances
CEVA expects each instance in a separate file. In the `data` folder you can find some graph files. To add these instances to the CEVA database, you can specify individual files as a list under the `instances` key:
```
instances:
  - data/graph1.txt
```
To add all files of the folder, just set the folder path:
```
instances:
  - data
```
On the next CEVA run these instances will be imported into the CEVA database. The instance configuration is only needed once to import the files. All instances in the database will still be run by CEVA even if they are removed from the configuration file. When importing the files, CEVA generates a checksum for each instance. If you change a file and rerun CEVA, the changed file will be added as a new instance, besides the old one. Filenames of the instances must not be unique. Each instance is given a unique ID in the CEVA database.

#### Algorithms
To let CEVA run your previously configured instances on the `mst.exe` application, you need to configure it under the `algorithms` key. You need to give each algorithm a unique name, lets use `mst` here. The minimum required parameter for each algorithm is `run_path`, the command to run the algorithm. You can now configure the algorithm as:
```
algorithms:
  mst:
    run_path: mst.exe
```

As for the instances, CEVA imports the algorithm configuration on the next run. The algorithms stay in the database even if you remove them from the configuration file. However, if you change the configuration of a certain algorithm name, CEVA will update these changes in the database on the next run. If you improve your applications, you might want to rerun your algorithms to see how they perform. Therefore, algorithms have a `version` attribute. If you increase this number, CEVA will rerun all your instances on the next run:
```
algorithms:
  mst:
    run_path: mst.exe
    version: 42
```

If you do not need an algorithm anymore, you can disable it with the `active` attribute. Removing an algorithm from the database requires to delete all its solutions and solution metrics and might therefore not be what you want to do.
```
algorithms:
  mst:
    run_path: mst.exe
    active: false
```

#### Metrics
CEVA can extract key figures from instances and solutions via metrics. The configuration is similar to that of algorithms. However, CEVA assumes that each metric only returns a single value. This value is stored in the database and available for analysis with your tool of choice. Metric results can either be integers, floats, or strings.

Let us assume you are interested in some stats on the graph size of your instances. Then, you can add instance metrics under the `imetrics´ key:
```
imetrics:
  nodes:
    run_path: mst.exe nodes
  edges:
    run_path: mst.exe edges
```

Stats of the solution files are configured via the `smetrics` key. You might be interested in the total cost of the created MST, so lets add the metric:
```
smetrics:
  cost:
    run_path: mst.exe cost
```

## 3. Running CEVA

```
$ java -jar ceva.jar ceva.yml
14:49:15.958 [main] INFO  InstanceImportService - instance graph1.txt imported
14:49:15.997 [main] INFO  Main - selected strategy: SynchronuousExecutionStrategy
14:49:16.025 [main] INFO  BatchController - filter sources for metric nodes
14:49:16.042 [main] INFO  UnsolvedSourcesFilter - found 1 missing values for metric nodes
14:49:16.042 [main] INFO  BatchController - running metric nodes for graph1.txt(0/1)
...
14:49:16.415 [main] INFO  BatchController - filter sources for algorithm mst
14:49:16.419 [main] INFO  UnsolvedSourcesFilter - found 1 missing values for algorithm mst
14:49:16.419 [main] INFO  BatchController - running algorithm mst for graph1.txt(0/1)
...
14:49:16.495 [main] INFO  BatchController - filter sources for metric cost
14:49:16.497 [main] INFO  UnsolvedSourcesFilter - found 1 missing values for metric cost
14:49:16.519 [main] INFO  BatchController - running metric cost for solution to graph1.txt(0/1)
...
14:49:16.827 [main] INFO  Main - done
```
Simply start CEVA by calling it on the command line with the configuration file given as first parameter. At first, CEVA will import the configuration and instances into the database. Afterwards, the missing instance metrics are calculated, followed by all missing solutions and solution metrics. When all is done, CEVA stops.
## 4. Exporting results
You can export the calculated metrics to CSV format, for further processing. To dump all values, use the following commands. Remember to redirect the output into a file via appending a `> myfile.csv`.
```
$ java -jar ceva.jar ceva.yml imetrics
ID;INSTANCE;METRIC;VALUE
1;graph1.txt;nodes;4
1;graph1.txt;edges;5
$ java -jar ceva.jar ceva.yml smetrics -a mst
ID;INSTANCE;ALGORITHM;PARAMETERS;VERSION;METRIC;VALUE;REPEAT
1;graph1.txt;mst;;0;cost;3;1
```

## 5. A full configuration file
```
database:
  type: mysql
  host: yourserver.com
  name: cevadb
  username: john
  password: secret
instances:
  - data
algorithms:
  mst:
    run_path: mst.exe
imetrics:
  nodes:
    run_path: mst.exe nodes
  edges:
    run_path: mst.exe edges
smetrics:
  cost:
    run_path: mst.exe cost
```

 [MST]: https://en.wikipedia.org/wiki/Minimum_spanning_tree
 [IO]: https://en.wikipedia.org/wiki/Redirection_(computing)
 [YAML]: https://en.wikipedia.org/wiki/YAML