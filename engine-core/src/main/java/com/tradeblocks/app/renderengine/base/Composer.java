package com.tradeblocks.app.renderengine.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeblocks.app.renderengine.base.core.MetaManager;
import com.tradeblocks.app.renderengine.base.core.MetaManager.FieldInfo;
import com.tradeblocks.app.renderengine.base.annotations.Connector;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.block.AbstractBlock;
import com.tradeblocks.app.renderengine.base.connector.BlockConnector;
import com.tradeblocks.app.renderengine.base.core.FastGetSet;
import com.tradeblocks.app.renderengine.base.engine.AbstractRenderEngine;
import com.tradeblocks.app.renderengine.base.block.parameter.BlockParameterPlain;
import com.tradeblocks.app.renderengine.base.util.DiscoverUtils;
import com.tradeblocks.app.renderengine.base.workflow.Workflow;

/**
 * Responsible for compose the final result of a Workflow.
 * This works discovering the engine, injecting data, preparing and linking blocks.
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public class Composer {
  private AbstractRenderEngine engine;
  private Workflow workflow;
  private JsonNode object;

  /**
   * Sets the engine target by name
   * @param target engine name
   * @return current instance
   */
  public Composer engineName(String target) {
    setEngine(DiscoverUtils.getEngineInstance(target));
    return this;
  }

  /**
   * @see #fromJSON(String)
   *
   * @param fileJson file location
   * @return current instance
   */
  public Composer fromJSON(File fileJson) {
    try {
      if(!fileJson.exists()) {
    	  throw new Error(fileJson.getAbsolutePath() + " doesn't exists");
      }
      String content = new String(Files.readAllBytes(Paths.get(fileJson.getAbsolutePath())));
      fromJSON(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this;
  }
  
  /**
   * Will search for an available Block concrete implementation by
   * a JsonNode
   * 
   * @param node Json node
   * @return Block instance
   */
  private AbstractBlock scanForNewBlockInstance(JsonNode node) {
    AbstractBlock block = null;
    
    String type = node.get("internalType").asText();
    
    try {
      block = getEngine().getIndexedAvailableBlocks().get(type).newInstance();
    } catch (Exception e) {
      System.err.println("Problems when scanning for: " + type);
      e.printStackTrace();
    }
    
    return block;
  }

  /**
   * Takes the json node of operators, goes through it and maps it to known blocks (all the fields in it included)
   *
   * @param operatorsNode json node of operators
   * @return a Map(list) of all blocks by Id
   */
  protected Map<String, AbstractBlock> processOperatorsFromJson(JsonNode operatorsNode) {
    final Map<String, AbstractBlock> blocks = new HashMap<>();
    Iterator<Entry<String, JsonNode>> opIterator = operatorsNode.fields();

    while(opIterator.hasNext()) {
      Entry<String, JsonNode> entry = opIterator.next();

      JsonNode blockNode = entry.getValue();

      AbstractBlock block = scanForNewBlockInstance(blockNode);

      Iterator<Entry<String, JsonNode>> parameters = blockNode.get("parameters").fields();

      // Block parameter list mapping
      while(parameters.hasNext()) {
        Entry<String, JsonNode> entryParameter = parameters.next();
        String parameterKey = entryParameter.getKey();
        JsonNode parameterNodeData = entryParameter.getValue();

        FieldInfo<Parameter> fieldInfo = MetaManager.getParametersByBlock(block.getClass()).get(parameterKey);

        if(fieldInfo == null) {
          System.err.println("Field @Parameter not implemented in: " + block.getClass() + ", field: " + parameterKey);
          break;
        }
        final String parameterValue = parameterNodeData.get("valueRendered") != null ? parameterNodeData.get("valueRendered").asText() : fieldInfo.getInfo().defaultValue();
        BlockParameterPlain blockParameter = new BlockParameterPlain(parameterKey, parameterValue);

        FastGetSet.setSafe(block, parameterKey, blockParameter);
      }

      if(DiscoverUtils.getStartblocks().contains(block.getClass())) {
        getWorkflow().getStartPoints().add(block);
      }

      blocks.put(entry.getKey(), block);
    }
    return blocks;
  }

  /**
   * Takes the node of links and processes it by linking all the blocks by its Ids
   *
   * @param linksNode json node of links
   * @param blocks list of blocks to be linked
   */
  protected void processLinkingFromJson(JsonNode linksNode, Map<String, AbstractBlock> blocks) {
    Iterator<Entry<String, JsonNode>> lkIterator = linksNode.fields();

    // Link mapping
    while(lkIterator.hasNext()) {
      Entry<String, JsonNode> entryLink = lkIterator.next();
      JsonNode linkDataNode = entryLink.getValue();

      String fromOperatorKey = linkDataNode.get("fromOperator").asText();
      String toOperatorKey = linkDataNode.get("toOperator").asText();
      String connectorKey = linkDataNode.get("fromConnector").asText();

      AbstractBlock block = blocks.get(fromOperatorKey);
      AbstractBlock targetBlock = blocks.get(toOperatorKey);
      //-------- Ponderate ---------
      if(!block.getOutConnectors().containsKey(connectorKey)) {
        block.getOutConnectors().put(connectorKey, new BlockConnector(block));
      }
      block.getOutConnectors().get(connectorKey).getDestinations().add(targetBlock);
      targetBlock.setParent(new BlockConnector(block, targetBlock));
      //----------------------------

      FieldInfo<Connector> fieldInfo = MetaManager.getConnectorsByBlock(block.getClass()).get(connectorKey);

      if(fieldInfo == null) {
        System.err.println("Field @Connector not implemented in: " + block.getClass() + ", field: " + connectorKey);
      }
      else if(fieldInfo.getInfo().multiple()) {
        List<BlockConnector> value = FastGetSet.<List<BlockConnector>, AbstractBlock>getSafe(block, connectorKey);
        if(value == null) {
          value = new ArrayList<>();
          FastGetSet.setSafe(block, connectorKey, value);
        }
        value.add(new BlockConnector(block, targetBlock));
      } else {
        FastGetSet.setSafe(block, connectorKey, new BlockConnector(block, targetBlock));
      }
    }
  }

  /**
   * Map the source json file to a known internal structure
   * 
   * @param json json string
   * @return current instance
   */
  public Composer fromJSON(String json) {
    try {
      setWorkflow(new Workflow());

      ObjectMapper mapper = new ObjectMapper();
      //mapper.enable(Feature.ALLOW_COMMENTS);
      object = mapper.readTree(json);
      
      JsonNode blocksNode = object.get("blocks");
      JsonNode operatorsNode = blocksNode.get("operators");
      JsonNode linksNode = blocksNode.get("links");

      final Map<String, AbstractBlock> blocks = processOperatorsFromJson(operatorsNode);

      processLinkingFromJson(linksNode, blocks);
      
      getWorkflow().getBlocks().addAll(blocks.values());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return this;
  }

  /**
   * @return current workflow
   */
  public Workflow getWorkflow() {
    return workflow;
  }

  /**
   * Sets current workflow
   * @param workflow the workflow to be set
   */
  public void setWorkflow(Workflow workflow) {
    this.workflow = workflow;
  }

  /**
   * @return the engine
   */
  public AbstractRenderEngine getEngine() {
    return engine;
  }

  /**
   * @param engine the engine to set
   */
  public void setEngine(AbstractRenderEngine engine) {
    this.engine = engine;
  }

  /**
   * Adds its current workflow into the engine workflow list
   *
   * @return current instance
   */
  public Composer injectWorkflow() {
    this.getEngine().getWorkflows().add(getWorkflow());
    return this;
  }
}
