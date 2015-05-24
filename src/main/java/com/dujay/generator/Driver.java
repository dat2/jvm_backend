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
import com.dujay.generator.constants.structures.NameAndTypeInfo;
import com.dujay.generator.constants.structures.StringInfo;
import com.dujay.generator.constants.structures.Utf8Info;
import com.dujay.generator.enums.AccessFlag;
import com.dujay.generator.file.ClassFile;
import com.dujay.generator.methods.MethodInfo;
import com.dujay.generator.methods.MethodPool;

public class Driver {
  
  private final static Logger logger = (Logger) LoggerFactory.getLogger("driver");

  private static void generateClasses(ConstantPool cpr) {
    cpr.setThisClass(new ClassInfo("Hello"));
    cpr.setSuperClass(new ClassInfo(Object.class));
  }

  private static void generateExtraInfo(ConstantPool cpr, String source) {
    cpr.add(new Utf8Info("Code"));
    cpr.add(new Utf8Info("LineNumberTable"));
    cpr.add(new Utf8Info("LocalVariableTable"));

    cpr.addSource(source);
  }

  public static void generateInitConstants(ConstantPool cpr) {
    // void <init>()
    Utf8Info name = new Utf8Info("<init>");
    Utf8Info descriptor = cpr.addDescriptor("<init>",
        Descriptor.methodDescriptor(Void.class));

    NameAndTypeInfo nt = new NameAndTypeInfo(name, descriptor);
    MemberRefInfo initRef = new MemberRefInfo(
        MemberRefInfo.MemberRefType.MethodRef, cpr.getThisClass(), nt);

    cpr.add(nt);
    cpr.add(initRef);

    // Object.<init>
    MemberRefInfo objectInitRef = new MemberRefInfo(
        MemberRefInfo.MemberRefType.MethodRef, cpr.getSuperClass(), nt);
    cpr.add(objectInitRef);
  }

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

  public static void generateMainConstants(ConstantPool cpr) {
    // REQUIRED CONSTANTS

    // System.out
    ClassInfo systemr = new ClassInfo(System.class);
    NameAndTypeInfo nt = new NameAndTypeInfo(new Utf8Info("out"),
        cpr.addDescriptor("out", Descriptor.fieldDescriptor(PrintStream.class)));
    MemberRefInfo outRef = new MemberRefInfo(
        MemberRefInfo.MemberRefType.FieldRef, systemr, nt);
    cpr.add(systemr);
    cpr.add(nt);
    cpr.add(outRef);

    // PrintStream.println();
    ClassInfo p = new ClassInfo(PrintStream.class);
    NameAndTypeInfo nt2 = new NameAndTypeInfo(new Utf8Info("println"),
        cpr.addDescriptor("println",
            Descriptor.methodDescriptor(Void.class, String.class)));
    MemberRefInfo printlnRef = new MemberRefInfo(
        MemberRefInfo.MemberRefType.MethodRef, p, nt2);
    cpr.add(p);
    cpr.add(nt2);
    cpr.add(printlnRef);

    // hello world
    StringInfo helloWorld = new StringInfo("Hello World");
    cpr.add(helloWorld);

    // void main(String[] args)
    Utf8Info name = new Utf8Info("main");
    NameAndTypeInfo nt3 = new NameAndTypeInfo(name, cpr.addDescriptor("main",
        Descriptor.methodDescriptor(Void.class, (new String[] {}).getClass())));
    cpr.add(nt3);
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
    ConstantPool cpr = cls.getConstantPool();

    generateClasses(cpr);
    generateExtraInfo(cpr, "Hello.jvm");

    generateInitConstants(cpr);
    generateMainConstants(cpr);
    
    cpr.setIndices();
    
    // Method Pool
    MethodPool mp = cls.getMethodPool();

    generateInitMethod(cpr, mp);
    generateMainMethod(cpr, mp);

    cls.save();
  }
}
