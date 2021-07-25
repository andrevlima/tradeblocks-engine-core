package com.tradeblocks.app.renderengine.implementation.mql5;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.annotations.BlockConnectors;
import com.tradeblocks.app.renderengine.base.annotations.BlockParameters;
import com.tradeblocks.app.renderengine.base.annotations.Connector;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.parameter.InputType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@BlockParameters({
	@Parameter(label = "condition", inputType = InputType.NUMBER)
})
@BlockConnectors({
  	@Connector("outs")
})
public class WhileRepeat extends AbstractRenderableBlock {
	@Override
	public String template() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
}
