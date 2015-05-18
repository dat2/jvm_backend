package com.dujay.generator;

import java.io.PrintStream;

import com.dujay.generator.model.AccessFlag;
import com.dujay.generator.model.ClassFileWriter;
import com.dujay.generator.model.constants.ClassDescriptor;
import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.model.constants.MemberDescriptor;
import com.dujay.generator.model.constants.MethodDescriptor;
import com.dujay.generator.model.constants.VariableDescriptor;
import com.dujay.generator.model.methods.Method;
import com.dujay.generator.model.methods.MethodPool;
import com.dujay.generator.writers.ByteCodeWriter;

public class Example {
  public static void main(String[] args) throws Exception {
    ClassFileWriter writer = new ClassFileWriter(new ClassDescriptor(null, "Hello"));
    
    ConstantPool constantPool = writer.getConstantPool();
    
    // add classes
    ClassDescriptor blah = writer.getThisClass();
    ClassDescriptor object = writer.getSuperClass();
    ClassDescriptor system = new ClassDescriptor(System.class);
    ClassDescriptor printStream = new ClassDescriptor(PrintStream.class);
    
    constantPool.addClass(system);
    constantPool.addClass(printStream);
    
    // methods 
    MethodDescriptor init = new MethodDescriptor(blah, "<init>", Void.class);
    MethodDescriptor main = new MethodDescriptor(blah, "main", Void.class, (new String[] {}).getClass());
    MethodDescriptor println = new MethodDescriptor(blah, "println", Void.class, String.class);
    
    constantPool.addMethod(init);
    constantPool.addMethod(main);
    constantPool.addMethod(println);
    
    // variables
    VariableDescriptor out = new VariableDescriptor("out", printStream);
    
    constantPool.addVariable(out);
    
    // name / types
    constantPool.addNameAndType(init);
    constantPool.addNameAndType(out);
    constantPool.addNameAndType(println);
    
    // method refs
    MemberDescriptor initRef = new MemberDescriptor(object, init);
    MemberDescriptor printlnRef = new MemberDescriptor(printStream, println);
    
    constantPool.addMethodRef(initRef);
    constantPool.addMethodRef(printlnRef);
    
    // field refs
    MemberDescriptor outRef = new MemberDescriptor(system, out);
    
    constantPool.addFieldRef(outRef);
    
    // constant values
    constantPool.addString("Hello World");

    // attribute names
    constantPool.addUtf8("Code");
    constantPool.addUtf8("LineNumberTable");
    constantPool.addUtf8("LocalVariableTable");

    constantPool.addSource("Hello.jvm");
    
    MethodPool methodPool = writer.getMethodPool();
    
    // Hello() 
    Method constructorMethod = new Method("<init>", 1, 1, AccessFlag.ACC_PUBLIC);
    ByteCodeWriter w = constructorMethod.getByteCodeWriter();
    
    w.aload_0();
    w.invokespecial(constantPool.getMemberRefIndex(initRef));
    w.vreturn();
    
    methodPool.addMethod(constructorMethod);
    
    // main(String[] args)
    Method mainMethod = new Method("main", 2, 1, AccessFlag.ACC_PUBLIC, AccessFlag.ACC_STATIC);
    w = mainMethod.getByteCodeWriter();
    
    w.getstatic(constantPool.getMemberRefIndex(outRef));
    w.ldc(constantPool.getStringIndex("Hello World"));
    w.invokevirtual(constantPool.getMemberRefIndex(printlnRef));
    w.vreturn();
    
    methodPool.addMethod(mainMethod);
    methodPool.prepareStream();
    
    writer.writeClassFile();
  }
}
