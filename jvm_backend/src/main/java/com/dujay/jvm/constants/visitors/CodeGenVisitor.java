package com.dujay.jvm.constants.visitors;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.ConstantInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.StringInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.visitor.ByteArrayVisitor;

public class CodeGenVisitor extends ByteArrayVisitor {

  private static final Logger logger = (Logger) LoggerFactory.getLogger("cgvisitor");

  @Override
  public ByteArrayOutputStream visit(ClassInfo c) {
    logger.debug(c.toString());

    // write the class structure
    u1(c.tag());
    u2(c.getNameIndex());

    return super.visit(c);
  }

  @Override
  public ByteArrayOutputStream visit(NameAndTypeInfo nt) {
    logger.debug(nt.toString());

    // write the name and type
    u1(nt.tag());
    u2(nt.getNameIndex());
    u2(nt.getTypeIndex());

    return super.visit(nt);
  }

  @Override
  public ByteArrayOutputStream visit(MemberRefInfo m) {
    logger.debug(m.toString());

    // write the member ref structure
    u1(m.tag());
    u2(m.getOwnerClassIndex());
    u2(m.getNameAndTypeIndex());

    return super.visit(m);
  }

  @Override
  public ByteArrayOutputStream visit(Utf8Info u) {
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
  public ByteArrayOutputStream visit(StringInfo i) {
    logger.debug(i.toString());

    u1(i.tag());
    u2(i.getUtf8().getIndex());

    return super.visit(i);
  }

  @Override
  public ByteArrayOutputStream visit(ConstantPool cp) {
    
    Collection<ConstantInfo> constants = cp.getConstants();

    this.u2(constants.size() + 1);
    this.visit(constants);

    return super.visit(cp);
  }
}
