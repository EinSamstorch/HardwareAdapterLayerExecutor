## HardwareAdapterLayerExcutor

### What's this
This is a hardware adapter layer executor for Intelligent Manufactoring System.
It communicates with Socket Server to decouple with Agents in the machines.  

This executor is not the actual executor we use in our system.
It's a fake executor that I can easily test some new features on virtual machines rather than starting real machines to run debugging.


### How to add new fake executors

1. Inherit the `AbstractExecutor` class.
2. Overwrite `protected void cmdHandler(int taskNo, String cmd, String extra)` method. You can follow the pattern of `AgvExecutor` to write other executors.
