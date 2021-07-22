package com.tradeblocks.app.renderengine.base.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;

import com.tradeblocks.app.renderengine.base.parameter.InputType;
import com.tradeblocks.app.renderengine.base.parameter.SymbolType;


/**
 * Basic block parameter definition, each one of this is used to be part of a {@link AbstractBlock}
 * 
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 *
 */
@Retention(RUNTIME)
public @interface Parameter {
  String label(); 
  InputType inputType(); 
  String defaultValue() default "";
  boolean isRequired() default true;
  SymbolType inputSymbolType() default SymbolType.ANY;
}
