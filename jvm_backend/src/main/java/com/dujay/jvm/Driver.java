package com.dujay.jvm;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.dujay.jvm.attributes.builders.CodeAttributeBuilder;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.ConstantPoolBuilder;
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
  
  private void generateInit() throws Exception {
    Constructor<Object> c = Object.class.getConstructor();
    
    // generate constants
    cpb
      .constructor("super", c)
      .constructor("this");
    
    // generate method
    CodeAttributeBuilder cab = mpb.beginMethod()
      .signature(ConstantPool.uniqueConstructorNTName(), 1, 1, AccessFlag.PUBLIC, AccessFlag.SYNTHETIC)
      
      .beginCode()
        .aload_0()
        .invokespecial(c)
        .vreturn();
    
    // add it for patching later
    this.code.add(cab);
  }
  
  private void generateMain() throws Exception {
    Method printlnS = PrintStream.class.getMethod("println", String.class);
    Method printlnI = PrintStream.class.getMethod("println", int.class);
    Method printlnF = PrintStream.class.getMethod("println", float.class);
    Method printlnL = PrintStream.class.getMethod("println", long.class);
    Method printlnD = PrintStream.class.getMethod("println", double.class);
    
    Field out = System.class.getField("out");
    
    // main method constants
    cpb
      .literal("s", "Hello World")
      .literal("i", 27)
      .literal("f", 3.14f)
      .literal("l", 12345678910L)
      .literal("d", 4.0)
      
      .clazz(System.class)
      .clazz(PrintStream.class)
      
      .field(out)
      
      .method(printlnS)
      .method(printlnI)
      .method(printlnF)
      .method(printlnL)
      .method(printlnD)
      
      // TODO make facade objects for both reflection and constructed classes
      .nameAndType("main.NT", "main", Void.class, (new String[] {}).getClass())
      .method("Hello.main", "this", "main.NT");
    
    // main method code
    // Hello.main(String[] args)
    CodeAttributeBuilder cab = mpb.beginMethod()
      .signature("main.NT", 3, 1, AccessFlag.PUBLIC, AccessFlag.STATIC, AccessFlag.SYNTHETIC)
      
      .beginCode()
        // System.out.println("Hello World");
        .getstatic(out)
        .ldc("s")
        .invokevirtual(printlnS)
        // System.out.println(integer);
        .getstatic(out)
        .ldc("i")
        .invokevirtual(printlnI)
        // System.out.println(float);
        .getstatic(out)
        .ldc("f")
        .invokevirtual(printlnF)
        // System.out.println(long);
        .getstatic(out)
        .ldc2_w("l")
        .invokevirtual(printlnL)
        // System.out.println(double);
        .getstatic(out)
        .ldc2_w("d")
        .invokevirtual(printlnD)
        // return;
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
        // patches addresses and adds the code attribute to the method info
        .build()
        // adds it to the method pool finally
        .endMethod();
    }
    mpb.build();
  }
  
  public void generate() throws Exception {
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
