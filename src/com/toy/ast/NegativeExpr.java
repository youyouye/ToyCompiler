package com.toy.ast;

import java.util.List;

import com.toy.compiler.ToyException;
import com.toy.compiler.TypeException;
import com.vm.Code;

import static com.vm.Opcode.*;

public class NegativeExpr extends ASTList{

	public NegativeExpr(List<ASTree> list) {
		super(list);
	}
	public ASTree operand(){
		return child(0);
	}
	public String toString(){
		return "-"+operand();
	}
	public Object eval(Environment env){
		Object v = operand().eval(env);
		if(v instanceof Integer){
			return new Integer(-(Integer)(v)).intValue();
		}else{
			throw new ToyException("bad type for - ",this);
		}
	}
	public void compile(Code c){
		operand().compile(c);
		c.add(NEG);
		c.add(encodeRegister(c.nextReg-1));
		
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		TypeInfo t = operand().typeCheck(tenv);
		t.assertSubtypeOf(TypeInfo.INT, tenv, this);
		return TypeInfo.INT;
	}
	
}
