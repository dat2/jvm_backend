package com.dujay.generator.model.constants;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.dujay.generator.writers.ByteStreamWriter;
import com.dujay.generator.writers.WriteableByteStream;

public class ConstantPool implements WriteableByteStream {
  
  private enum ConstantType {
    
    Class(7),
    Fieldref(9),
    Methodref(10),
    InterfaceMethodref(11),
    String(8),
    Integer(3),
    Float(4),
    Long(5),
    Double(6),
    NameAndType(12),
    Utf8(1),
    MethodHandle(15),
    MethodType(16),
    InvokeDynamic(18);

    private int tag;
    ConstantType(int flag) {
      this.tag = flag;
    }
    
    public int tag() {
      return tag;
    }
  }

  private ByteArrayOutputStream stream;
  private ByteStreamWriter writer;
  
  private Map<String, Integer> classIndices;
  private Map<String, Integer> methodIndices;
  private Map<String, Integer> memberRefIndices;
  private Map<String, Integer> utf8Indices;
  private Map<String, Integer> variableIndices;
  private Map<NameAndType, Integer> nameAndTypeIndices;
  private Map<String, Integer> stringIndices;
  
  private int currentIndex;

  public ConstantPool() {
    stream = new ByteArrayOutputStream();
    writer = new ByteStreamWriter(stream);
    
    currentIndex = 0;
    classIndices = new HashMap<String, Integer>();
    methodIndices = new HashMap<String, Integer>();
    memberRefIndices = new HashMap<String, Integer>();
    utf8Indices = new HashMap<String, Integer>();
    variableIndices = new HashMap<String, Integer>();
    nameAndTypeIndices = new HashMap<NameAndType, Integer>();
    stringIndices = new HashMap<String, Integer>();
  }
  
  public int nextIndex() {
    return (currentIndex + 1) + 1;
  }
  
  private <T> int getIndex(Map<T, Integer> map, Object object) {
    if(map.containsKey(object)) {
      return map.get(object);
    }
    return -1;
  }
  
  public int getClassIndex(String className) {
    return getIndex(classIndices, className);
  }
  
  public int getMethodIndex(String methodName) {
    return getIndex(methodIndices, methodName);
  }

  public int getMemberRefIndex(String className, String memberName) {
    return getIndex(memberRefIndices, className + ":" + memberName);
  }
  
  public int getUtf8Index(String utf) {
    return getIndex(utf8Indices, utf);
  }

  public int getVariableIndex(String name) {
    return getIndex(variableIndices, name);
  }
  
  public int getNameAndTypeIndex(String name, String type) {
    return getIndex(nameAndTypeIndices, new NameAndType(name, type));  
  }
  
  public int getStringIndex(String string) {
    return getIndex(stringIndices, string);
  }

  public int getSourceAttributeIndex() {
    return getUtf8Index("SourceFile");
  }

  public int getSourceIndex() {
    return getSourceAttributeIndex() + 1;
  }

  public void addClass(String className) {
    writer.u1(ConstantType.Class.tag());
    writer.u2(++currentIndex + 1); // needs to be 1 indexed
    classIndices.put(className, currentIndex);
    
    this.addUtf8(className);
  }
  
  public void addMethod(String methodName, String methodDescriptor) {
    this.addUtf8(methodName);
    methodIndices.put(methodName, currentIndex);
    
    this.addUtf8(methodDescriptor);
  }

  public void addString(String s) {
    writer.u1(ConstantType.String.tag());
    writer.u2(++currentIndex + 1);
    
    stringIndices.put(s, currentIndex);
    
    this.addUtf8(s);
  }

  private void addMemberRef(int memberType, String className, String name, String type) throws Exception {
    int classIdx = this.getClassIndex(className);
    if(classIdx == -1) {
      throw new Exception("Class " + className + " not found!");
    }
    
    int nameTypeIdx = this.getNameAndTypeIndex(name, type);
    if(nameTypeIdx == -1) {
      throw new Exception("Name " + name + " not found!");
    }
    
    writer.u1(memberType);
    writer.u2(classIdx);
    writer.u2(nameTypeIdx);
    currentIndex++;
    
    memberRefIndices.put(className + ":" + name, currentIndex);
  }

  public void addFieldRef(String className, String name, String type) throws Exception {
    addMemberRef(ConstantType.Fieldref.tag(), className, name, type);
  }

  public void addMethodRef(String className, String name, String type) throws Exception {
    addMemberRef(ConstantType.Methodref.tag(), className, name, type);
  }

  public void addInterfaceMethodRef(String className, String name, String type) throws Exception {
    addMemberRef(ConstantType.InterfaceMethodref.tag(), className, name, type);
  }

  private void addNumber(int numberType, int bytes) {
    writer.u1(numberType);
    writer.u4(bytes);
    currentIndex++;
  }

  public void addInt(int val) {
    addNumber(ConstantType.Integer.tag(), val);
  }

  public void addFloat(float val) {
    addNumber(ConstantType.Integer.tag(), Float.floatToIntBits(val));
  }

  private void addLongNumber(int numberType, long bytes) {
    writer.u1(numberType);
    writer.u4((int) (bytes >> 32));
    writer.u4((int) (bytes));
    currentIndex++;
  }

  public void addLong(long val) {
    addLongNumber(ConstantType.Long.tag(), val);
  }

  public void addDouble(double val) {
    addLongNumber(ConstantType.Double.tag(), Double.doubleToLongBits(val));
  }

  public void addNameAndType(String name, String type) throws Exception {
    int nameIdx = this.getUtf8Index(name);
    if(nameIdx == -1) {
      throw new Exception("Name " + name + " not found!");
    }
    
    int typeIdx = this.getUtf8Index(type);
    if(typeIdx == -1) {
      throw new Exception("Type " + type + " not found!");
    }
    
    writer.u1(ConstantType.NameAndType.tag());
    writer.u2(nameIdx);
    writer.u2(typeIdx);
    currentIndex++;
    
    this.nameAndTypeIndices.put(new NameAndType(name, type), currentIndex);
  }
  
  public void addVariable(String variableName, String type) {
    this.addUtf8(variableName);
    this.variableIndices.put(variableName, currentIndex);
    
    this.addUtf8(type);
  }

  public void addUtf8(String utf) {
    writer.u1(ConstantType.Utf8.tag());
    writer.u2(utf.length());
    for (byte b : utf.getBytes()) {
      writer.u1(b);
    }
    currentIndex++;
    utf8Indices.put(utf, currentIndex);
  }

  public void addMethodHandle(int refKind, int refIdx) {
    writer.u1(ConstantType.MethodHandle.tag());
    writer.u1(refKind);
    writer.u2(refIdx);
    currentIndex++;
  }

  public void addMethodType(int descriptorIdx) {
    writer.u1(ConstantType.MethodType.tag());
    writer.u2(descriptorIdx);
    currentIndex++;
  }

  public void addInvokeDynamic(int bootstrapMethodIdx, int nameAndTypeIdx) {
    writer.u1(ConstantType.InvokeDynamic.tag());
    writer.u2(bootstrapMethodIdx);
    writer.u2(nameAndTypeIdx);
    currentIndex++;
  }

  public int length() {
    return (currentIndex + 1);
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return stream;
  }

  public void addSource(String string) {
    this.addUtf8("SourceFile");
    this.addUtf8(string);
  }
}
