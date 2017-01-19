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
	 * @���󷽷����ѣ�Ϊ���ü̳�������ʵ���ض��ķ�����
	 * */
	public int getNumber(){
		throw new ToyException("û��ʵ���������⣬�޷���ȡ") ;
	}
	public String getText(){return "";}
}
