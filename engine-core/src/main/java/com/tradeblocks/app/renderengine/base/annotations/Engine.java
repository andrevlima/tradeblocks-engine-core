package com.tradeblocks.app.renderengine.base.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Engine {
  String value();
}
