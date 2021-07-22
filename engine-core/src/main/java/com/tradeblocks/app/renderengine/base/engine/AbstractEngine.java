package com.tradeblocks.app.renderengine.base.engine;

import com.tradeblocks.app.renderengine.base.util.DiscoverUtils;
import com.tradeblocks.app.renderengine.base.workflow.Workflow;
import com.tradeblocks.app.renderengine.base.block.AbstractBlock;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Runs above a set of workflows and "do work"
 * <br>
 * All engines must extend this abstract implementation
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public abstract class AbstractEngine {
  private List<Workflow> workflows = new LinkedList<>();

  public Collection<Class<AbstractBlock>> getAvailableBlocks() {
    return DiscoverUtils.getBlocksCompatibleWith(this.getClass()).values();
  }

  public Map<String, Class<AbstractBlock>> getIndexedAvailableBlocks() {
    return DiscoverUtils.getBlocksCompatibleWith(this.getClass());
  }

  /**
   * @return the workflows
   */
  public List<Workflow> getWorkflows() {
    return workflows;
  }

  /**
   * @param workflows the workflows to set
   */
  public void setWorkflows(List<Workflow> workflows) {
    this.workflows = workflows;
  }
}
