package com.dujay.generator.redesign.constants;

import java.util.ArrayList;
import java.util.List;

import com.dujay.generator.redesign.visitor.Element;
import com.dujay.generator.redesign.visitor.Visitor;

public class ConstantPoolR implements Element {
  
  private List<ClassInfoR> classes;
  private List<MemberRefInfo> members;
  private List<NameAndTypeInfo> namesAndTypes;
  private List<Utf8Info> utf8s;
  
  public ConstantPoolR() {
    classes = new ArrayList<ClassInfoR>();
    members = new ArrayList<MemberRefInfo>();
    namesAndTypes = new ArrayList<NameAndTypeInfo>();
    utf8s = new ArrayList<Utf8Info>();
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

  public boolean add(ClassInfoR e) {
    return classes.add(e);
  }

  public boolean add(MemberRefInfo e) {
    return members.add(e);
  }

  public boolean add(NameAndTypeInfo e) {
    return namesAndTypes.add(e);
  }

  public boolean add(Utf8Info e) {
    return utf8s.add(e);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

}
