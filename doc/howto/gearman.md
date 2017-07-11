# Tutorial: Distributed execution of experiments
This tutorial explains how to execute your experiments on multiple machines and collect all results with CEVA.
Therefore, we set up one machine as CEVA master. This machine manages all jobs and database communication. Another machine will be set up as CEVA slave. It receives jobs from the master via a [Gearman Job Server][GM] and returns the results back to the master.

## 1. The basic scenario
We use the same MST example as in our first [tutorial](basics.md). To execute our experiments we have two machines, say `MASTER` and `SLAVE`. Furthmore, we have a running Gearman Job Server at `GEARMAN`.

## 2. Configuring CEVA
At first, we need to configure the CEVA master and slave instances separately.

#### Configuring MASTER
Under the `queue` key we tell CEVA to run in master mode and send the jobs to a job queue. First, we set the mode to `master` and specify the host that runs Gearman (we use the default port 4730 here, so we do not need to specify it). All jobs will be send to a queue named `cevatest`. Furthermore, the master machine should also run two worker processes. Be aware, that CEVA requires some machine capacity. So, if you run a CEVA master with four workers on a machine with four cores, solution performance will degrade.
```
queue:
  mode: master
  host: GEARMAN
  queue: cevatest
  worker: 2
```

#### Configuring SLAVE
CEVA slaves are configured in the same way as master instances. Under the `queue` key, set the mode to `slave` and connect CEVA to the same Gearman Job Server. On a slave machine, CEVA has no computation overhead. This means, if all your jobs require only one thread, it is save to run as many workers on a slave machine as there are cores.
```
queue:
  mode: slave
  host: GEARMAN
  queue: cevatest
  worker: 4
```

## 3. Running CEVA
Assume, we stored the CEVA configurations in `ceva.master.yaml` and `ceva.slave.yaml`. To be able to run all commands from both (master and slave) machines, we recommend to use a shared filesystem. First, you should start the CEVA slave on the command line via:
```
$ java -jar ceva.jar ceva.slave.yaml
```
The command now waits for jobs, i.e., nothing will be printed on the screen until the first jobs arrive via the job server. Now let's start the master instance on the other machine:
```
$ java -jar ceva.jar ceva.master.yaml
```
CEVA now searches for missing experiments and metrics. It will send them to the job server. Then, the local workers on the master as well as all workers on the slave will execute the work. The master instance continuously collects the results and stores them into the database. If all work is done, the master stops. The slave instances continue to wait for future experiments.

 [GM]: http://gearman.org/