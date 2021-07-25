package com.tradeblocks.app.renderengine.base.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Should be used on a class that represents an active engine, the first argument
 * is the name of engine, it will identify this engine.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Engine {
  String value();
}
