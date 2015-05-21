package com.dujay.generator.redesign.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dujay.generator.redesign.visitor.Element;
import com.dujay.generator.redesign.visitor.Visitor;

public class ConstantPoolR implements Element {
  
  private List<ClassInfoR> classes;
  private List<MemberRefInfo> members;
  private List<NameAndTypeInfo> namesAndTypes;
  private List<Utf8Info> utf8s;
  private List<StringInfo> strings;
  
  private ClassInfoR thisClass;
  private ClassInfoR superClass;
  
  private Utf8Info source;
  private Utf8Info sourceFile;
  
  private ConstantPoolVisitor v;
  
  public ConstantPoolR() {
    classes = new ArrayList<ClassInfoR>();
    members = new ArrayList<MemberRefInfo>();
    namesAndTypes = new ArrayList<NameAndTypeInfo>();
    utf8s = new ArrayList<Utf8Info>();
    strings = new ArrayList<StringInfo>();
    
    v = new ConstantPoolVisitor();
  }

  public List<ClassInfoR> getClasses() {
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

  public ClassInfoR getThisClass() {
    return thisClass;
  }

  public void setThisClass(ClassInfoR thisClass) {
    this.thisClass = thisClass;
  }

  public ClassInfoR getSuperClass() {
    return superClass;
  }

  public void setSuperClass(ClassInfoR superClass) {
    this.superClass = superClass;
  }

  public void add(ClassInfoR e) {
    classes.add(e);
    utf8s.add(e.getUtf8());
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
  
  public Optional<ClassInfoR> getClass(String className) {
    return classes.stream()
        .filter(x -> x.getUtf8().getString().equals(className))
        .findFirst();
  }
  
  public Optional<MemberRefInfo> getMemberRef(String className, String memberName) {
    return members.stream()
        .filter(x -> x.getOwnerClass().getUtf8().getString().equals(className)
            && x.getNameAndType().getName().getString().equals(memberName) )
        .findFirst();
  }
  
  public Optional<StringInfo> getString(String utf8) {
    return strings.stream()
        .filter(x -> x.getUtf8().getString().equals(utf8))
        .findFirst();
  }
  
  public int length() {
    // 1 is extra length (for some reason)
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
            "ConstantPoolR [classes=%s, members=%s, namesAndTypes=%s, utf8s=%s, thisClass=%s, superClass=%s]",
            classes, members, namesAndTypes, utf8s, thisClass, superClass);
  }

  public void setIndices() {
    this.accept(v);
  }

  public ConstantPoolVisitor getVisitor() {
    return v;
  }

}
