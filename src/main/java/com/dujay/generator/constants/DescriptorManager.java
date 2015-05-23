package com.dujay.generator.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DescriptorManager {
  private Map<String, Utf8Info> descriptors;
  
  public DescriptorManager() {
    this.descriptors = new HashMap<String, Utf8Info>();
  }
  
  public Utf8Info add(String name, String descriptorString) {
    Utf8Info descriptor = new Utf8Info(descriptorString);
    this.descriptors.put(name, descriptor);
    return descriptor;
  }
  
  public Optional<Utf8Info> get(String name) {
    return Optional.ofNullable(this.descriptors.get(name));
  }
}
