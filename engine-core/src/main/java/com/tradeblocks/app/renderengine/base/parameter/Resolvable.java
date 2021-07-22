package com.tradeblocks.app.renderengine.base.parameter;

/**
 * Represents something that can be resolved
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 * @param <R> type of result
 */
@FunctionalInterface
public interface Resolvable<R> {
  public R resolve();
}
