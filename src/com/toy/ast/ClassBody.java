package com.toy.ast;

import java.util.ArrayList;
import java.util.List;

public class ClassBody extends ASTList{

	public ClassBody(List<ASTree> list) {
		super(list);
	}
	public Object eval(Environment env){
		for(ASTree t : children){
			if(!(t instanceof DefStmnt)){
				t.eval(env);
			}
		}
		return null;
	}
	public void lookup(Symbols syms,Symbols methodNames,Symbols fieldNames,ArrayList<DefStmnt> methods){
		for(ASTree t: children){
			if(t instanceof DefStmnt){
				DefStmnt def = (DefStmnt)t;
				int oldSize = methodNames.size();
				int i = methodNames.putNew(def.name());
				if(i >= oldSize){
					methods.add(def);
				}else{
					methods.set(i, def);
				}
				def.lookupAsMethod(fieldNames);
			}else{
				t.lookup(syms);
			}
		}
	}
}
