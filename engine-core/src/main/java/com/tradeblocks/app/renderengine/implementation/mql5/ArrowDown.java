package com.tradeblocks.app.renderengine.implementation.mql5;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.annotations.CompatibleWith;

@CompatibleWith("mql5")
public class ArrowDown extends AbstractRenderableBlock {

  @Override
  public String template() {
    return "ArrowDown();";
  }
}
