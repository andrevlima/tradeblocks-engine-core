/**
 * 
 */
package com.tradeblocks.app.renderengine.base.block;

import com.tradeblocks.app.renderengine.base.block.AbstractBlock;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Any block that can be ran
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public abstract class AbstractRunnableBlock extends AbstractBlock {

  /* (non-Javadoc)
   * @see com.tradeblocks.app.renderengine.base.block.Block#perform()
   */
  @Override
  public <T> T perform() {
    // TODO Auto-generated method stub
    throw new NotImplementedException();
  }

}
