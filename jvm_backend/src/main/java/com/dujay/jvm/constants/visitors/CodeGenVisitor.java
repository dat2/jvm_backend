package com.dujay.jvm.constants.visitors;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.ConstantInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.constants.structures.literals.LiteralInfo;
import com.dujay.jvm.constants.structures.literals.LongLiteralInfo;
import com.dujay.jvm.visitor.ByteArrayVisitor;

public class CodeGenVisitor extends ByteArrayVisitor {

  private static final Logger logger = (Logger) LoggerFactory.getLogger("cgvisitor");

  @Override
  public List<Byte> visit(ClassInfo c) {
    logger.debug(c.toString());

    // write the class structure
    u1(c.tag());
    u2(c.getName().getIndex());

    return super.visit(c);
  }

  @Override
  public List<Byte> visit(NameAndTypeInfo nt) {
    logger.debug(nt.toString());

    // write the name and type
    u1(nt.tag());
    u2(nt.getName().getIndex());
    u2(nt.getType().getIndex());

    return super.visit(nt);
  }

  @Override
  public List<Byte> visit(MemberRefInfo m) {
    logger.debug(m.toString());

    // write the member ref structure
    u1(m.tag());
    u2(m.getOwnerClass().getIndex());
    u2(m.getNameAndType().getIndex());

    return super.visit(m);
  }

  @Override
  public List<Byte> visit(Utf8Info u) {
    logger.debug(u.toString());

    // write the utf8 structure
    u1(u.tag());
    u2(u.length());
    for (byte b : u.getBytes()) {
      u1(b);
    }

    return super.visit(u);
  }

  @Override
  public List<Byte> visit(LiteralInfo i) {
    logger.debug(i.toString());

    
    Consumer<Integer> c;
    switch(i.numBytes()) {
    case 1:
      c = this::u1;
      break;
    case 2:
      c = this::u2;
      break;
    case 4:
      c = this::u4;
      break;
    default:
        c = (x) -> {};
    }
    
    if(i instanceof LongLiteralInfo) {
      LongLiteralInfo lv = (LongLiteralInfo) i;

      // it takes two entries?
      u1(i.tag());
      u4(lv.bytes());
      u4(lv.lowBytes());
      
    } else {
      u1(i.tag());
      c.accept(i.bytes());
    }

    return super.visit(i);
  }

  @Override
  public List<Byte> visit(ConstantPool cp) {
    
    Collection<ConstantInfo> constants = cp.getConstants();

    this.u2(constants.size() + 1);
    this.visit(constants);

    return super.visit(cp);
  }
}
