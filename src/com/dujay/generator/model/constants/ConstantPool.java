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
  
  private Map<ClassInfo, Integer> classIndices;
  private Map<MemberDescriptor, Integer> memberRefIndices;
  private Map<Descriptor, Integer> variableIndices;
  private Map<Descriptor, Integer> nameAndTypeIndices;
  private Map<String, Integer> methodIndices;
  private Map<String, Integer> utf8Indices;
  private Map<String, Integer> stringIndices;
  
  private int currentIndex;

  public ConstantPool() {
    stream = new ByteArrayOutputStream();
    writer = new ByteStreamWriter(stream);
    
    currentIndex = 0;
    
    classIndices = new HashMap<ClassInfo, Integer>();
    memberRefIndices = new HashMap<MemberDescriptor, Integer>();
    variableIndices = new HashMap<Descriptor, Integer>();
    nameAndTypeIndices = new HashMap<Descriptor, Integer>();
    
    methodIndices = new HashMap<String, Integer>();
    utf8Indices = new HashMap<String, Integer>();
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
  
  public int getClassIndex(ClassInfo classDescriptor) {
    return getIndex(classIndices, classDescriptor);
  }

  public int getMemberRefIndex(MemberDescriptor descriptor) {
    return getIndex(memberRefIndices, descriptor);
  }
  
  public int getUtf8Index(String utf) {
    return getIndex(utf8Indices, utf);
  }

  public int getVariableIndex(Descriptor descriptor) {
    return getIndex(variableIndices, descriptor);
  }
  
  public int getNameAndTypeIndex(Descriptor descriptor) {
    return getIndex(nameAndTypeIndices, descriptor);  
  }
  
  public int getMethodIndex(String name) {
    return getIndex(methodIndices, name);
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

  public void addClass(ClassInfo descriptor) {
    writer.u1(ConstantType.Class.tag());
    writer.u2(++currentIndex + 1); // needs to be 1 indexed
    classIndices.put(descriptor, currentIndex);
    
    this.addUtf8(descriptor.getTypeString());
  }
  
  public void addMethod(MethodDescriptor descriptor) {
    String methodName = descriptor.getName();
    this.addUtf8(methodName);
    methodIndices.put(methodName, currentIndex);
    
    this.addUtf8(descriptor.getTypeString());
  }

  public void addString(String s) {
    writer.u1(ConstantType.String.tag());
    writer.u2(++currentIndex + 1);
    
    stringIndices.put(s, currentIndex);
    
    this.addUtf8(s);
  }

  private void addMemberRef(int memberType, MemberDescriptor descriptor) throws Exception {
    ClassInfo ownerClass = descriptor.getOwnerClass();
    
    int classIdx = this.getClassIndex(ownerClass);
    if(classIdx == -1) {
      throw new Exception("Class " + ownerClass.getName() + " not found!");
    }
    
    String name = descriptor.getName();
    int nameTypeIdx = this.getNameAndTypeIndex(descriptor);
    if(nameTypeIdx == -1) {
      throw new Exception("Name " + name + " not found!");
    }
    
    writer.u1(memberType);
    writer.u2(classIdx);
    writer.u2(nameTypeIdx);
    currentIndex++;
    
    memberRefIndices.put(descriptor, currentIndex);
  }

  public void addFieldRef(MemberDescriptor descriptor) throws Exception {
    addMemberRef(ConstantType.Fieldref.tag(), descriptor);
  }

  public void addMethodRef(MemberDescriptor descriptor) throws Exception {
    addMemberRef(ConstantType.Methodref.tag(), descriptor);
  }

  public void addInterfaceMethodRef(MemberDescriptor descriptor) throws Exception {
    addMemberRef(ConstantType.InterfaceMethodref.tag(), descriptor);
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

  public void addNameAndType(Descriptor descriptor) throws Exception {
    String name = descriptor.getName();
    String type = descriptor.getTypeString();
    
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
    
    this.nameAndTypeIndices.put(descriptor, currentIndex);
  }
  
  public void addVariable(Descriptor descriptor) {
    String variableName = descriptor.getName();
    String type = descriptor.getTypeString();
    
    this.addUtf8(variableName);
    this.variableIndices.put(descriptor, currentIndex);
    
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
