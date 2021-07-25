package com.tradeblocks.app.renderengine.base.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.tradeblocks.app.renderengine.base.annotations.Block;
import com.tradeblocks.app.renderengine.base.annotations.Engine;
import com.tradeblocks.app.renderengine.base.block.AbstractBlock;
import com.tradeblocks.app.renderengine.base.engine.AbstractEngine;
import com.tradeblocks.app.renderengine.base.engine.AbstractRenderEngine;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

/**
 * Useful utilities to manage engines
 *
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public class DiscoverUtils {
  static {
    System.err.println("RenderEngineUtils is starting...");
  }

  //Utility class
  private DiscoverUtils() { }

  private static final ScanResult fullResult = new ClassGraph().enableAllInfo().scan();

  /**
   * Cache all blocks implemented
   */
  @SuppressWarnings("unchecked")
  private static final List<Class<AbstractBlock>> blocks =
          fullResult.getSubclasses(AbstractBlock.class.getName()).stream()
            .map(block -> (Class<AbstractBlock>) block.loadClass())
            .filter(block -> block.isAnnotationPresent(Block.class))
            .collect(Collectors.toList());

  private static Map<String, Map<String, Class<AbstractBlock>>> resultsIndexedByEngine = new HashMap<>();

  /**
   * Index all engines by {@link Engine} annotation and cache it.
   */
  @SuppressWarnings("unchecked")
  private static Map<String, Class<AbstractRenderEngine>> engines =
      fullResult.getSubclasses(AbstractRenderEngine.class.getName())
        .stream()
        .collect(Collectors.toMap((clazz) -> {
           return clazz.loadClass().getAnnotation(Engine.class).value();
        }, (clazz) -> {
          return (Class<AbstractRenderEngine>) clazz.loadClass();
        }));

  /**
   * Blocks annotated with {@link Block#isStaringPoint()} and is starting point
   */
  private static final Set<Class<?>> startBlocks = new HashSet<>();

  /**
   * Discover all blocks and index them by class engine
   */
  public static void loadAllBlocksByEngine() {
    getBlocks().stream().forEach((block) -> {
      Block blockAnnotation = block.getAnnotation(Block.class);
      String[] engineNames = blockAnnotation.compatibleWith();

      if(blockAnnotation.isStaringPoint()) {
        getStartblocks().add(block);
      }

      /* // Parallel Stream Approach
      for(String engineName : engineNames) {
        Map<String, Class<AbstractBlock>> compatibleWith = getBlocks().parallelStream().collect(Collectors.toMap(Class::getSimpleName, Function.identity()));
        resultsIndexedByEngine.put(engineName, compatibleWith);
      }
      */

      for(String engineName : engineNames) {
        if(resultsIndexedByEngine.containsKey(engineName)) {
          Map<String, Class<AbstractBlock>> value = resultsIndexedByEngine.get(engineName);

          value.put(block.getSimpleName(), block);
        } else {
          Map<String, Class<AbstractBlock>> value = new HashMap<>();
          value.put(block.getSimpleName(), block);

          resultsIndexedByEngine.put(engineName, value);
        }
      }
    });
  }

  /*
   * Runs initial needed caching
   */
  static {
	System.err.println("\tBlock discovering started...");
    loadAllBlocksByEngine();
    System.err.println("\tBlock discovering is finished!");
  }


  /**
   * Get all blocks available for a engine implementation
   *
   * @param renderEngineClazz engine class
   * @return map of blocks indexed by class name
   */
  public static Map<String, Class<AbstractBlock>> getBlocksCompatibleWith(final Class<? extends AbstractEngine> renderEngineClazz) {
    String targetEngine = renderEngineClazz.getAnnotation(Engine.class).value();
    return resultsIndexedByEngine.get(targetEngine);
  }

  /**
   * Get an engine by name expressed by {@link Engine} annotation
   * @param name of engine
   * @return engine instance
   */
  public static AbstractRenderEngine getEngineInstance(final String name) {
    try {
      return engines.get(name).newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * See all discovered blocks available
   *
   * @return all blocks
   */
  public static List<Class<AbstractBlock>> getBlocks() {
    return blocks;
  }

  /**
   * @return the startblocks
   */
  public static Set<Class<?>> getStartblocks() {
    return startBlocks;
  }

  static {
    System.err.println("RenderEngineUtils finished pre caching cycles");
  }
}
