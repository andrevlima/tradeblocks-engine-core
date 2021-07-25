package com.tradeblocks.app.renderengine.base;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import com.google.googlejavaformat.java.Formatter;
import com.tradeblocks.app.renderengine.implementation.mql5.MockEngine;

public class ComposerTest {

  @Test
  public final void testFromJSON() throws Exception {
    File file = new File(ComposerTest.class.getClassLoader().getResource("simple_flow.json").toURI());
    String input = new String(Files.readAllBytes(Paths.get(file.toURI())));
    
    File resultFile = new File(ComposerTest.class.getClassLoader().getResource("simple_flow.result.txt").toURI());
    String expected = new String(Files.readAllBytes(Paths.get(resultFile.toURI())));
        
    long startTime = System.nanoTime();
    
    Composer composer = new Composer()
        .engineName("mql5")
        .fromJSON(input);
    
    System.err.println("DURATION: " +  (System.nanoTime() - startTime) / 1_000_000);
    
    String produced = ((MockEngine) composer.injectWorkflow().getEngine()).renderContent();
    System.err.println("OUTPUT:\n\n" + new Formatter().formatSource("class Test{" + produced + "}") + "\n\n");
    
    System.err.println("DURATION N2: " +  (System.nanoTime() - startTime) / 1_000_000);

    assertEquals(cleanStr(produced), (cleanStr(expected)), "Output from the rendering is not the same as expected");
  }
  
  private String cleanStr(String str) {
    return StringUtils.normalizeSpace(str).replace(" ", "");
  }
}
