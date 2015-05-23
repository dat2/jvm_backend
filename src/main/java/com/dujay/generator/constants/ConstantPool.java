package com.dujay.generator.constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dujay.generator.visitor.Element;
import com.dujay.generator.visitor.Visitor;

public class ConstantPool implements Element {
  
  private List<ClassInfo> classes;
  private List<MemberRefInfo> members;
  private List<NameAndTypeInfo> namesAndTypes;
  private List<Utf8Info> utf8s;
  private List<StringInfo> strings;
  
  private ClassInfo thisClass;
  private ClassInfo superClass;
  
  private Utf8Info source;
  private Utf8Info sourceFile;
  
  private DescriptorManager dm;
  
  private Visitor<ByteArrayOutputStream> v;
  
  public ConstantPool() {
    classes = new ArrayList<ClassInfo>();
    members = new ArrayList<MemberRefInfo>();
    namesAndTypes = new ArrayList<NameAndTypeInfo>();
    utf8s = new ArrayList<Utf8Info>();
    strings = new ArrayList<StringInfo>();
    
    v = new ConstantPoolVisitor();
    dm = new DescriptorManager();
  }

  public List<ClassInfo> getClasses() {
    return classes;
  }
  
  public List<MemberRefInfo> getMembers() {
    return members;
  }

  public List<NameAndTypeInfo> getNamesAndTypes() {
    return namesAndTypes;
  }

  public List<Utf8Info> getUtf8s() {
    return utf8s;
  }

  public List<StringInfo> getStrings() {
    return strings;
  }

  public ClassInfo getThisClass() {
    return thisClass;
  }

  public void setThisClass(ClassInfo thisClass) {
    this.thisClass = thisClass;
  }

  public ClassInfo getSuperClass() {
    return superClass;
  }

  public void setSuperClass(ClassInfo superClass) {
    this.superClass = superClass;
  }

  public void add(ClassInfo e) {
    classes.add(e);
    utf8s.add(e.getName());
  }

  public void add(MemberRefInfo e) {
    members.add(e);
  }

  public void add(StringInfo e) {
    strings.add(e);
    this.add(e.getUtf8());
  }

  public void add(NameAndTypeInfo e) {
    namesAndTypes.add(e);
    utf8s.add(e.getName());
    utf8s.add(e.getType());
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
  
  public Optional<ClassInfo> getClass(String className) {
    return classes.stream()
        .filter(x -> x.getName().getString().equals(className))
        .findFirst();
  }
  
  public Optional<MemberRefInfo> getMemberRef(ClassInfo classInfo, String memberName) {
    return members.stream()
        .filter(x -> x.getOwnerClass().getName().getString().equals(classInfo.getName().getString())
            && x.getNameAndType().getName().getString().equals(memberName) )
        .findFirst();
  }
  
  public Optional<StringInfo> getString(String utf8) {
    return strings.stream()
        .filter(x -> x.getUtf8().getString().equals(utf8))
        .findFirst();
  }
  
  public int length() {
    // 4 is the constants for this, thisUtf8, super, superUtf8
    return 4
        + classes.size() + members.size() + namesAndTypes.size() + utf8s.size() + strings.size();
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

  public ByteArrayOutputStream generate() {
    return this.accept(v);
  }

  public Visitor<ByteArrayOutputStream> getVisitor() {
    return v;
  }

  public Utf8Info addDescriptor(String name, String descriptor) {
    return dm.add(name, descriptor);
  }

  public Optional<Utf8Info> getDescriptor(String name) {
    return dm.get(name);
  }

}
