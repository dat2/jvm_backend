# Usage
Check out Example.java under the main src directory. In the current state, the project requires an intimate understanding of class files and this project just provides a helper class to make a class file.

If you'd like to understand some of the design, here's the official [reference from Oracle](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html). I try to use some terminology from here, but with some divergence. I am currently trying to restructure to map closer to this, but also have a layer of abstraction on top to allow for a generalized backend, as explained below.

# Design Goals
I plan on making this project a bit easier for users so that this can be a generalized backend for the JVM. Then users would just need to make a mapping of their language structures and semantics to Java classes, but also need to learn Java's byte code ([reference here](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html)).

# Optimization
Optimization is not a design goal right now, but it is on the road map.
