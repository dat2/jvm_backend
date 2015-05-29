# Usage
Check out [An example usage here](jvm_backend/src/main/java/com/dujay/generator/Driver.java).

# Design
The API is designed to be fluid so that you don't need to maintain a huge set of objects just to write a simple class file.

The main fluid classes you need to store are ConstantPoolBuilder, MethodPoolBuilder, and MethodInfoBuilder. You can store an instance of these when generating. Currently, you must complete the constant pool before using the method pool, but I am planning to allow you to interweave these to allow for a single pass through your language IR when using this library.

# Links
[The class file format](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html)


[Bytecode reference](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html)

# Optimization
Optimization is left up to the compiler writer for now.
