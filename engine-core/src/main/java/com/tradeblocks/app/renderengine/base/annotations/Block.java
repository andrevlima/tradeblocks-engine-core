package com.tradeblocks.app.renderengine.base.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines a class that represents a Block
 * <code>
 *     @Block(compatibleWith = "node-js")
 *     class IfBlock extends AbstractBlock {
 *         //...
 *     }
 *
 * </code>
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Block {
  String[] compatibleWith();
  boolean isStaringPoint() default false;
}
