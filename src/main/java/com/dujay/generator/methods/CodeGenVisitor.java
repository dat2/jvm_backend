package com.dujay.generator.methods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.generator.attributes.CodeAttribute;
import com.dujay.generator.visitor.ByteArrayVisitor;

public class CodeGenVisitor extends ByteArrayVisitor {
  
  private static final Logger logger = (Logger) LoggerFactory.getLogger("mpvisitor");

  @Override
  public ByteArrayOutputStream visit(MethodInfo mi) {
    
    logger.debug(mi.toString());
    
    logger.debug("method info");
    this.u2(mi.getAccessFlags());
    this.u2(mi.getName().getIndex());
    this.u2(mi.getDescriptor().getIndex());
    
    // attributes
    logger.debug("attributes");
    this.u2(mi.getAttributes().size());
    this.visit(mi.getAttributes());
    
    return super.visit(mi);
  }

  @Override
  public ByteArrayOutputStream visit(CodeAttribute ca) {
    
    logger.debug(ca.toString());
    
    int codeLength = ca.getStream().size();
    int exceptionsLength = 0;
    int attributesLength = ca.getAttributes().size();
    int fullLength = 2+2+4+codeLength+2+exceptionsLength+2+attributesLength;
    
    logger.debug("code attribute");
    this.u2(ca.getAttributeName().getIndex());
    this.u4(fullLength);
    this.u2(ca.getMaxStack());
    this.u2(ca.getMaxLocals());
    this.u4(codeLength);

    logger.debug("bytecode");
    try {
      logger.debug("size before: " + getStream().size());
      ca.getStream().writeTo(getStream());
      logger.debug("size after: " + getStream().size());
    } catch (IOException e) {
      e.printStackTrace();
    }

    // TODO exceptions
    logger.debug("exceptions");
    this.u2(exceptionsLength);

    // TODO validate correct attribute types
    logger.debug("code attributes");
    this.u2(attributesLength);
    this.visit(ca.getAttributes());
    
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(MethodPool mp) {
    
    logger.debug("method pool");
    this.u2(mp.length());
    
    this.visit(mp.getMethods());
    
    return super.visit(mp);
  }
}
