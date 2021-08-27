package com.kvoli.base;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

// Bad practices / cheat codes
public class Command {
  public String type;
  private Map<String, String> properties = new HashMap<>();

  @JsonAnyGetter
  public Map<String, String> get() {
    return properties;
  }

  @JsonAnySetter
  public void add(String key, String value) {
    properties.put(key, value);
  }
}
