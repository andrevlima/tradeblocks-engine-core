package com.tradeblocks.app.renderengine.base.block.parameter;

/**
 * Represents a Parameter of a Block {@link com.tradeblocks.app.renderengine.base.block.Block} which
 * should be resolved
 *
 * @param <T> type of input data of parameter
 * @param <R> type of result after resolving parameter
 */
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
   * @return the compatibleWith
   */
  public T getValue() {
    return value;
  }

  /**
   * @param value the compatibleWith to set
   */
  public void setValue(T value) {
    this.value = value;
  }
  
  public abstract R resolve();
  
}
