package com.dujay.generator.model;

import java.io.IOException;
import java.io.PrintStream;

import com.dujay.generator.model.constants.ClassDescriptor;
import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.model.constants.Descriptor;
import com.dujay.generator.model.constants.MemberDescriptor;
import com.dujay.generator.model.constants.MethodDescriptor;
import com.dujay.generator.model.constants.VariableDescriptor;
import com.dujay.generator.model.methods.Method;
import com.dujay.generator.model.methods.MethodPool;
import com.dujay.generator.writers.ByteCodeWriter;

public class ClassFileWriter {
  
  private ClassFile classFile;
  
  private ClassDescriptor thisClass;
  private ClassDescriptor superClass;
  
  private ConstantPool constantsPool;
  private MethodPool methodsPool;
  
  public ClassFileWriter(ClassDescriptor thisClass, ClassDescriptor superClass) {
    this.classFile = new ClassFile(thisClass.getName() + ".class");
    this.thisClass = thisClass;
    this.superClass = superClass;
    
    this.constantsPool = new ConstantPool();
    
    this.constantsPool.addClass(thisClass);
    this.constantsPool.addClass(superClass);
    
    this.methodsPool = new MethodPool(constantsPool);
  }
  
  public ClassFileWriter(ClassDescriptor thisClass) {
    this(thisClass, new ClassDescriptor(Object.class));
  }
  
  public ConstantPool getConstantPool() {
    return constantsPool;
  }
  
  public MethodPool getMethodPool() {
    return methodsPool;
  }

  public ClassFile getClassFile() {
    return classFile;
  }

  public ClassDescriptor getThisClass() {
    return thisClass;
  }

  public ClassDescriptor getSuperClass() {
    return superClass;
  }
  
  public void writeClassFile() throws IOException {
    classFile.writeMagicNumber();
    classFile.writeVersion(ClassFile.JAVA8_MAJOR, ClassFile.JAVA8_MINOR);
    
    classFile.writeConstantPool(constantsPool);
    classFile.writeAccessFlags(AccessFlag.ACC_SUPER, AccessFlag.ACC_PUBLIC);
    
    classFile.writeThisClass(constantsPool.getClassIndex(thisClass));
    classFile.writeSuperClass(constantsPool.getClassIndex(superClass));
    
    classFile.writeInterfaces();
    classFile.writeFields();
    classFile.writeMethods(methodsPool);
    classFile.writeAttributes(constantsPool);
    
    classFile.saveToFile();
  }
}
