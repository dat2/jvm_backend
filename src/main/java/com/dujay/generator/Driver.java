package com.dujay.generator;

import java.io.PrintStream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.dujay.generator.attributes.CodeAttribute;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.Descriptor;
import com.dujay.generator.constants.structures.ClassInfo;
import com.dujay.generator.constants.structures.MemberRefInfo;
import com.dujay.generator.constants.structures.StringInfo;
import com.dujay.generator.enums.AccessFlag;
import com.dujay.generator.file.ClassFile;
import com.dujay.generator.methods.MethodInfo;
import com.dujay.generator.methods.MethodPool;

public class Driver {
  
  private final static Logger logger = (Logger) LoggerFactory.getLogger("driver");

  public static void generateInitMethod(ConstantPool cpr, MethodPool mp) {
    MethodInfo init = new MethodInfo(cpr.getUtf8("<init>").get(), cpr
        .getDescriptor("<init>").get(), AccessFlag.PUBLIC, AccessFlag.SYNTHETIC);
    init.add(new CodeAttribute(cpr, 1, 1));

    // referenced constants
    MemberRefInfo member = cpr.getMemberRef(new ClassInfo(Object.class),
        "<init>").get();

    logger.debug("<init> code");
    CodeAttribute code = init.getCode().get();
    code.aload_0();
    code.invokespecial(member.getIndex());
    code.vreturn();
    logger.debug("end <init> code");

    mp.add(init);
  }

  public static void generateMainMethod(ConstantPool cpr, MethodPool mp) {
    // Attributes
    MethodInfo main = new MethodInfo(cpr.getUtf8("main").get(), cpr
        .getDescriptor("main").get(), AccessFlag.PUBLIC, AccessFlag.STATIC,
        AccessFlag.SYNTHETIC);
    main.add(new CodeAttribute(cpr, 2, 1));

    // constants needed for code
    MemberRefInfo sysout = cpr.getMemberRef(new ClassInfo(System.class), "out")
        .get();
    StringInfo hw = cpr.getString("Hello World").get();
    MemberRefInfo println = cpr.getMemberRef(new ClassInfo(PrintStream.class),
        "println").get();

    logger.debug("main code");
    CodeAttribute code = main.getCode().get();
    code.getstatic(sysout.getIndex());
    code.ldc(hw.getIndex());
    code.invokevirtual(println.getIndex());
    code.vreturn();
    logger.debug("end main code");

    mp.add(main);
  }

  public static void main(String[] args) throws Exception {
    Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.DEBUG);
    
    ClassFile cls = new ClassFile("Hello", AccessFlag.PUBLIC, AccessFlag.SUPER, AccessFlag.SYNTHETIC);

    // Constant Pool
    ConstantPool cpr = cls.makeConstantPoolBuilder()
      // this and super class
      .thisClass("Hello")
      .superClass(Object.class)
      // attribute names
      .utf8("Code")
      // debug info
      .utf8("LineNumberTable")
      .utf8("LocalVariableTable")
      .source("Hello.jvm")
      // init method constants
      .nameAndType("initNT", "<init>", Descriptor.methodDescriptor(Void.class))
      .method("this", "initNT")
      .method("super", "initNT")
      // main method constants
      .cClass("System", System.class)
      .nameAndType("outNT", "out", Descriptor.fieldDescriptor(PrintStream.class))
      .field("System", "outNT")
      .cClass("PrintStream", PrintStream.class)
      .nameAndType("printlnNT", "println", Descriptor.methodDescriptor(Void.class, String.class))
      .method("PrintStream", "printlnNT")
      .literal("Hello World")
      .nameAndType("mainNT", "main", Descriptor.methodDescriptor(Void.class, (new String[] {}).getClass()))
      .method("this", "mainNT")
      // finally generate indices
      .index()
      // finally return the constant pool
      .build();
    
    // Method Pool
    MethodPool mp = cls.getMethodPool();

    generateInitMethod(cpr, mp);
    generateMainMethod(cpr, mp);

    cls.save();
  }
}
