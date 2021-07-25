package com.tradeblocks.app.renderengine.implementation.mql5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.common.io.Files;
import com.tradeblocks.app.renderengine.base.block.Block;
import com.tradeblocks.app.renderengine.base.engine.AbstractRenderEngine;
import com.tradeblocks.app.renderengine.base.annotations.Engine;

@Engine("mql5")
public class MockEngine extends AbstractRenderEngine {
  
  public String renderContent() {
    StringBuilder result = new StringBuilder();
    
    for(Block block : getWorkflows().get(0).getStartPoints()) {
      result.append(block.perform().toString());
    }
    
    return result.toString();
  }
  
  @Override
  public File build() throws IOException {
    File file = Paths.get("test-file.mql").toFile();
    Files.write(renderContent().getBytes(), file);
    return file;
  }
}
