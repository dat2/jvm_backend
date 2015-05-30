package com.dujay.jvm;

import java.io.PrintStream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.dujay.jvm.constants.Descriptor;
import com.dujay.jvm.constants.enums.AccessFlag;
import com.dujay.jvm.file.ClassFile;

public class Driver {

  public static void main(String[] args) throws Exception {
    Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.DEBUG);
    
    ClassFile cls = new ClassFile("Hello", AccessFlag.PUBLIC, AccessFlag.SUPER, AccessFlag.SYNTHETIC);

    // Constant Pool
    cls.makeConstantPoolBuilder()
      // this and super class
      .thisClass("Hello")
      .superClass(Object.class)
      
      // attributes
      .attribute("Code")
      .attribute("LineNumberTable")
      .attribute("LocalVariableTable")
      .source("Hello.jvm")
      
      // init method constants
      .nameAndType("initNT", "<init>", Descriptor.methodDescriptor(Void.class))
      .method("Hello.<init>", "this", "initNT")
      .method("Object.<init>", "super", "initNT")
      
      // main method constants
      .literal("hw", "Hello World")
      .literal("hw2", "Hello World, Again!")
      .literal("hw3", "Hello World, Again2!")
      .cClass("System", System.class)
      .nameAndType("outNT", "out", Descriptor.fieldDescriptor(PrintStream.class))
      .field("System.out", "System", "outNT")
      .cClass("PrintStream", PrintStream.class)
      .nameAndType("printlnNT", "println", Descriptor.methodDescriptor(Void.class, String.class))
      .method("PrintStream.println", "PrintStream", "printlnNT")
      .nameAndType("mainNT", "main", Descriptor.methodDescriptor(Void.class, (new String[] {}).getClass()))
      .method("Hello.main", "this", "mainNT")
      
      // finally generate indices
      .index()
      // finally return the constant pool
      .build();
    
    // Method Pool
    cls.makeMethodPoolBuilder()
    
      // Hello.<init>()
      .beginMethod()
        .define("initNT", 1, 1, AccessFlag.PUBLIC, AccessFlag.SYNTHETIC)
        
        .beginCode()
          .aload_0()
          .invokespecial("Object.<init>")
          .vreturn()
          .patch()
        .endCode()
        
      .endMethod()
      
      // Hello.main(String[] args)
      .beginMethod()
        .define("mainNT", 2, 1, AccessFlag.PUBLIC, AccessFlag.STATIC, AccessFlag.SYNTHETIC)
        
        .beginCode()
          // System.out.println("Hello World");
          .getstatic("System.out")
          .ldc("hw")
          .invokevirtual("PrintStream.println")
          // System.out.println("Hello World, Again!");
          .getstatic("System.out")
          .ldc("hw2")
          .invokevirtual("PrintStream.println")
          // System.out.println("Hello World, Again 2!");
          .getstatic("System.out")
          .ldc("hw3")
          .invokevirtual("PrintStream.println")
          .vreturn()
          .patch()
        .endCode()
        
      .endMethod()
      
      .build();

    cls.save();
  }
}
