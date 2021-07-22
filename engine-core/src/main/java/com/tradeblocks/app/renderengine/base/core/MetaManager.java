package com.tradeblocks.app.renderengine.base.core;

import java.lang.reflect.Field;
import java.util.*;

import com.tradeblocks.app.renderengine.base.annotations.Connector;
import com.tradeblocks.app.renderengine.base.annotations.Parameter;
import com.tradeblocks.app.renderengine.base.block.AbstractBlock;
import com.tradeblocks.app.renderengine.base.util.DiscoverUtils;

/**
 * For meta programming purposes with great performance
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public class MetaManager {
  private final static Map<Class<AbstractBlock>, Map<String, FieldInfo<Connector>>> connectorsByBlock = new HashMap<>();
  private final static Map<Class<AbstractBlock>, Map<String, FieldInfo<Parameter>>> parametersByBlock = new HashMap<>();

  /**
   * Holds information about a field of a class
   * 
   * @param <T>
   */
  public static class FieldInfo<T> {
    private Field field;
    private T info;

    public FieldInfo(Field field, T info) {
      this.setField(field);
      this.setInfo(info);
    }

    public Field getField() {
      return field;
    }

    public void setField(Field field) {
      this.field = field;
    }

    public T getInfo() {
      return info;
    }

    public void setInfo(T info) {
      this.info = info;
    }
  }

  /**
   * Retrieve all fields from a given class, including those inherited ones.
   *
   * @param aClass given class
   * @return list of fields of a class
   */
  protected static List<Field> getAllModelFields(Class aClass) {
    List<Field> fields = new ArrayList<>();
    do {
      Collections.addAll(fields, aClass.getDeclaredFields());
      aClass = aClass.getSuperclass();
    } while (aClass != null);
    return fields;
  }

  /**
   * This method goes through the available implementations of Block and cache all connectors and parameters
   */
  public static void loadCacheData() {
    for(Class<AbstractBlock> block : DiscoverUtils.getBlocks()) {
      System.err.println("\tLoading class: " + block.getName());
      
      HashMap<String, FieldInfo<Connector>> connectors = new HashMap<>();
      connectorsByBlock.put(block, connectors);
      HashMap<String, FieldInfo<Parameter>> parameters = new HashMap<>();
      parametersByBlock.put(block, parameters);
      
      for(Field field : getAllModelFields(block)) {
        if(field.isAnnotationPresent(Connector.class)) {
          System.err.println("\t\tLoading connector: " + field.getName());
          connectors.put(field.getName(), new FieldInfo<Connector>(field, field.getAnnotation(Connector.class)));
        } else if(field.isAnnotationPresent(Parameter.class)) {
          System.err.println("\t\tLoading field: " + field.getName());
          parameters.put(field.getName(), new FieldInfo<Parameter>(field, field.getAnnotation(Parameter.class)));
        }
      }
    }
  }

  /**
   * Given a block class it retrieves the parameters map where key is the name and value is the field control
   *
   * @param targetClass
   * @return map of parameters from a given block
   */
  public static Map<String, FieldInfo<Parameter>> getParametersByBlock(Class<? extends AbstractBlock> targetClass) {
    return parametersByBlock.get(targetClass);
  }

  /**
   * Given a block class it retrieves the parameters map where key is the name and value is the field control
   *
   * @param targetClass
   * @return map of connectors from a given block
   */
  public static Map<String, FieldInfo<Connector>> getConnectorsByBlock(Class<? extends AbstractBlock> targetClass) {
    return connectorsByBlock.get(targetClass);
  }


  // First load on startup
  static {
    System.err.println("MetaManager starting...");
    loadCacheData();
    System.err.println("MetaManager finished!");
  }
}
