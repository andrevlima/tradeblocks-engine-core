package com.tradeblocks.app.renderengine.mql5.implementation;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.annotations.CompatibleWith;

@CompatibleWith("mql5")
public class ArrowUp extends AbstractRenderableBlock {

  @Override
  public String template() {
    return "ArrowUp();";
  }

}
