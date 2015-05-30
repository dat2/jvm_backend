package com.dujay.jvm;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.dujay.jvm.attributes.builders.CodeAttributeBuilder;
import com.dujay.jvm.constants.ConstantPoolBuilder;
import com.dujay.jvm.constants.Descriptor;
import com.dujay.jvm.constants.enums.AccessFlag;
import com.dujay.jvm.file.ClassFile;
import com.dujay.jvm.methods.builders.MethodPoolBuilder;

public class Driver {
  
  private ClassFile cls;
  
  private ConstantPoolBuilder cpb;
  private MethodPoolBuilder mpb;
  
  private List<CodeAttributeBuilder> code;
  
  private Driver() {
    this.code = new ArrayList<CodeAttributeBuilder>();
    this.cls = new ClassFile("Hello", AccessFlag.PUBLIC, AccessFlag.SUPER, AccessFlag.SYNTHETIC);
    this.cpb = cls.makeConstantPoolBuilder();
    this.mpb = cls.makeMethodPoolBuilder();
  }
  
  private void generateConstants() {
    // Constant Pool
    cpb
      // this and super class
      .thisClass("Hello")
      .superClass(Object.class)
      
      // attributes
      .attribute("Code")
      .attribute("LineNumberTable")
      .attribute("LocalVariableTable")
      .source("Hello.jvm");
  }
  
  private void generateInit() {
    // generate constants
    cpb
      .nameAndType("initNT", "<init>", Descriptor.methodDescriptor(Void.class))
      .method("Hello.<init>", "this", "initNT")
      .method("Object.<init>", "super", "initNT");
    
    // generate method
    CodeAttributeBuilder cab = mpb.beginMethod()
      .signature("initNT", 1, 1, AccessFlag.PUBLIC, AccessFlag.SYNTHETIC)
      
      .beginCode()
        .aload_0()
        .invokespecial("Object.<init>")
        .vreturn();
    
    // add it for patching later
    this.code.add(cab);
  }
  
  private void generateMain() {   
    // main method constants
    cpb
      .literal("hw", "Hello World")
      .literal("hw2", "Hello World, Again!")
      .literal("hw3", "Hello World, Again2!")
      
      .clazz("System", System.class)
      .clazz("PrintStream", PrintStream.class)
      
      .nameAndType("outNT", "out", Descriptor.fieldDescriptor(PrintStream.class))
      .nameAndType("printlnNT", "println", Descriptor.methodDescriptor(Void.class, String.class))
      .nameAndType("mainNT", "main", Descriptor.methodDescriptor(Void.class, (new String[] {}).getClass()))
      
      .field("System.out", "System", "outNT")
      
      .method("PrintStream.println", "PrintStream", "printlnNT")
      .method("Hello.main", "this", "mainNT");
    
    // main method code
    // Hello.main(String[] args)
    CodeAttributeBuilder cab = mpb.beginMethod()
      .signature("mainNT", 2, 1, AccessFlag.PUBLIC, AccessFlag.STATIC, AccessFlag.SYNTHETIC)
      
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
        .vreturn();
    
    this.code.add(cab);
  }
  
  private void buildConstantPool() {
    cpb
      // finally generate indices
      .index()
      // finally return the constant pool
      .build();
  }
  
  private void buildMethodPool() {
    for(CodeAttributeBuilder b : this.code) {
      b
        // patches the byte list with all the indices of the generated constants
        .patch()
        // lets you end the method
        .endCode()
        // adds it to the method pool finally
        .endMethod();
    }
    mpb.build();
  }
  
  public void generate() throws IOException {
    this.generateConstants();
    this.generateInit();
    this.generateMain();
    
    this.buildConstantPool();
    this.buildMethodPool();
    
    cls.save();
  }

  public static void main(String[] args) throws Exception {
    Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.DEBUG);

    Driver d = new Driver();
    d.generate();
  }
}
