package com.tradeblocks.app.renderengine.base.block;

import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.util.TemplateUtils;
import com.tradeblocks.app.renderengine.base.annotations.Connector;
import lombok.Getter;
import lombok.Setter;

/**
 * Any block that is a template to be rendered, must extend this
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public abstract class AbstractRenderableBlock extends AbstractBlock implements RenderableBlock {
  @Connector("next") @Getter @Setter
  protected BlockConnector next;

  @Override
  public String render() {
    return TemplateUtils.getRendered(template() + "\n$!{this.next}", this, this.getClass().getSimpleName());
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public String perform() {
    return render();
  }
  
  public abstract String template();
}
