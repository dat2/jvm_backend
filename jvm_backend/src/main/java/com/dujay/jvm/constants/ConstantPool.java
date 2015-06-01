package com.dujay.jvm.constants;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.ConstantInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.constants.structures.literals.LiteralInfo;
import com.dujay.jvm.constants.structures.literals.StringInfo;
import com.dujay.jvm.constants.visitors.CodeGenVisitor;
import com.dujay.jvm.constants.visitors.IndexVisitor;
import com.dujay.jvm.typeclasses.Generatable;
import com.dujay.jvm.visitor.Element;
import com.dujay.jvm.visitor.Visitor;

public class ConstantPool implements Element, Generatable {
  
  private Set<ClassInfo> classes;
  private Set<MemberRefInfo> members;
  private Set<NameAndTypeInfo> namesAndTypes;
  private Set<Utf8Info> utf8s;
  private Set<LiteralInfo> literals;
  
  private Map<String, NameAndTypeInfo> ntMap;
  private Map<String, ClassInfo> classMap;
  private Map<String, MemberRefInfo> memberMap;
  private Map<String, LiteralInfo> literalMap;
  
  // we need the order
  private List<ConstantInfo> finalList;
  
  private ClassInfo thisClass;
  private ClassInfo superClass;
  
  private Utf8Info source;
  private Utf8Info sourceFile;
  
  private DescriptorManager dm;
  
  public ConstantPool() {
    classes = new HashSet<ClassInfo>();
    members = new HashSet<MemberRefInfo>();
    namesAndTypes = new HashSet<NameAndTypeInfo>();
    utf8s = new HashSet<Utf8Info>();
    literals = new HashSet<LiteralInfo>();
    
    finalList = new ArrayList<ConstantInfo>();
    
    dm = DescriptorManager.empty();
    
    this.ntMap = new HashMap<String, NameAndTypeInfo>();
    this.classMap = new HashMap<String, ClassInfo>();
    this.memberMap = new HashMap<String, MemberRefInfo>();
    this.literalMap = new HashMap<String, LiteralInfo>();
  }

  public static String uniqueMethodName(String mName, Class<?> rType, Class<?>... pTypes) {
    return mName + "." + Descriptor.methodDescriptor(rType, pTypes) + ".method";
  }
  public static String uniqueMethodName(Method m) {
    return m.getName() + "." + Descriptor.methodDescriptor(m) + ".method";
  }
  public static String uniqueConstructorName(Constructor<?> constructor) {
    return uniqueClassName(constructor.getDeclaringClass()) + ".<init>";
  }
  public static String uniqueClassName(Class<?> c) {
    return c.getSimpleName() + ".class";
  }
  public static String uniqueFieldName(Field f) {
    return f.getName() + ".field";
  }
  public static String uniqueNTName(String ciName, String methodName) {
    return methodName + "." + ciName + ".NT";
  }
  public static String uniqueNTName(Member m) {
    return m.getName() + "." + uniqueClassName(m.getDeclaringClass()) + ".NT";
  }
  public static String uniqueNTName(Constructor<?> constructor) {
    return "<init>." + Descriptor.methodDescriptor(Void.class, constructor.getParameterTypes()) + ".NT";
  }
  public static String uniqueConstructorNTName(Class<?>... paramTypes) {
    return "<init>." + Descriptor.methodDescriptor(Void.class, paramTypes) + ".NT";
  }

  public Collection<ClassInfo> classes() {
    return classes;
  }
  
  public Collection<MemberRefInfo> members() {
    return members;
  }

  public Collection<NameAndTypeInfo> namesAndTypes() {
    return namesAndTypes;
  }

  public Collection<Utf8Info> utf8s() {
    return utf8s;
  }

  public Collection<LiteralInfo> literals() {
    return literals;
  }

  public ClassInfo getThisClass() {
    return thisClass;
  }

  public ClassInfo getSuperClass() {
    return superClass;
  }

  public void setThisClass(ClassInfo thisClass) {
    classMap.put("this", thisClass);
    this.thisClass = thisClass;
  }

  public void setSuperClass(ClassInfo superClass) {
    classMap.put("super", thisClass);
    this.superClass = superClass;
  }

  private <T> void put(Map<String, T> m, String key, T t) {
    m.put(key, t);
  }

  public void put(String key, ClassInfo value) {
    put(classMap, key, value);
    this.add(value);
  }
  
  public void put(String literalName, LiteralInfo s) {
    put(literalMap, literalName, s);
    this.add(s);
  }
  
  public void put(String mName, MemberRefInfo member) {
    put(memberMap, mName, member);
    this.add(member);
  }
  
  public void put(String key, NameAndTypeInfo value) {
    put(ntMap, key, value);
    this.add(value);
  }
  
  public Optional<NameAndTypeInfo> getNameAndType(String nt) {
    return Optional.ofNullable(ntMap.get(nt));
  }
  
  public Optional<LiteralInfo> getLiteral(String lit) {
    return Optional.ofNullable(literalMap.get(lit));
  }
  
  public Optional<ClassInfo> getClassInfo(String ci) {
    return Optional.ofNullable(classMap.get(ci));
  }
  
  public Optional<MemberRefInfo> getMemberRefInfo(String mref) {
    return Optional.ofNullable(memberMap.get(mref));
  }

  public void add(ClassInfo e) {
    classes.add(e);

    Optional<Utf8Info> name = this.getUtf8(e.getName().getString());
    if(!name.isPresent()) {
      utf8s.add(e.getName());
    } else {
      e.setName(name.get());
    }
  }

  public void add(MemberRefInfo e) {
    members.add(e);
  }

  public void add(LiteralInfo e) {
    literals.add(e);
    if(e instanceof StringInfo) {
      StringInfo si = (StringInfo) e;
      utf8s.add(si.getUtf8());
    }
  }

  public void add(NameAndTypeInfo e) {
    namesAndTypes.add(e);
    
    Optional<Utf8Info> name = this.getUtf8(e.getName().getString());
    if(!name.isPresent()) {
      utf8s.add(e.getName());
    } else {
      e.setName(name.get());
    }
    
    Optional<Utf8Info> type = this.getUtf8(e.getType().getString());
    if(!type.isPresent()) {
      utf8s.add(e.getType());
    } else{
      e.setType(type.get());
    }
  }

  public void add(Utf8Info e) {
    utf8s.add(e);
  }
  
  public void addSource(String sourceFilename) {
    this.source = new Utf8Info("SourceFile");
    this.sourceFile = new Utf8Info(sourceFilename);
    
    this.add(this.source);
    this.add(this.sourceFile);
  }
  
  public Optional<Utf8Info> getSource() {
    return Optional.ofNullable(source);
  }

  public Optional<Utf8Info> getSourceFile() {
    return Optional.ofNullable(sourceFile);
  }

  public Optional<Utf8Info> getUtf8(String utf8) {
    return utf8s.stream()
        .filter(x -> x.getString().equals(utf8))
        .findFirst();
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String
        .format(
            "ConstantPool [classes=%s, members=%s, namesAndTypes=%s, utf8s=%s, thisClass=%s, superClass=%s]",
            classes, members, namesAndTypes, utf8s, thisClass, superClass);
  }

  public Utf8Info addDescriptor(String name, String descriptor) {
    return dm.add(name, descriptor);
  }

  public Optional<Utf8Info> getDescriptor(String name) {
    return dm.get(name);
  }

  public List<Byte> generate() {
    return this.accept(new CodeGenVisitor());
  }
  
  public void setIndices() {
    // index visitor controls where to place the constants in the final .class file
    this.finalList = this.accept(new IndexVisitor())
        .collect(Collectors.toList());
  }
  
  public Collection<ConstantInfo> getConstants() {
    return finalList;
  }

}
