package com.toy.ast;

public class Function {
	protected ParameterList parameters;
	protected BlockStmnt body;
	protected Environment env;
	
	public Function(ParameterList parameters2, BlockStmnt body2,
		Environment env2) {
		this.parameters = parameters2;
		this.body = body2;
		this.env = env2;	
	}
	public ParameterList parameters(){
		return parameters;
	}
	public BlockStmnt block(){
		return body;
	}
	public Environment makeEnv(){
		return new NestedEnv(env);
	}
	@Override
	public String toString(){
		return "<fun:"+hashCode() + ">";
	}
}
