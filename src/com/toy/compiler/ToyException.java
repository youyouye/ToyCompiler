package com.toy.compiler;

import java.io.IOException;

import com.toy.ast.ASTree;

public class ToyException extends RuntimeException{
	public ToyException(String m){super(m);}
	public ToyException(Token t){
		this("",t);
	}
	public ToyException(String msg,Token t){
		super("syntax error arround" + location(t)+ "."+msg);
	}
	public ToyException(String m,ASTree t){
		super(m + "" + t.location());
	}
	public static String location(Token t){
		if(t == Token.EOF)
			return "the last line";
		else
			return "\"" + t.getText() + "\" + at line " + t.getLineNumber();
	}
	public ToyException(IOException e){
		super(e);
	}
}
