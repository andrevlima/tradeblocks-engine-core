package com.tradeblocks.app.renderengine.base.connector;

import com.tradeblocks.app.renderengine.base.block.AbstractBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConnector {
  private String label;
  private AbstractBlock origin;
  private List<AbstractBlock> destinations = new ArrayList<>();
  
  public BlockConnector(AbstractBlock origin) {
    this(origin, null);
  }
  
  public BlockConnector(AbstractBlock origin, AbstractBlock destination) {
    this.setLabel(label);
    this.setOrigin(origin);
    this.setDestination(destination);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
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

  public AbstractBlock getOrigin() {
    return origin;
  }

  public void setOrigin(AbstractBlock origin) {
    this.origin = origin;
  }

  public List<AbstractBlock> getDestinations() {
    return destinations;
  }

  public void setDestinations(List<AbstractBlock> destinations) {
    this.destinations = destinations;
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
