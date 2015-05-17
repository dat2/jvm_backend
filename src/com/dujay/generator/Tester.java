package com.dujay.generator;

import java.io.IOException;

import com.dujay.generator.model.AccessFlag;
import com.dujay.generator.model.ClassFile;
import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.model.methods.Method;
import com.dujay.generator.model.methods.MethodPool;
import com.dujay.generator.writers.ByteCodeWriter;

public class Tester {
  
  private static ConstantPool createSimpleConstantPool() throws Exception {
    ConstantPool pool = new ConstantPool();

    // classes
    pool.addClass("blah");
    pool.addClass("java/lang/Object");
    pool.addClass("java/lang/System");
    pool.addClass("java/io/PrintStream");
    
    // methods 
    pool.addMethod("<init>","()V");
    pool.addMethod("main", "([Ljava/lang/String;)V");
    pool.addMethod("println", "(Ljava/lang/String;)V");
    
    // variables
    pool.addVariable("out", "Ljava/io/PrintStream;");
    
    // name / types
    pool.addNameAndType("<init>", "()V");
    pool.addNameAndType("out", "Ljava/io/PrintStream;");
    pool.addNameAndType("println", "(Ljava/lang/String;)V");
    
    // method refs
    pool.addMethodRef("java/lang/Object", "<init>", "()V");
    pool.addMethodRef("java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    
    // field refs
    pool.addFieldRef("java/lang/System", "out", "Ljava/io/PrintStream;");
    
    // constant values
    pool.addString("Hello World");

    // attribute names
    pool.addUtf8("Code");
    pool.addUtf8("LineNumberTable");
//    pool.addUtf8("LocalVariableTable");

    pool.addSource("blah.jvm");
    
    return pool;
  }

  private static MethodPool createSimpleMethodPool(ConstantPool constants) throws IOException {
    MethodPool pool = new MethodPool();
    
    // Hello() 
    Method constructor = new Method("<init>", 1, 1, AccessFlag.ACC_PUBLIC);
    ByteCodeWriter w = constructor.getByteCodeWriter();
    
    w.aload_0();
    w.invokespecial(constants.getMemberRefIndex("java/lang/Object", "<init>"));
    w.vreturn();
    
    pool.addMethod(constructor);
    
    // main(String[] args)
    Method main = new Method("main", 2, 1, AccessFlag.ACC_PUBLIC, AccessFlag.ACC_STATIC);
    w = main.getByteCodeWriter();
    
    w.getstatic(constants.getMemberRefIndex("java/lang/System", "out"));
    w.ldc(constants.getStringIndex("Hello World"));
    w.invokevirtual(constants.getMemberRefIndex("java/io/PrintStream", "println"));
    w.vreturn();
    
    pool.addMethod(main);
    
    pool.prepareStream(constants);
    
    return pool;
  }

  public static void main(String[] args) throws Exception {
    ConstantPool constants = Tester.createSimpleConstantPool();
    MethodPool methods = Tester.createSimpleMethodPool(constants);
    
    ClassFile classFile = new ClassFile("blah.class");

    classFile.writeMagicNumber();
    classFile.writeVersion(ClassFile.JAVA8_MAJOR, ClassFile.JAVA8_MINOR);
    
    classFile.writeConstantPool(constants);
    classFile.writeAccessFlags(AccessFlag.ACC_SUPER, AccessFlag.ACC_PUBLIC);
    
    classFile.writeThisClass(constants.getClassIndex("blah"));
    classFile.writeSuperClass(constants.getClassIndex("java/lang/Object"));
    
    classFile.writeInterfaces();
    classFile.writeFields();
    classFile.writeMethods(methods);
    classFile.writeAttributes(constants);
    
    classFile.saveToFile();
  }

}
