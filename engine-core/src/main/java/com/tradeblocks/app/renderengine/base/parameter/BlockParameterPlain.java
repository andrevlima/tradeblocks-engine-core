package com.tradeblocks.app.renderengine.base.parameter;

import com.tradeblocks.app.renderengine.base.parameter.BlockParameter;

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
