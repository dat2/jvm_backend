package com.dujay.generator;

import java.io.IOException;

import com.dujay.generator.codegen.AccessFlag;
import com.dujay.generator.codegen.IClassFileGenerator;
import com.dujay.generator.codegen.SimpleClassFileGenerator;

public class Tester {

	public static void main(String[] args) throws IOException {
		IClassFileGenerator generator = new SimpleClassFileGenerator();
		generator.setFilename("J.class");
		
		//headers
		generator.magicNumber();
		generator.java8version();
		
		//c_count (21 entries)
		generator.u2(22);
		
		//constant_pool[c_count]
		
		// the hello class
		generator.constClass(2);
		generator.constUtf8("Hello");
		
		// the object class
		generator.constClass(4);
		generator.constUtf8("java/lang/Object");
		generator.constUtf8("<init>");
		generator.constUtf8("()V");
		generator.constUtf8("Code");
		generator.constMethodRef(3, 9);
		generator.constNameAndType(5, 6);
		
		generator.constUtf8("LineNumberTable");
		generator.constUtf8("LocalVariableTable");
		
		generator.constUtf8("this");
		
		// public static void main(string[] args);
    generator.constUtf8("LHello;");
		generator.constUtf8("main");
		generator.constUtf8("([Ljava/lang/String;)V");
		generator.constUtf8("args");
    generator.constUtf8("[Ljava/lang/String;");
    
    // int x
    generator.constUtf8("x");
    generator.constUtf8("I");
    
    generator.constUtf8("SourceFile");
    generator.constUtf8("Hello.java");
    
		//access flags
    generator.accessFlags(AccessFlag.ACC_SUPER, AccessFlag.ACC_PUBLIC);
    
    // 0 indexed now
    //this class
    //super class
    generator.thisClass(1);
    generator.superClass(3);
		
		//i_count
		//interfaces[i_count]
    generator.u2(0);
		
		//f_count
    //fields[f_count]
    generator.u2(0);
		
		//m_count
		//methods[m_count]
    generator.u2(2); //main, and constructor
    
    // constructor
    generator.accessFlags(AccessFlag.ACC_PUBLIC);
    generator.u2(5); // <init>
    generator.u2(6); // V()
    generator.u2(1); // 1 attr, code
    
    // Code attribute
    generator.u2(7); // Code
    generator.u4(47); // length of this attribute
    generator.u2(1); // max_stack
    generator.u2(1); // max_locals
    
    generator.u4(5); // code_length
    
    // code
    generator.aload_0();
    generator.invokespecial(new byte[]{ 0, 8 }); // the 9th object in the constant pool
    generator.vreturn();
    
    // exceptions
    generator.u2(0);
    
    // code attribute, attributes
//    generator.u2(0);
    generator.u2(2);
    
    // line number table
    generator.u2(10); // LineNumberTable
    generator.u4(6);
    generator.u2(1);
    generator.u2(0);
    generator.u2(3);
		
    // local variable table
    generator.u2(11); // LocalVariableTable
    generator.u4(12);
    generator.u2(1);
    generator.u2(0);
    generator.u2(5);
    generator.u2(12);
    generator.u2(13);
    generator.u2(0);
    
    // MAIN
    generator.accessFlags(AccessFlag.ACC_PUBLIC, AccessFlag.ACC_STATIC);
    generator.u2(14); // main
    generator.u2(15); //([Ljava/lang/String;)V
    generator.u2(1); // 1 attribute, code
    
    // MAIN code attribute
    generator.u2(7); // Code
    generator.u4(59); // attribute length
    generator.u2(1); // max_stack
    generator.u2(2); // max_locals (x, String[] args)
    
    // code
    generator.u4(3);
    generator.iconst_0();
    generator.istore_1();
    generator.vreturn();
    
    generator.u2(0);
    
    // codes
//    generator.u2(10); // LineNumberTable
//    generator.u4(6);
//    generator.u2(1);
//    generator.u2(0);
//    generator.u2(3);
    
		//a_count
		//attributes[a_count]
    generator.u2(0);
		
		// write file out
		generator.writeToFile();
	}

}
