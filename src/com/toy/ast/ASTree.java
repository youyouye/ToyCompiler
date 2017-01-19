package com.toy.ast;

import java.util.Iterator;

import com.toy.compiler.TypeException;
import com.vm.Code;

public abstract class ASTree implements Iterator<ASTree>{
	public abstract ASTree child(int i);
	public abstract int numChildren();
	public abstract Iterator<ASTree> children();
	public abstract String location();
	public Iterator<ASTree> iterator(){ return children();}
	public abstract Object eval(Environment env);
	public void lookup(Symbols syms) {
	}
	public void compile(Code c){}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		return null;
	}
	public String translate(TypeInfo result){
		return "";
	}
}
