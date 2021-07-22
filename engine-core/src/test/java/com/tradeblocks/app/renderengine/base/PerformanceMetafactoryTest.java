package com.tradeblocks.app.renderengine.base;

import com.tradeblocks.app.renderengine.base.core.FastGetSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Performance test used to decide between different approaches
 */
public class PerformanceMetafactoryTest {

  public String getCenas() {
    return cenas;
  }

  public void setCenas(String cenas) {
    this.cenas = cenas;
  }

  private String cenas;

  /**
   * This test is a performance test of the approaches of Reflection vs LambdaMetaFactory
   *
   * @throws Exception
   */
  @Test
  @Disabled("Just used for approach decision")
  public void runPerformanceTest() throws Exception {
    int j = 100_000_000;

    long startTime;
    long endTime;

    System.err.println("TIME SPENT:");
    // ------------------------------------------------------------------------------------------------
    PerformanceMetafactoryTest m = new PerformanceMetafactoryTest();

    startTime = System.nanoTime();

    for (int i = 0; i < j; i++) {
      m.setCenas("abcd");
    }

    endTime = System.nanoTime();
    long totalTimeSpentNative = (endTime - startTime) / 1000000;
    System.err.println("\tSET NATIVE: " + totalTimeSpentNative + "ms");
    // ------------------------------------------------------------------------------------------------

    m = new PerformanceMetafactoryTest();
    Field field = m.getClass().getDeclaredField("cenas");
    field.setAccessible(true);

    startTime = System.nanoTime();

    for (int i = 0; i < j; i++) {
      field.set(m, "abcd");
    }

    endTime = System.nanoTime();
    long totalTimeSpentReflection = (endTime - startTime) / 1000000;
    System.err.println("\tSET REFLECTION: " + totalTimeSpentReflection + "ms");

    // ------------------------------------------------------------------------------------------------
    final MethodHandles.Lookup lookup = MethodHandles.lookup();
    final BeanInfo beanInfo = Introspector.getBeanInfo(PerformanceMetafactoryTest.class);
    final Function<String, PropertyDescriptor> property =
        name ->
            Stream.of(beanInfo.getPropertyDescriptors())
                .filter(p -> name.equals(p.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found: " + name));
    PropertyDescriptor cenasProperty = property.apply("cenas");

    final Function getter =
        GettersSettersDzone.createGetter(lookup, lookup.unreflect(cenasProperty.getReadMethod()));

    final BiConsumer setter =
        GettersSettersDzone.createSetter(lookup, lookup.unreflect(cenasProperty.getWriteMethod()));

    startTime = System.nanoTime();

    for (int i = 0; i < j; i++) {
      // getter.apply(m);
      setter.accept(m, "abcd");
    }

    endTime = System.nanoTime();
    long totalTimeSpentLambdaFactory = (endTime - startTime) / 1000000;
    System.err.println("\tSET LAMBDA_FACTORY : " + totalTimeSpentLambdaFactory + "ms");
    // ------------------------------------------------------------------------------------------------

    FastGetSet.cacheSetter(PerformanceMetafactoryTest.class, "cenas");
    m = new PerformanceMetafactoryTest();
    startTime = System.nanoTime();

    for (int i = 0; i < j; i++) {
      FastGetSet.set(m, "cenas", "abcd");
    }

    endTime = System.nanoTime();
    long totalTimeFastGetSet = (endTime - startTime) / 1000000;
    System.err.println("\tSET FAST_GET_SET: " + totalTimeFastGetSet + "ms");

    Assertions.assertTrue(
        totalTimeSpentLambdaFactory < totalTimeSpentReflection,
        "Current implementation is slower than reflection");
  }

  /**
   * This class is a sample implementation of the Metafactory approach instead of using purely
   * Reflection
   */
  public static class GettersSettersDzone {
    static Function createGetter(
        final MethodHandles.Lookup lookup, final MethodHandle getter) throws Exception {
      return getFunction(lookup, getter);
    }

    static Function getFunction(MethodHandles.Lookup lookup, MethodHandle getter)
        throws Exception {
      final CallSite site =
          LambdaMetafactory.metafactory(
              lookup,
              "apply",
              MethodType.methodType(Function.class),
              MethodType.methodType(
                  Object.class,
                  Object.class), // signature of method Function.apply after type erasure
              getter,
              getter.type()); // actual signature of getter
      try {
        return (Function) site.getTarget().invokeExact();
      } catch (final Exception e) {
        throw e;
      } catch (final Throwable e) {
        throw new Error(e);
      }
    }

    static BiConsumer createSetter(
        final MethodHandles.Lookup lookup, final MethodHandle setter) throws Exception {
      return getBiConsumer(lookup, setter);
    }

    static BiConsumer getBiConsumer(MethodHandles.Lookup lookup, MethodHandle setter)
        throws Exception {
      final CallSite site =
          LambdaMetafactory.metafactory(
              lookup,
              "accept",
              MethodType.methodType(BiConsumer.class),
              MethodType.methodType(
                  void.class,
                  Object.class,
                  Object.class), // signature of method BiConsumer.accept after type erasure
              setter,
              setter.type()); // actual signature of setter
      try {
        return (BiConsumer) site.getTarget().invokeExact();
      } catch (final Exception e) {
        throw e;
      } catch (final Throwable e) {
        throw new Error(e);
      }
    }
  }
}
