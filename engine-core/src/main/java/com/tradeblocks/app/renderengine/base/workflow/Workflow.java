package com.tradeblocks.app.renderengine.base.workflow;

import com.tradeblocks.app.renderengine.base.block.AbstractBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Workflow {
  private List<AbstractBlock> blocks = new ArrayList<>();
  private Set<AbstractBlock> startPoints = new HashSet<>();
  

  /**
   * @return the blocks
   */
  public List<AbstractBlock> getBlocks() {
    return blocks;
  }

  /**
   * @param blocks the blocks to set
   */
  public void setBlocks(List<AbstractBlock> blocks) {
    this.blocks = blocks;
  }

  /**
   * @return the startPoints
   */
  public Set<AbstractBlock> getStartPoints() {
    return startPoints;
  }

  /**
   * @param startPoints the startPoints to set
   */
  public void setStartPoints(Set<AbstractBlock> startPoints) {
    this.startPoints = startPoints;
  }
  
}
