package com.toy.ast;

import com.toy.compiler.ToyException;

public class JavaFunction extends Function{
	
	protected String className;
	protected Class<?> clazz;
	public JavaFunction() {
		super(null,null,null);
	}
	public static String className(String name){
		return "com.toy.fun." + name;
	}
	public Object invoke(Object[] args){
		try{
			return clazz.getDeclaredMethods()[0].invoke(null, args);
		}catch(Exception e){
			throw new ToyException(e.getMessage());
		}
	}
}
