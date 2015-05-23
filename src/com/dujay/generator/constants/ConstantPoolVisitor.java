package com.dujay.generator.constants;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

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
  public ByteArrayOutputStream visit(StringInfo i) {
    
    u1(i.tag());
    u2(i.getUtf8().getIndex());
    
    return super.visit(i);
  }
  
  @Override
  public ByteArrayOutputStream visit(ConstantPool cp) {
    
    u2(cp.length() - 1);
    System.out.println(cp.length());
    
    int index = 1;
    
    cp.getThisClass().getUtf8().setIndex(2);
    cp.getSuperClass().getUtf8().setIndex(4);
    index = acceptAll(Arrays.asList(cp.getThisClass(), cp.getThisClass().getUtf8(),
        cp.getSuperClass(), cp.getSuperClass().getUtf8()), index);

    index = acceptAll(cp.getUtf8s(), index);
    index = acceptAll(cp.getStrings(), index);
    index = acceptAll(cp.getClasses(), index);
    index = acceptAll(cp.getNamesAndTypes(), index);
    index = acceptAll(cp.getMembers(), index);
    
    return super.visit(cp);
  }
  
  private int acceptAll(List<? extends ConstantInfo> list, int index) {
    return list.stream().reduce(index, (i,x) -> {
      x.setIndex(i);
      x.accept(this);
      System.out.println(x);
      return i + 1;
    }, (x,y) -> x + y);
  }
}
