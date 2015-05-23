package com.dujay.generator.constants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.generator.visitor.ByteArrayVisitor;
import com.dujay.generator.visitor.Element;

public class ConstantPoolVisitor extends ByteArrayVisitor {

  private static final Logger logger = (Logger) LoggerFactory.getLogger("cpvisitor");

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

    int index = 1;

    // set indices of names before writing this , and super class
    ClassInfo thisClass = cp.getThisClass();
    Utf8Info thisClassName = thisClass.getName();
    ClassInfo superClass = cp.getSuperClass();
    Utf8Info superClassName = superClass.getName();

    thisClassName.setIndex(2);
    superClassName.setIndex(4);

    List<ConstantInfo> firstConstants = Arrays.asList(thisClass, thisClassName, superClass, superClassName);

    // write this class, this class name, super class, super class name
    index = setIndices(firstConstants, index);

    // TODO reorder constants?
    index = setIndices(cp.getUtf8s(), index);
    index = setIndices(cp.getStrings(), index);
    index = setIndices(cp.getClasses(), index);
    index = setIndices(cp.getNamesAndTypes(), index);
    index = setIndices(cp.getMembers(), index);

    List<Element> constants = new ArrayList<Element>();
    constants.addAll(firstConstants);
    constants.addAll(cp.getUtf8s());
    constants.addAll(cp.getStrings());
    constants.addAll(cp.getClasses());
    constants.addAll(cp.getNamesAndTypes());
    constants.addAll(cp.getMembers());

    this.u2(constants.size() + 1);
    this.visit(constants);

    return super.visit(cp);
  }

  private int setIndices(List<? extends ConstantInfo> list, int index) {
    return list.stream().reduce(index, (i, x) -> {
      // set index before generating is easier
      x.setIndex(i);
      return i + 1;
    }, (x, y) -> x + y);
  }
}
