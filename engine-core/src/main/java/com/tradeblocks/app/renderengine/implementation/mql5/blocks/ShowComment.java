package com.tradeblocks.app.renderengine.implementation.mql5.blocks;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.block.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.annotations.Block;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.block.parameter.InputType;
import lombok.Getter;
import lombok.Setter;

@Block(compatibleWith = "mql5")
public class ShowComment extends AbstractRenderableBlock {
    @Parameter(label = "messageTextLbl", inputType = InputType.TEXT) @Getter @Setter
    private BlockParameterPlain text;

    @Override
    public String template() {
        return "Comment(${this.text});";
    }
}
