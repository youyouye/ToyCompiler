package com.toy.ast;

import java.util.List;

public class Fun extends ASTList{

	protected int size = -1;
	
	public Fun(List<ASTree> list) {
		super(list);
	}
	public ParameterList parameters(){
		return (ParameterList) child(0);
	}
	public BlockStmnt body(){
		return (BlockStmnt) child(1);
	}
	public String toString(){
		return "(fun" + parameters() + " " + body() + ")";
	}
	public Object eval(Environment env){
		return new Function(parameters(), body(), env);
	}
	
	public void lookup(Symbols syms){
		size = lookup(syms, parameters(), body());
	}
	public static int lookup(Symbols syms,ParameterList params,BlockStmnt body){
		Symbols newSyms = new Symbols(syms);
		params.lookup(newSyms);
		body.lookup(newSyms);
		return newSyms.size();
	}
}
