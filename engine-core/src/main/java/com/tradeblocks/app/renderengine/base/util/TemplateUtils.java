package com.tradeblocks.app.renderengine.base.util;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.common.io.Files;

/**
 * Provides text template rendering shortcut mechanisms 
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
public class TemplateUtils {
  public static String getRendered(String templateString, Map feed) {
    return getRendered(templateString, feed, "A horse with no name...");
  }
  
  public static String getRendered(String templateString, Object feed, String templateName) {
    VelocityContext context = new VelocityContext();
    context.put("this", feed);
    StringWriter output = new StringWriter();

    Velocity.evaluate(context, output, templateName, templateString);
         
    return output.toString();
  }
}
