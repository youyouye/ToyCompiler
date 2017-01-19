package com.toy.ast;

import java.util.List;

import com.toy.compiler.ToyException;
import com.toy.compiler.TypeException;

public abstract class Protfix extends ASTList{

	public Protfix(List<ASTree> list) {
		super(list);
	}

	public Object eval(Environment env, Object res) {
		return child(0).eval(env);
	}
	public abstract TypeInfo typeCheck(TypeEnv tenv,TypeInfo target) throws TypeException;
}
