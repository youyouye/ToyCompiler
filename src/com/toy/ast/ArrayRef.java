package com.toy.ast;

import java.util.List;

import com.toy.compiler.TypeException;

public class ArrayRef extends Protfix{

	public ArrayRef(List<ASTree> list) {
		super(list);
	}
	public ASTree index(){
		return child(1);
	}
	public String toString(){
		return child(0)+"["+index()+"]";
	}
	@Override
	public TypeInfo typeCheck(TypeEnv tenv, TypeInfo target)
			throws TypeException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
