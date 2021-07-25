package com.tradeblocks.app.renderengine.base.connector;

import com.tradeblocks.app.renderengine.base.block.AbstractBlock;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the connection between 2 or more {@code Block} instances
 *
 * @see com.tradeblocks.app.renderengine.base.block.Block
 * */
public class BlockConnector {
  @Getter @Setter
  private String label;
  @Getter @Setter
  private AbstractBlock origin;
  @Getter @Setter
  private List<AbstractBlock> destinations = new ArrayList<>();
  
  public BlockConnector(AbstractBlock origin) {
    this(origin, null);
  }
  
  public BlockConnector(AbstractBlock origin, AbstractBlock destination) {
    this.setLabel(label);
    this.setOrigin(origin);
    this.setDestination(destination);
  }

  public AbstractBlock getDestination() {
    return getDestinations().get(0);
  }

  public void setDestination(AbstractBlock destination) {
    if(this.getDestinations().size() == 0) { 
      this.getDestinations().add(destination);
      return;
    }
    
    if(destination == null) {
      this.getDestinations().remove(0);
    } else {
      this.getDestinations().set(0, destination);      
    }
    
  }

  public AbstractBlock get() {
    return getDestination();
  }
  
  public boolean isConnected() {
    return !getDestinations().isEmpty();    
  }
  
  @Override
  public String toString() {
    return getDestinations().stream()
        .map((dest) -> dest.perform().toString())
        .collect(Collectors.joining("\n")) + "\n";
  }
}
