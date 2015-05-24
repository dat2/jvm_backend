package com.dujay.generator.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dujay.generator.attributes.Attribute;
import com.dujay.generator.attributes.CodeAttribute;
import com.dujay.generator.constants.structures.Utf8Info;
import com.dujay.generator.enums.AccessFlag;
import com.dujay.generator.visitor.Element;
import com.dujay.generator.visitor.Visitor;

public class MethodInfo implements Element {
  private int accessFlags;
  private Utf8Info name;
  private Utf8Info descriptor;
  
  private List<Attribute> attributes;
  private CodeAttribute code;
  
  public MethodInfo(Utf8Info name, Utf8Info descriptor, AccessFlag... flags) {
    this.accessFlags = AccessFlag.mask(flags);
    this.name = name;
    this.descriptor = descriptor;
    this.attributes = new ArrayList<Attribute>();
    this.code = null;
  }

  public boolean add(Attribute e) {
    return attributes.add(e);
  }
  
  public boolean add(CodeAttribute c) {
    this.code = c;
    return this.add((Attribute)c);
  }
  
  public Optional<CodeAttribute> getCode() {
    return Optional.ofNullable(this.code);
  }

  public int getAccessFlags() {
    return accessFlags;
  }

  public Utf8Info getName() {
    return name;
  }

  public Utf8Info getDescriptor() {
    return descriptor;
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format(
        "MethodInfo [accessFlags=(%s), name=%s, descriptor=%s, attributes=%s]",
        AccessFlag.maskedToString(accessFlags), name.getString(), descriptor.getString(), attributes);
  }
}
