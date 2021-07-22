package com.tradeblocks.app.renderengine.base.parameter;

import com.tradeblocks.app.renderengine.base.parameter.Resolvable;

public abstract class BlockParameter<T, R> implements Resolvable<R> {
  private String name = null;
  private T value = null;
  
  public BlockParameter(String name, T value) {
    this.setName(name);
    this.setValue(value);
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the value
   */
  public T getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(T value) {
    this.value = value;
  }
  
  public abstract R resolve();
  
}
