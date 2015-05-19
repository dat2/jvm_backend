package com.dujay.generator.model;

import java.io.IOException;

import com.dujay.generator.model.constants.ClassInfo;
import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.model.methods.MethodPool;

public class ClassFileWriter {
  
  private ClassFile classFile;
  
  private ClassInfo thisClass;
  private ClassInfo superClass;
  
  private ConstantPool constantsPool;
  private MethodPool methodsPool;
  
  public ClassFileWriter(ClassInfo thisClass, ClassInfo superClass) {
    this.classFile = new ClassFile(thisClass.getName() + ".class");
    this.thisClass = thisClass;
    this.superClass = superClass;
    
    this.constantsPool = new ConstantPool();
    
    this.constantsPool.addClass(thisClass);
    this.constantsPool.addClass(superClass);
    
    this.methodsPool = new MethodPool(constantsPool);
  }
  
  public ClassFileWriter(ClassInfo thisClass) {
    this(thisClass, new ClassInfo(Object.class));
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

  public ClassInfo getThisClass() {
    return thisClass;
  }

  public ClassInfo getSuperClass() {
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
