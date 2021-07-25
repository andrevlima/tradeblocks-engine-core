package com.tradeblocks.app.renderengine.implementation.mql5.blocks;

import com.tradeblocks.app.renderengine.base.block.AbstractRenderableBlock;
import com.tradeblocks.app.renderengine.base.block.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.annotations.Block;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.block.parameter.InputType;
import lombok.Getter;
import lombok.Setter;

@Block(compatibleWith = "mql5")
public class CreateObject extends AbstractRenderableBlock {
    @Parameter(label = "symbolLbl", inputType = InputType.TEXT, defaultValue = "Symbol()", isRequired = false) @Getter @Setter
    private BlockParameterPlain symbol;
    @Parameter(label = "objIdLbl", inputType = InputType.TEXT, defaultValue = "ObjCreated", isRequired = false) @Getter @Setter
    private BlockParameterPlain id;
    @Parameter(label = "chartTimeLbl", inputType = InputType.TEXT, defaultValue = "0", isRequired = false) @Getter @Setter
    private BlockParameterPlain time;
    @Parameter(label = "chartPriceLbl", inputType = InputType.NUMBER, defaultValue = "0", isRequired = false) @Getter @Setter
    private BlockParameterPlain price;
    @Parameter(label = "objTypeLbl", inputType = InputType.NUMBER) @Getter @Setter
    private BlockParameterPlain type;

    @Override
    public String template() {
        return "createObject(ChartID(), ${this.id}, 0, ${this.time}, ${this.price}, ${this.type});";
    }
}
