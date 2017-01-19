package com.toy.compiler;

public abstract class Token {
	public static final Token EOF = new Token(-1){};
	public static final String EOL = "\\n";
	
	private int lineNumber;
	protected Token(int line){
		lineNumber = line;
	}
	public int getLineNumber(){return lineNumber;}
	public boolean isIdentifier(){return false;}
	public boolean isNumber(){return false;}
	public boolean isString(){return false;}
	/*
	 * @抽象方法而已，为了让继承它的类实现特定的方法。
	 * */
	public int getNumber(){
		throw new ToyException("没有实例化的问题，无法读取") ;
	}
	public String getText(){return "";}
}
