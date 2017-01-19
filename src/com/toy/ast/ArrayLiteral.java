package com.toy.ast;

import java.util.List;

public class ArrayLiteral extends ASTList{

	public ArrayLiteral(List<ASTree> list) {
		super(list);
	}
	public int size(){
		return numChildren();
	}
	public Object eval(Environment env){
		int s = numChildren();
		Object[] res = new Object[s];
		int i = 0;
		for(ASTree t : children){
			res[i++] = t.eval(env);
		}
		return res;
	}
}
