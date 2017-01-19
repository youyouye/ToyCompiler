package com.toy.ast;

import java.util.List;

import com.toy.compiler.TypeException;

public class VarStmnt extends ASTList{

	protected int index;
	protected TypeInfo varType,valueType;
	
	public VarStmnt(List<ASTree> list) {
		super(list);
	}
	public String name(){
		return ((ASTLeaf)child(0)).token().getText();
	}
	public TypeTag type(){
		return (TypeTag)child(1);
	}
	public ASTree initializer(){
		return child(2);
	}
	public String tosString(){
		return "(var"+name()+" "+type()+" "+initializer() + ")";
	}
	public void lookup(Symbols syms){
		index = syms.putNew(name());
		initializer().lookup(syms); 
	}
	public Object eval(Environment env){
		Object value = initializer().eval(env);
		env.put(0, index,value);
		return value;
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		if(tenv.get(0, index) != null){
			throw new TypeException("duplicate variable:"+name(),this);
		}
		varType = TypeInfo.get(type());
		tenv.put(0, index, varType);
		valueType = initializer().typeCheck(tenv);
		valueType.assertSubtypeOf(varType, tenv, this);
		return varType;
	}
}
