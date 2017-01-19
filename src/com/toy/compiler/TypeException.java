package com.toy.compiler;

import com.toy.ast.ASTree;

public class TypeException extends Exception{
	public TypeException(String msg,ASTree t){
		super(msg + " " + t.location());
	}
}
