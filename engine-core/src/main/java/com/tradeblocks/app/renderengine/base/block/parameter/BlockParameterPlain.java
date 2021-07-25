package com.tradeblocks.app.renderengine.base.block.parameter;

import com.tradeblocks.app.renderengine.base.block.parameter.BlockParameter;

/**
 *  Represents a Parameter which don't need to be resolved
 *  of a Block {@link com.tradeblocks.app.renderengine.base.block.Block}
 */
public class BlockParameterPlain extends BlockParameter<String, String> {
  
  public BlockParameterPlain(String name, String value) {
    super(name, value);
  }

  @Override
  public String toString() {
    return this.resolve();
  }

  @Override
  public String resolve() {
    return this.getValue();
  }
}
