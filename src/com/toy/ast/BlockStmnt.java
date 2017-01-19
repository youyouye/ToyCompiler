package com.toy.ast;

import java.util.List;

import com.toy.compiler.TypeException;
import com.vm.Code;

import static com.vm.Opcode.*;

public class BlockStmnt extends ASTList{
	
	protected TypeInfo type;
	
	public BlockStmnt(List<ASTree> list) {
		super(list);
	}
	public Object eval(Environment env){
		Object result = 0;
		for(ASTree t : children){
			if(!(t instanceof NullStmnt)){
				result = t.eval(env);
			}
		}
		return result;
	}
	public void compile(Code c){
		if(numChildren()>0){
			int initReg = c.nextReg;
			for(ASTree t:children){
				c.nextReg = initReg;
				t.compile(c);
			}
		}else{
			c.add(BCONST);
			c.add((byte)0);
			c.add(encodeRegister(c.nextReg++));
		}
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		type = TypeInfo.INT;
		for(ASTree t : children){
			if(!(t == null)){
				type = t.typeCheck(tenv);
			}
		}
		return type;
	}
}
