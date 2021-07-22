package com.tradeblocks.app.renderengine.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;

import com.tradeblocks.app.renderengine.base.util.TemplateUtils;
import org.junit.jupiter.api.Test;

public class TemplateUtilsTest {
  TemplateUtils templateUtils = new TemplateUtils();

  @Test
  public final void testGetRenderedStringMap() {
    Map<String, String> feed = new HashMap<>();
    feed.put("name", "ABC");
    feed.put("surname", "EFG");
    
    final String templateString = "The name is $this.name and surname is $this.surname";
    
    String result = templateUtils.getRendered(templateString, feed);
    
    assertEquals("The name is ABC and surname is EFG", result, "Rendering is not working properly");
  }
  
  public static void main(String args[]) {
    Map<?,?> feed = new HashMap<Object, Object>() {
      private static final long serialVersionUID = 6724442695939808201L;
    { put("condition","1+1=1"); }};
    
    System.out.println(new TemplateUtils().getRendered("if(${condition}) { }", feed));
  }

}
