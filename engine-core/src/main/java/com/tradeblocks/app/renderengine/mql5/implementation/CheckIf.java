package com.tradeblocks.app.renderengine.mql5.implementation;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.annotations.CompatibleWith;
import com.tradeblocks.app.renderengine.base.annotations.Connector;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.parameter.InputType;

import lombok.Getter;
import lombok.Setter;

@CompatibleWith("mql5")
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
