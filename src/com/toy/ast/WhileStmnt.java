package com.toy.ast;

import java.util.List;

import com.toy.compiler.TypeException;
import com.vm.Code;

import static com.vm.Opcode.*;

public class WhileStmnt extends ASTList{

	public WhileStmnt(List<ASTree> list) {
		super(list);
	}
	public ASTree condition(){ return child(0);}
	public ASTree body(){
		return child(1);
	}
	public String toString(){
		return "(while" + condition() + " " + body() + ")";
	}
	public Object eval(Environment env){
		Object result = 0;
		for(;;){
			Object c = condition().eval(env);
			if(c instanceof Integer && ((Integer)c).intValue() == 0){
				return result;
			}else{
				result = body().eval(env);
			}
		}
	}
	public void compile(Code c){
		int oldReg = c.nextReg;
		c.add(BCONST);
		c.add((byte)0);
		c.add(encodeRegister(c.nextReg++));
		int pos = c.position();
		condition().compile(c);
		int pos2 = c.position();
		c.add(IFZERO);
		c.add(encodeRegister(--c.nextReg));
		c.add(encodeShortOffset(0));
		c.nextReg = oldReg;
		body().compile(c);
		int pos3 = c.position();
		c.add(GOTO);
		c.add(encodeShortOffset(pos-pos3));
		c.set(encodeShortOffset(c.position()-pos2), pos2+2);
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		TypeInfo condType = condition().typeCheck(tenv);
		condType.assertSubtypeOf(TypeInfo.INT, tenv, this);
		TypeInfo bodyType = body().typeCheck(tenv);
		return bodyType.union(TypeInfo.INT, tenv);
	}
}
