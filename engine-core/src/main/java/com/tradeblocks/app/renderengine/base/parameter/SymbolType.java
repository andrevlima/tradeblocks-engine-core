package com.tradeblocks.app.renderengine.base.parameter;

public enum SymbolType {
  FUNCTION("function"),
  VARIABLE("variable"),
  COMPARATOR("comparator"),
  RAW("raw"),
  ANY("*");
  
  private String id;
  
  private SymbolType(String id) {
    this.id = id;
  }
  
  public String toString() {
    return id;
  }
}