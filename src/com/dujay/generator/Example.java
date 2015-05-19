package com.dujay.generator;

import java.io.PrintStream;

import com.dujay.generator.model.AccessFlag;
import com.dujay.generator.model.ClassFileWriter;
import com.dujay.generator.model.constants.ClassInfo;
import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.model.constants.MemberDescriptor;
import com.dujay.generator.model.constants.MethodDescriptor;
import com.dujay.generator.model.constants.VariableDescriptor;
import com.dujay.generator.model.methods.Method;
import com.dujay.generator.model.methods.MethodPool;
import com.dujay.generator.redesign.constants.ClassInfoR;
import com.dujay.generator.redesign.constants.ConstantPoolVisitor;
import com.dujay.generator.redesign.constants.Descriptor;
import com.dujay.generator.redesign.constants.MemberRefInfo;
import com.dujay.generator.redesign.constants.NameAndTypeInfo;
import com.dujay.generator.redesign.constants.Utf8Info;
import com.dujay.generator.writers.ByteCodeWriter;

public class Example {
  public static void main(String[] args) throws Exception {
    
    ConstantPoolVisitor v = new ConstantPoolVisitor();
    
    // System.out
    ClassInfoR s = new ClassInfoR(System.class);
    NameAndTypeInfo nt = new NameAndTypeInfo(new Utf8Info("out"),
        new Utf8Info(Descriptor.fieldDescriptor(PrintStream.class)));
    MemberRefInfo mr = new MemberRefInfo(MemberRefInfo.MemberRefType.FieldRef, s, nt);
    mr.accept(v);

    // PrintStream.println();
    ClassInfoR p = new ClassInfoR(PrintStream.class);
    NameAndTypeInfo nt2 = new NameAndTypeInfo(new Utf8Info("println"),
        new Utf8Info(Descriptor.methodDescriptor(Void.class, String.class)));
    MemberRefInfo mr2 = new MemberRefInfo(MemberRefInfo.MemberRefType.MethodRef, p, nt2);
    mr2.accept(v);

    // void main(String[] args)
    NameAndTypeInfo nt3 = new NameAndTypeInfo(new Utf8Info("main"),
        new Utf8Info(Descriptor.methodDescriptor(Void.class, (new String[]{}).getClass())));
    nt3.accept(v);

    // void <init>()
    NameAndTypeInfo nt4 = new NameAndTypeInfo(new Utf8Info("<init>"),
        new Utf8Info(Descriptor.methodDescriptor(Void.class)) );
    nt4.accept(v);
    
    // blah, Object
    ClassInfoR blahr = new ClassInfoR("blah");
    ClassInfoR objectr = new ClassInfoR(Object.class);
    blahr.accept(v);
    objectr.accept(v);
    
    ClassFileWriter writer = new ClassFileWriter(new ClassInfo(null, "Hello"));
    
    ConstantPool constantPool = writer.getConstantPool();
    
    // add classes
    ClassInfo blah = writer.getThisClass();
    ClassInfo object = writer.getSuperClass();
    ClassInfo system = new ClassInfo(System.class);
    ClassInfo printStream = new ClassInfo(PrintStream.class);
    
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
