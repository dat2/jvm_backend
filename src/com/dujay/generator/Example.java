package com.dujay.generator;

import java.io.PrintStream;

import com.dujay.generator.constants.ClassInfoR;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.Descriptor;
import com.dujay.generator.constants.MemberRefInfo;
import com.dujay.generator.constants.NameAndTypeInfo;
import com.dujay.generator.constants.StringInfo;
import com.dujay.generator.constants.Utf8Info;

public class Example {
  public static void main(String[] args) throws Exception {

    ConstantPool cpr = new ConstantPool();
    
    // System.out
    ClassInfoR systemr = new ClassInfoR(System.class);
    NameAndTypeInfo nt = new NameAndTypeInfo(new Utf8Info("out"),
        new Utf8Info(Descriptor.fieldDescriptor(PrintStream.class)));
    MemberRefInfo outRef = new MemberRefInfo(MemberRefInfo.MemberRefType.FieldRef, systemr, nt);
    cpr.add(systemr);
    cpr.add(nt);
    cpr.add(outRef);

    // PrintStream.println();
    ClassInfoR p = new ClassInfoR(PrintStream.class);
    NameAndTypeInfo nt2 = new NameAndTypeInfo(new Utf8Info("println"),
        new Utf8Info(Descriptor.methodDescriptor(Void.class, String.class)));
    MemberRefInfo printlnRef = new MemberRefInfo(MemberRefInfo.MemberRefType.MethodRef, p, nt2);
    cpr.add(p);
    cpr.add(nt2);
    cpr.add(printlnRef);
    
    // void main(String[] args)
    NameAndTypeInfo nt3 = new NameAndTypeInfo(new Utf8Info("main"),
        new Utf8Info(Descriptor.methodDescriptor(Void.class, (new String[]{}).getClass())));
    cpr.add(nt3);

    // void <init>()
    NameAndTypeInfo nt4 = new NameAndTypeInfo(new Utf8Info("<init>"),
        new Utf8Info(Descriptor.methodDescriptor(Void.class)) );
    MemberRefInfo initRef = new MemberRefInfo(MemberRefInfo.MemberRefType.MethodRef, cpr.getThisClass(), nt4);
    cpr.add(nt4);
    cpr.add(initRef);
    
    StringInfo helloWorld = new StringInfo("Hello World");
    cpr.add(helloWorld);
    
    Utf8Info code = new Utf8Info("Code");
    Utf8Info lnt = new Utf8Info("LineNumberTable");
    Utf8Info lvt = new Utf8Info("LocalVariableTable");
    cpr.add(code);
    cpr.add(lnt);
    cpr.add(lvt);
    
    cpr.addSource("Hello.jvm");
    
    cpr.setIndices();
  }
}
