package com.toy.ast;

public class OptMethod extends OptFunction{

	OptToyObject self;
	
	public OptMethod(ParameterList parameters, BlockStmnt body,
			Environment env, int memorySize,OptToyObject self) {
		super(parameters, body, env, memorySize);
		this.self = self;
	}
	@Override
	public Environment makeEnv(){
		ArrayEnv e = new ArrayEnv(size, env);
		e.put(0, 0,self);
		return e;
	}
}
