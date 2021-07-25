package com.tradeblocks.app.renderengine.implementation.mql5;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.annotations.*;
import com.tradeblocks.app.renderengine.base.parameter.InputType;
import com.tradeblocks.app.renderengine.base.parameter.SymbolType;
import lombok.Getter;
import lombok.Setter;

@CompatibleWith("mql5")
public class ForLoop extends AbstractRenderableBlock {

    @Parameter(label = "startForLoopLbl", inputType = InputType.NUMBER) @Getter @Setter
    private BlockParameterPlain start;

    @Parameter(label = "stopForLoopLbl", inputType = InputType.NUMBER) @Getter @Setter
    private BlockParameterPlain stop;

    @Parameter(label = "indexVarForLoopLbl", inputType = InputType.NUMBER, inputSymbolType = SymbolType.VARIABLE) @Getter @Setter
    private BlockParameterPlain index;


    @Connector("repeat") @Getter @Setter
    private BlockConnector repeat;

    @Override
    public String template() {
        return "for(${this.index} = ${this.start}; compareAdapted(${this.start}, ${this.stop}, ${this.index}); incremetAdapted(${this.start}, ${this.stop}, ${this.index})) {\n" +
                "   $!{this.repeat}\n" +
                "}";
    }
}
