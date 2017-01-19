package com.toy.ast;

public class OptFunction extends Function{
	protected int size;

	public OptFunction(ParameterList parameters, BlockStmnt body,
			Environment env, int memorySize) {
		super(parameters, body, env);
		size = memorySize;
	}
	@Override
	public Environment makeEnv(){return new ArrayEnv(size, env);}
}
