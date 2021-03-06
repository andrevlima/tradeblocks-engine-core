package com.tradeblocks.app.renderengine.implementation.mql5.blocks;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.block.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.annotations.Block;
import com.tradeblocks.app.renderengine.base.annotations.Connector;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.block.parameter.InputType;

import lombok.Getter;
import lombok.Setter;

@Block(compatibleWith = "mql5")
public class CheckIf extends AbstractRenderableBlock {
  @Parameter(label = "conditionLbl", inputType = InputType.TOGGLE) @Getter @Setter
  private BlockParameterPlain formula;

  @Connector("yes") @Getter @Setter
  private BlockConnector yes;

  @Connector("no") @Getter @Setter
  private BlockConnector no;

  @Override
  public String template() {
    return "if(${this.formula}) {" +
            " $!{this.yes} " +
            "} else { " +
            " $!{this.no} " +
            "}";
  }
}
