# Does CEVA calculate solutions for old algorithm versions if new instances are added?

No, CEVA does not store your executables of previous versions. Therefore, it cannot run them on new instances. An simple workaround is to set the `run_path` to your old executable and also the appropriate `version`. Then rerun CEVA. It will execute the missing experiments. Afterwards, just return to the previous configuration.

# What to do if CEVA hangs on algorithm/metric crashes (waiting for debugging)?

If you have a Visual Studio Debugger installed, Windows will prompt you for debugging a crashed application. However, if the crashed process was called by CEVA, it will wait for you to handle the prompt and thus block further execution by CEVA.

A simple solution is to disable the UI prompt of Windows Error Reporting. Therefore you need to add a Key `DontShowUI` with value 1 to one of the following paths of your windows registry:

```
HKEY_CURRENT_USER\Software\Microsoft\Windows\Windows Error Reporting
HKEY_LOCAL_MACHINE\Software\Microsoft\Windows\Windows Error Reporting
```
See: https://msdn.microsoft.com/en-us/library/bb513638.aspx