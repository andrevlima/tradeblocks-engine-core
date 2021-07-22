package com.tradeblocks.app.renderengine.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.tradeblocks.app.renderengine.base.core.FastGetSet;
import org.junit.jupiter.api.Test;

public class FastGetSetTest {

  public static class StubClass {
    private String stuffy;

    public String getStuffy() {
      return stuffy;
    }

    public void setStuffy(String stuffy) {
      this.stuffy = stuffy;
    }
  }
  
  @Test
  public void testGetter() throws Exception {
    StubClass stubInstance = new StubClass();
    String stuffy = "Lol!";
    stubInstance.setStuffy(stuffy);
    
    String propertyName = "stuffy";
    FastGetSet.cacheGetter(StubClass.class, propertyName);
    String result = FastGetSet.<String, Object>get(stubInstance, propertyName);
    
    assertEquals(stuffy, result);
  }

  @Test
  public void testSetter() {
    StubClass stubInstace = new StubClass();
    String stuffy = "Anyway!";
    stubInstace.setStuffy(stuffy);
    
    String propertyName = "stuffy";
    FastGetSet.cacheSetter(StubClass.class, propertyName);
    String expected = "New content!";
    FastGetSet.set(stubInstace, propertyName, expected);
    
    assertEquals(stubInstace.getStuffy(), expected);
  }

  private void forceCleanCache() throws Exception {
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    
    Field cache = FastGetSet.class.getDeclaredField("getterByBlock");
    cache.setAccessible(true);
    modifiersField.setInt(cache, cache.getModifiers() & ~Modifier.FINAL);
    cache.set(null, new HashMap<>());
    
    cache = FastGetSet.class.getDeclaredField("setterByBlock");
    cache.setAccessible(true);
    modifiersField.setInt(cache, cache.getModifiers() & ~Modifier.FINAL);
    cache.set(null, new HashMap<>());
  }
  
  @Test
  public void testSafeGetter() throws Exception {
    forceCleanCache();
    
    StubClass stubInstace = new StubClass();
    String stuffy = "Anyway!";
    stubInstace.setStuffy(stuffy);
    
    String propertyName = "stuffy";
    String expected = "New content!";
    FastGetSet.setSafe(stubInstace, propertyName, expected);
    
    assertEquals(stubInstace.getStuffy(), expected);
  }

  @Test
  public void testSafeSetter() throws Exception {
    forceCleanCache();
    
    StubClass stubInstance = new StubClass();
    String stuffy = "Lol!";
    stubInstance.setStuffy(stuffy);
    
    String propertyName = "stuffy";
    FastGetSet.cacheGetter(StubClass.class, propertyName);
    String result = FastGetSet.<String, Object>getSafe(stubInstance, propertyName);
    
    assertEquals(stuffy, result);
  }
}
