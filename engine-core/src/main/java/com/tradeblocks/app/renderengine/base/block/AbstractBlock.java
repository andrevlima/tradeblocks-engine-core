package com.tradeblocks.app.renderengine.base.block;

import java.util.HashMap;
import java.util.Map;

import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.core.MetaManager;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;

/**
 * Block represents a peace of code in a flow
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public abstract class AbstractBlock implements Block {
  private Map<String, BlockConnector> outConnectors = new HashMap<>();
  private BlockConnector parent;
  
  public BlockConnector getParent() {
    return parent;
  }
  
  public void setParent(BlockConnector parent) {
    this.parent = parent;
  }

  public Map<String, BlockConnector> getOutConnectors() {
    return outConnectors;
  }

  /**
   * @return list of parameters of the class
   */
  public Map<String, MetaManager.FieldInfo<Parameter>> getParametersDefinition() {
    return MetaManager.getParametersByBlock(this.getClass());
  }
}
