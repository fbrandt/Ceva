# Configuration
CEVA is configured using a single config file written in [YAML](http://en.wikipedia.org/wiki/YAML). YAML syntax is easy-to-learn and human-readable.

All CEVA settings in the config file related to the experiments (instances, metrics, and algorithms) will be imported into the experiment database and are then available for future use. Even if they are removed from the config file. So, instead of using a configuration file, you could also modify these settings directly in the database.

New and changed settings in the configuration file are updated in the database on the next CEVA run. Beware: removing settings from the configuration file does NOT delete those settings in the database, as this might also delete results of the experiments. See the `active` parameter how to disable metrics or algorithms. 

Configuration details not related to the experiment data, like the database connection and runtime execution worker settings, can only be configured in the configuration file.

# Table of contents

  * [A full example](#full-example)
  * [Database Configuration](#database-configuration)
  * [Worker Configuration](#worker-configuration)
  * [Instance Configuration](#instance-configuration)
  * [Instance Metric Configuration](#instance-metric-configuration)
  * [Algorithm Configuration](#algorithm-configuration)
  * [Solution Metric Configuration](#solution-metric-configuration)
  * [Execution Configuration](#execution-configuration)
  
# Full example
```
database:
  type: pgsql
  name: myexperiments
  username: jondoe
  password: secret
queue:
  worker: 2
instances:
  - data/set1
  - test/dummy.txt
imetrics:
  filesize:
    run_path: wc -c
algorithms:
  sortfile:
    run_path: sort
smetrics:
  filesize:
    run_path: wc -c
 ```

# Database Configuration
CEVA uses a database to store its experiment configuration as well as the collected data (instances, solutions, metric results). Currently it supports the following database systems: [PostgreSQL](http://www.postgresql.org), [MySQL](https://www.mysql.com), [SQLite](https://www.sqlite.org/) and the Java in-memory database [H2](http://www.h2database.com).

Use the `database` parameter to specify the database settings CEVA uses to connect to the database. The database has to be writable by the given user and should initially be empty. On the first run CEVA will initialize the database schema as required.

### Parameters
 * `type`: DBMS type (`h2`, `mysql`, `pgsql`, `sqlite`, default: `h2`)
 * `host`: The database host name (default: `localhost`)
 * `port`: The database port (default depending on type)
 * `name`: The database name (default: `ceva`)
 * `username`: The database user (default: `ceva`)
 * `password`: The database password (default: empty string)

### Database-specific behaviour
| Database | Comment |
|-|-|
| **H2**         | The database name is used as filename. |
| **MySQL** | The default port is 3306. |
| **PostgreSQL** | The default port is 5432. |
| **SQLite** | The database name is used as filename. |

### Example
```
database:
  type: pgsql
  host: example.org
  username: jondoe
  password: secretkey
```

# Worker Configuration

By default CEVA executes all experiments and metrics with one worker on the local machine one after another. But, experiments and metrics can also be run in parallel, either on the local machine or distributed via a [Gearman](http://gearman.org/) job server. Therefore, the number of workers in a CEVA instance can be configured with the `worker` parameter. In the distributed case, you need to start a CEVA instance on each host and set the `mode` parameter. There needs to be a single CEVA instance in mode `master` that manages the database and submits the jobs which need to be done into a job queue. On the other end multiple CEVA instances in mode `slave` run the jobs and submit the results back to the master instance. Both master and slave instances can run multiple worker threads to do the work. If no Gearman `host` attribute is given, only workers of the local CEVA instance will execute the jobs. 

### Parameters
 * `host`: Host address of the Gearman job server to send/receive jobs and results (default: empty)
 * `idle_timeout`: Stop worker after receiving no job for n seconds (no timeout: 0, default: 0)
 * `job_queue`: Name of the Gearman queue to send/receive results (default: `ceva`)
 * `mode`: runtime mode of this CEVA instance: `master` or `slave` (default: `master`)
 * `port`: Port of the Gearman job server (default: 4730)
 * `worker`: The number of local workers to start (default: 1)

### Example Master Configuration
```
queue:
  host: gearman.example.org
  port: 4730
  job_queue: myexperiments
  worker: 0
```

### Example Slave Configuration
```
queue:
  host: gearman.example.org
  port: 4730
  mode: slave
  job_queue: myexperiments
  worker: 4
```

# Instance Configuration
CEVA reads the list of instance files from the `instances` parameter. Each entry can either be a file or a folder. If a folder is given, then all files in this folder (and its subfolders) will be imported as instances. No wildcards are supported in the instance definition. Instances will be identified by their checksum (MD5). Therefore, it is possible to import multiple files of the same name. If a file's MD5 checksum changes (i.e., even if only whitespace is changed) it will be imported as a new instance, to not break existing experiment results.

### Example
```
instances:
  - data/set1/instance_file.txt
  - data/set2
```

# Instance Metric Configuration
CEVA imports the list of instance metrics from the `imetrics` parameter. Metrics will be identified by their name. So, changing a metric name adds a new metric to the database. Changes to the metric's parameters will update the definition in the database at the next CEVA run.

### Parameters
 * `active`: Should this metric be executed by CEVA? (default: `true`)
 * `base_path`: Path prefix for both `version_path` and `run_path` (default: empty)
 * `description`: Free text description of the metric (default: empty)
 * `run_path`: Shell command to execute the metric (default: empty)
 * `type`: Return type of the metric: `float`, `integer`, `string` (default: `integer`)
 * `version_path`: Shell command returning the current version of the metric (default: empty, i.e., version is 0)
 * `version`: Fixed version number, considered only if `version_path` is not set (default: 0)

### Example
```
imetrics:
  customers:
    base_path: ./problem_solver/program.exe
    version_path: --version
    run_path: --count_customers
  line_count:
    run_path: wc -l
    type: integer
    active: false
```

# Algorithm Configuration
CEVA imports the list of solution procedures from the the `algorithms` parameter. The configuration is similar to that of the instance metrics, except that algorithms do not have a return `type`.
Algorithms will be identified by their name. So, changing a algorithm name adds a new algorithm to the database. Changes to the algorithm's parameters will update the definition in the database at the next CEVA run. The same algorithm can be run with different parameter settings. Therefore, the `run_path` parameter of the algorithm may hold named tokens, that will be replaced by parameter values taken from a list. The syntax of tokens is `{token_name}`. They are only available in the `run_path` attribute.

### Parameters
 * `active`: Should this algorithm be executed by CEVA? (default: `true`)
 * `base_path`: Path prefix for both `version_path` and `run_path` (default: empty)
 * `description`: Free text description of the metric (default: empty)
 * `run_path`: Shell command to execute the metric (default: empty)
 * `version_path`: Shell command returning the current version of the metric (default: empty, i.e., version is 0)
 * `version`: Fixed version number, considered only if `version_path` is not set (default: 0)
 * `repeat`: Number of repeated executions of the algorithm per instance (default: 1)
 * `parameters`: List of parameter values, which hold a **string** list of values, each. The values of the parameters will be used to replace tokens of the form `{parameter_name}` that were set in the `run_path` of the algorithm. While running the experiments, all combinations of the given parameters will be used.

### Basic example
```
algorithms:
  main:
    base_path: ./problem_solver/program.exe
    version_path: --version
```

### Example with parameters
```
algorithms:
  main:
    run_path: cmd1 {firstParameter} /{secondParameter} | cmd2 {thirdParameter}
    version: 42
    parameters:
      firstParameter: ["1", "2", "3"]
      secondParameter: ['b', 'a', 'r']
      thirdParameter: 
        - 'reverse' 
        - 'shuffle' 
```

In YAML, there are two possible notations for lists. The first one is to use squared brackets, as shown in parameter `firstParameter` in the example. The other way is specify a list by indented dashes, as shown in parameter `thirdParameter`. Besides the syntax for lists, there are two possible notations for strings as well: With double quotes, as used for values of `firstParameter`, and with single quotes, as used for values of `secondParameter`.

# Solution Metric Configuration

CEVA imports the list of solution metrics from the `smetrics` parameter. The parameters and behaviour are equal to the [Instance Metric Configuration](#instance-metric-configuration). The only exception is the additional `input` parameter to determine if the solutions `stdout` stream or `stderr` stream is fed into the metric.

### Parameters
 *  `input`: Source of solution data (`stdout` or `stderr`) to use as input (default: `stdout`)

### Example
```
smetrics:
  objective:
    base_path: ./problem_solver/calculate_obj
    version_path: --version
  errorcount:
    run_path: wc -l
    input: stderr
```

# Execution Configuration

By default CEVA calculates all missing solutions and metric values on all instances and solutions when it is started. But, CEVA can be configured to only check certain metrics, algorithms, or instances. Active instances can be filtered by their instance metric values. The metrics and algorithms themselves can be toggled on and off via whitelists/blacklists. The execution configuration is not placed in the CEVA database and needs to be given via the configuration script on each CEVA run.

## Active instances

To only consider instances with a certain metric value during a CEVA run, just set the following filter:
```
execute:
  instances:
    - metric: metricname
      value: 42
```

To filter for a set of values, use the `values` attribute:
```
execute:
  instances:
    - metric: metricname
      value: ["tag-A", "tag-B"]
```

The filter works for all types of instance metrics. For metrics of type string, there is also a filter that matches against the given string. The `contains` attribute can also take a set of patterns to match and each instance matching any pattern is used. 

```
execute:
  instances:
    - metric: metricname
      contains: "tag-A"
```

There can also be multiple filters. In this case only instances passing all filters are considered.  
```
execute:
  instances:
    - metric: metricname
      contains: "tag-A"
    - metric: hardproblem
      value: 1
```

## Active algorithms and metrics

Enabling only certain algorithms or metrics in a CEVA run works via the `imetrics`, `algorithms`, and `smetrics` attributes of the `execute` config path. By default all categories (algorithms and metrics) are run. To skip a certain category completely, just set it to false:
```
execute:
  algorithms: false
```

To run only specific algorithms/metrics list them under the `include` attribute:
```
execute:
  imetrics:
    include:
      - myactivemetric
```

Similarly, certain algorithms/metrics can be blacklisted with the `exclude` attribute:
```
execute:
  smetrics:
    exclude:
      - myinactivemetric
```
