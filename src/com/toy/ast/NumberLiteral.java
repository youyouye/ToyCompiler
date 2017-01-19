package com.toy.ast;

import com.toy.compiler.Token;
import com.vm.Code;
import static com.vm.Opcode.*;

public class NumberLiteral extends ASTLeaf{

	public NumberLiteral(Token t) {
		super(t);
	}
	public int value(){return token().getNumber();}
	public Object eval(Environment env){
		return value();
	}
	public void compile(Code c){
		int v = value();
		if(Byte.MIN_VALUE <= v && v <= Byte.MAX_VALUE){
			c.add(BCONST);
			c.add((byte)v);
		}else{
			c.add(ICONST);
			c.add(v);
		}
		c.add(encodeRegister(c.nextReg++));
	}
	public TypeInfo typeCheck(TypeEnv tenv){
		return TypeInfo.INT;
	}
	public String translate(TypeInfo result){
		return Integer.toString(value());
	}
}
