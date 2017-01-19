package com.toy.ast;

import com.toy.compiler.ToyException;

public class ClassInfo {
	protected ClassStmnt definition;
	protected Environment environment;
	protected ClassInfo superClass;
	public ClassInfo(ClassStmnt cs,Environment env){
		definition = cs;
		environment = env;
		Object obj = env.get(cs.superClass());
		if(obj == null){
			superClass = null;
		}else if(obj instanceof ClassInfo){
			superClass = (ClassInfo) obj;
		}else{
			throw new ToyException("unknown super class:"+ cs.superClass());
		}
	}
	public String name(){
		return definition.name();
	}
	public ClassInfo superClass(){
		return superClass;
	}
	public ClassBody body(){
		return definition.body();
	}
	public Environment environment(){
		return environment;
	}
	@Override
	public String toString(){
		return "<class" + name() + ">";
	}
}
