package com.tradeblocks.app.renderengine.base.block;

/**
 * Most basic definition of a Block
 * @author Andre Vinicius Lima <andrelimamail@gmail.com>
 */
public interface Block {
	public <T> T perform();
}
