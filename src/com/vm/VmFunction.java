package com.vm;

import com.toy.ast.BlockStmnt;
import com.toy.ast.Environment;
import com.toy.ast.Function;
import com.toy.ast.ParameterList;

public class VmFunction extends Function{
	protected int entry;
	
	public VmFunction(ParameterList parameters2, BlockStmnt body2,
			Environment env2,int entry) {
		super(parameters2, body2, env2);
		this.entry = entry;
	}
	public int entry(){
		return entry;
	}
}
