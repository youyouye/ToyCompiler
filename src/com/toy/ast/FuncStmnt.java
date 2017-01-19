package com.toy.ast;

import java.util.List;

import com.toy.ast.TypeInfo.FunctionType;
import com.toy.compiler.TypeException;


public class FuncStmnt extends ASTList{

	public FuncStmnt(List<ASTree> list) {
		super(list);
	}
	public String FucName(){
		return ((ASTLeaf)child(0)).token().getText();
	}
	public Protfix funArgument(){
		return (Protfix)child(1);
	}
	public String toString(){
		return "("+FucName()+funArgument()+")";
	}
	public Object eval(Environment env){
		Object res = child(0).eval(env);
		res = ((Protfix)child(1)).eval(env,res);
		return res;
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		TypeInfo.FunctionType type = (FunctionType) child(0).typeCheck(tenv);
		return type.returnType;
	}
}
