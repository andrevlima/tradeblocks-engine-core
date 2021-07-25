package com.tradeblocks.app.renderengine.implementation.mql5.blocks;

import java.util.List;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.annotations.Block;
import com.tradeblocks.app.renderengine.base.annotations.Connector;

import lombok.Getter;
import lombok.Setter;

@Block(compatibleWith = "mql5", isStaringPoint = true)
public class OnTick extends AbstractRenderableBlock {

  @Connector(value = "outs", multiple = true) @Getter @Setter
  private List<BlockConnector> outs;
  
  @Override
  public String template() {
    return "void OnTick() { ${this.outs.get(0)} }";
  }
}
