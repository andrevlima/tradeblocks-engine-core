package com.tradeblocks.app.renderengine.base.core;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Used in blocks mapping and dependency injection 
 * <br><br>
 * The purpose of this class is to provide a simple 
 * way of work with reflection above classes with
 * some improvements in performance using caching and
 * LambdaFactory and MethodHandlers 
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FastGetSet {
  private static final Map<Class<?>, Map<String, BiConsumer>> setterByBlock = new HashMap<>();
  private static final Map<Class<?>, Map<String, Function>> getterByBlock = new HashMap<>();
  private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
  
  /**
   * Gets a property of an object
   * 
   * @param instance
   * @param propertyName
   * @return
   */
  public static final <T, U> T get(U instance, String propertyName) {
    final Function getter = getterByBlock.get(instance.getClass()).get(propertyName);
    return (T) getter.apply(instance);
  }

  /**
   * Sets a property of an object
   * 
   * @param instance
   * @param propertyName
   * @param value
   */
  public static final <T, U> void set(U instance, String propertyName, T value) {
    final BiConsumer setter = setterByBlock.get(instance.getClass()).get(propertyName);
    setter.accept(instance, value);
  }
  
  /**
   * Gets a property of an object but caring
   * about if it already have a reference cached
   * 
   * @param instance
   * @param propertyName
   * @return
   */
  public static final <T, U> T getSafe(U instance, String propertyName) {
    final Class<?> clazz = instance.getClass();
    Map<String, Function> fields = getterByBlock.get(clazz);
    
    if(fields == null) {
      cacheGetter(clazz, propertyName);
      fields = getterByBlock.get(clazz);
    }
    Function getter = fields.get(propertyName);
    
    if(getter == null) { 
      cacheGetter(clazz, propertyName);
      getter = fields.get(propertyName);
    }
    
    return (T) getter.apply(instance);
  }
  
  /**
   * Sets a property of an object but caring
   * about if it already have a reference cached
   * 
   * @param instance
   * @param propertyName
   * @param value
   */
  public static final <T, U> void setSafe(U instance, String propertyName, T value) {
    final Class<?> clazz = instance.getClass();
    Map<String, BiConsumer> fields = setterByBlock.get(clazz);
    
    if(fields == null) {
      cacheSetter(clazz, propertyName);
      fields = setterByBlock.get(clazz);
    }
    BiConsumer setter = fields.get(propertyName);
    
    if(setter == null) { 
      cacheSetter(clazz, propertyName);
      setter = fields.get(propertyName);
    }
    
    setter.accept(instance, value);
  }  

  /**
   * Do cache of all needed references to get
   * a property indicated
   * 
   * @param clazz
   * @param propertyName
   */
  public static final <T> void cacheGetter(Class<T> clazz, String propertyName) {
    if(!getterByBlock.containsKey(clazz)) {
      getterByBlock.put(clazz, new HashMap<>());
    }
    
    try {
      final PropertyDescriptor property = getProperty(clazz, propertyName);
      getterByBlock.get(clazz).put(propertyName, createGetter(lookup, lookup.unreflect(property.getReadMethod())));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Do cache of all needed references to set
   * a property indicated
   * 
   * @param clazz
   * @param propertyName
   */
  public static final <T> void cacheSetter(Class<T> clazz, String propertyName) {
    if(!setterByBlock.containsKey(clazz)) {
      setterByBlock.put(clazz, new HashMap<>());
    }
    
    try {
      final PropertyDescriptor property = getProperty(clazz, propertyName);
      setterByBlock.get(clazz).put(propertyName, createSetter(lookup, lookup.unreflect(property.getWriteMethod())));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Provides a shortcut to identify and
   * get informations about an specific field
   * (like if it has a getter and setter)
   * 
   * @param clazz
   * @param propertyName
   * @return
   * @throws IntrospectionException
   */
  protected static PropertyDescriptor getProperty(Class<?> clazz, String propertyName) throws IntrospectionException {
    BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
    final Function<String, PropertyDescriptor> propertyFinder =
        name -> Stream.of(beanInfo.getPropertyDescriptors()).filter(p -> name.equals(p.getName()))
            .findFirst().orElseThrow(() -> new IllegalStateException("Not found (Getter/Setter Bean Pair): <" + name + "> in <" + clazz.getCanonicalName() + ">"));
    PropertyDescriptor discoveredProperty = propertyFinder.apply(propertyName);
    return discoveredProperty;
  }
  
  /**
   * Use the techniques of LambdaFactory to build a getter access
   * 
   * @param lookup
   * @param getter
   * @return
   * @throws Exception
   */
  private static Function createGetter(final MethodHandles.Lookup lookup, final MethodHandle getter) throws Exception {
    final CallSite site = LambdaMetafactory.metafactory(lookup, "apply",
        MethodType.methodType(Function.class), MethodType.methodType(Object.class, Object.class), 
        getter, getter.type()); // actual signature of getter
    try {
      return (Function) site.getTarget().invokeExact();
    } catch (final Exception e) {
      throw e;
    } catch (final Throwable e) {
      throw new Error(e);
    }
  }

  /**
   * Use the techniques of LambdaFactory to build a setter access
   * 
   * @param lookup
   * @param setter
   * @return
   * @throws Exception
   */
  private static BiConsumer createSetter(final MethodHandles.Lookup lookup, final MethodHandle setter) throws Exception {
    final CallSite site =
        LambdaMetafactory.metafactory(lookup, "accept", MethodType.methodType(BiConsumer.class),
            MethodType.methodType(void.class, Object.class, Object.class), 
            setter, setter.type()); // actual signature of setter
    try {
      return (BiConsumer) site.getTarget().invokeExact();
    } catch (final Exception e) {
      throw e;
    } catch (final Throwable e) {
      throw new Error(e);
    }
  }
  
  //Utility class
  private FastGetSet() { }
}
