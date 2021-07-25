package com.tradeblocks.app.renderengine.base.block.parameter;

public enum InputType {
  TEXT("text"),
  NUMBER("number"),
  TIME("time"),
  TOGGLE("toggle"),
  ANY("*"),
  NONE("");
  
  private String id;
  
  private InputType(String id) {
    this.id = id;
  }
  
  public String toString() {
    return id;
  }
}