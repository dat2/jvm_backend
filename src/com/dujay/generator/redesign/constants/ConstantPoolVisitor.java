package com.dujay.generator.redesign.constants;

import java.io.ByteArrayOutputStream;

import com.dujay.generator.redesign.visitor.ByteArrayVisitor;

public class ConstantPoolVisitor extends ByteArrayVisitor {

  @Override
  public ByteArrayOutputStream visit(ClassInfoR c) {
    
    // write the class structure
    u1(c.tag());
    u2(c.getUtf8Index());
    
    return super.visit(c);
  }
  
  @Override
  public ByteArrayOutputStream visit(NameAndTypeInfo nt) {
    
    // write the name and type
    u1(nt.tag());
    u2(nt.getNameIndex());
    u2(nt.getTypeIndex());
    
    return super.visit(nt);
  }
  
  @Override
  public ByteArrayOutputStream visit(MemberRefInfo m) {
    
    // write the member ref structure
    u1(m.tag());
    u2(m.getOwnerClassIndex());
    u2(m.getNameAndTypeIndex());
    
    return super.visit(m);
  }
  
  @Override
  public ByteArrayOutputStream visit(Utf8Info u) {
    
    // write the utf8 structure
    u1(u.tag());
    u2(u.length());
    for (byte b : u.getBytes()) {
      u1(b);
    }
    
    return super.visit(u);
  }
  
  @Override
  public ByteArrayOutputStream visit(ConstantPoolR cp) {
    
    // write all the structures in the constant pool
    
    return super.visit(cp);
  }
}
