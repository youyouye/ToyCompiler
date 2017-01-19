package com.toy.ast;

import java.util.List;

import com.toy.compiler.TypeException;
import com.vm.Code;

import static com.vm.Opcode.*;

public class IfStmnt extends ASTList{

	public IfStmnt(List<ASTree> list) {
		super(list);
	}
	public ASTree condition(){
		return child(0);
	}
	public ASTree thenBlock(){
		return child(1);
	}
	public ASTree elseBlock(){
		return numChildren() > 2 ? child(2) : null;
	}
	public String toString(){
		return "(if" + condition() + " " + thenBlock() + " else " + elseBlock() + ")";
	}
	public Object eval(Environment env){
		Object c = condition().eval(env);
		if(c instanceof Integer && ((Integer) c).intValue() > 0){
			return thenBlock().eval(env);
		}else{
			ASTree b = elseBlock();
			if(b == null){
				return 0;
			}else{
				return b.eval(env);
			}
		}
	}
	public void compile(Code c){
		condition().compile(c);
		int pos = c.position();
		c.add(IFZERO);
		c.add(encodeRegister(--c.nextReg));
		c.add(encodeShortOffset(0));
		int oldReg = c.nextReg;
		thenBlock().compile(c);
		int pos2 = c.position();
		c.add(GOTO);
		c.add(encodeShortOffset(0));
		c.set(encodeShortOffset(c.position()-pos), pos+2);
		ASTree b = elseBlock();
		c.nextReg = oldReg;
		if(b != null){
			b.compile(c);
		}else{
			c.add(BCONST);
			c.add((byte)0);
			c.add(encodeRegister(c.nextReg++));
		}
		c.set(encodeShortOffset(c.position()-pos2), pos2+1);
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		TypeInfo condType = condition().typeCheck(tenv);
		condType.assertSubtypeOf(TypeInfo.INT, tenv, this);
		TypeInfo thenType = thenBlock().typeCheck(tenv);
		TypeInfo elseType;
		ASTree elseBk = elseBlock();
		if(elseBk == null){
			elseType = TypeInfo.INT;
		}else{
			elseType = elseBk.typeCheck(tenv);
		}
		return thenType.union(elseType, tenv);
	}
}
