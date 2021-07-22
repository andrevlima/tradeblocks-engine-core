package com.tradeblocks.app.renderengine.mql5.implementation;

import java.util.List;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.annotations.BlockStartPoint;
import com.tradeblocks.app.renderengine.base.annotations.CompatibleWith;
import com.tradeblocks.app.renderengine.base.annotations.Connector;

import lombok.Getter;
import lombok.Setter;

@BlockStartPoint
@CompatibleWith("mql5")
public class OnTick extends AbstractRenderableBlock {

  @Connector(value = "outs", multiple = true) @Getter @Setter
  private List<BlockConnector> outs;
  
  @Override
  public String template() {
    return "void OnTick() { ${this.outs.get(0)} }";
  }
}
