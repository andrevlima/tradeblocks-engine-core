package com.tradeblocks.app.renderengine.implementation.mql5.blocks;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.annotations.Block;

@Block(compatibleWith = "mql5")
public class ArrowDown extends AbstractRenderableBlock {

  @Override
  public String template() {
    return "ArrowDown();";
  }
}
