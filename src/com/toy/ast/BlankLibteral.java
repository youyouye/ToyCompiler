package com.toy.ast;

import com.toy.compiler.Token;

public class BlankLibteral extends ASTLeaf{

	public BlankLibteral(Token t) {
		super(t);
	}
	public String name(){
		return token().getText();
	}
}
