package com.dujay.jvm.methods;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dujay.jvm.typeclasses.Generatable;
import com.dujay.jvm.visitor.Element;
import com.dujay.jvm.visitor.Visitor;

public class MethodPool implements Element, Generatable {
  
  private List<MethodInfo> methods;
  
  public MethodPool() {
    this.methods = new ArrayList<MethodInfo>();
  }

  public boolean add(MethodInfo e) {
    return methods.add(e);
  }

  public int length() {
    return this.methods.size();
  }

  public List<MethodInfo> getMethods() {
    return methods;
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format("MethodPool [methods=%s]", methods);
  }

  public ByteArrayOutputStream generate() {
    return this.accept(new CodeGenVisitor());
  }

}
