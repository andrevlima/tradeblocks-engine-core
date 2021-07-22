package com.tradeblocks.app.renderengine.mql5.implementation;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.annotations.CompatibleWith;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.parameter.InputType;
import lombok.Getter;
import lombok.Setter;

@CompatibleWith("mql5")
public class ShowComment extends AbstractRenderableBlock {
    @Parameter(label = "messageTextLbl", inputType = InputType.TEXT) @Getter @Setter
    private BlockParameterPlain text;

    @Override
    public String template() {
        return "Comment(${this.text});";
    }
}
