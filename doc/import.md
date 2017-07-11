# Importing Data

Solutions that have been calculated outside CEVA, like those of public benchmark instances, can be imported into the CEVA database. After the import, the next CEVA run will automatically calculate the missing solution metrics for those solutions. The instances related to the solutions must already be present in the CEVA database.

The syntax to use the import is as follows:
```
java -jar ceva.jar CONFIGFILE import -i INSTANCE -a ALGORITHM [-v VERSION] [-m MACHINE] [-t RUNTIME] [-p PARAMS] [-o STDOUT_FILE] [-e STDERR_FILE] < CONTENT
```

The instance can either be given by its filename or id. The name does not need to be complete - just unambiguous - and CEVA will figure out the right instance. The algorithm needs to be given by its full name, but does not need to match the exact upper/lower case. If the version is ommitted, then 0 is assumed.
Machine defaults to `imported`. The runtime is given in seconds. Parameters must not have spaces,. If a option `-o` is given then standard input is ignored.

### Examples
```
java -jar ceva.jar ceva.yml import -i I0815.in.txt -a mysolver < I0815.sol.txt
java -jar ceva.jar ceva.yml import -i I0815.in.txt -a mysolver -o I0815.sol.txt
java -jar ceva.jar ceva.yml import -i I0815.in.txt -a mysolver  -v 42 -m mycomputer -t 1800 -p timelimit:1800 -o I0815.sol.txt -e I0815.sol.log
```
