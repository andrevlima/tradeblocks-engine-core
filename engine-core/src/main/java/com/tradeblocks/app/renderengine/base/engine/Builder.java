/**
 * 
 */
package com.tradeblocks.app.renderengine.base.engine;

/**
 * Something that builds
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public interface Builder<T> {
  public T build() throws Exception;
}
