package com.toy.ast;

import com.toy.ast.Symbols.Location;
import com.toy.compiler.ToJava;
import com.toy.compiler.Token;
import com.toy.compiler.ToyException;
import com.toy.compiler.TypeException;
import com.vm.Code;

import static com.vm.Opcode.*;

public class Name extends ASTLeaf{
	
	protected static final int UNKNOW = -1;
	protected int nest,index;
	protected TypeInfo type;
	protected TypeTag typeTag;
	
	public Name(Token t) {
		super(t);
	}
	public Name(Token t,TypeTag tag){
		super(t);
		this.typeTag = tag;
	}
	public String name(){
		return token().getText();
	}
	public Object eval(Environment env){
		if(index == UNKNOW){
			return env.get(name());
		}else if(nest == MemberSymbols.FIELD){
			return getThis(env).read(index);
		}else if(nest == MemberSymbols.METHOD){
			return getThis(env).method(index);
		}else{
			return env.get(nest,index);
		}
	}
	public void lookup(Symbols syms){
		Location loc = syms.get(name());
		if(loc == null){
			throw new ToyException("undefined name" + name());
		}else{
			nest = loc.nest;
			index = loc.index;
		}
	}
	public void lookupForAssign(Symbols syms){
		Location loc = syms.put(name());
		nest = loc.nest;
		index = loc.index;
	}

	public void evalForAssign(Environment env, Object value) {
		// TODO Auto-generated method stub
		if(index == UNKNOW){
			env.put(name(), value);
		}else if(nest == MemberSymbols.FIELD){
			getThis(env).write(index, value);
		}else if(nest == MemberSymbols.METHOD){
			throw new ToyException("cannot update a method"+name());
		}else{
			env.put(nest, index,value);
		}
	}
	protected OptToyObject getThis(Environment env){
		return (OptToyObject)env.get(0,0);
	}
	
	public void compile(Code c){
		if(nest > 0){
			c.add(GMOVE);
			c.add(encodeShortOffset(index));
			c.add(encodeRegister(c.nextReg++));
		}else{
			c.add(MOVE);
			c.add(encodeOffset(index));
			c.add(encodeRegister(c.nextReg++));
		}
	}
	public void compileAssign(Code c){
		if(nest > 0){
			c.add(GMOVE);
			c.add(encodeRegister(c.nextReg - 1));
			c.add(encodeShortOffset(index));
		}else{
			c.add(MOVE);
			c.add(encodeRegister(c.nextReg-1));
			c.add(encodeOffset(index));
		}
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		type = tenv.get(nest, index);
		if(type == null){
			throw new TypeException("undefied name:"+name(), this);
		}else{
			return type;
		}
	}
	public TypeInfo typeCheckForAssign(TypeEnv tenv,TypeInfo valueType) throws TypeException{
		type = tenv.get(nest, index);
		if(type == null){
			type = valueType;
			tenv.put(0, index, valueType);
			return valueType;
		}else{
			valueType.assertSubtypeOf(type, tenv, this);
			return type;
		}
	}
	public TypeTag typeTag(){
		return typeTag;
	}
	public String translate(TypeInfo result){
		if(type.isFunctionType()){
			return JavaFunction.className(name())+"."+"m";
		}else if(nest == 0){
			return "v" + index;
		}else{
			String expr = "env" + ".get(0," + index + ")";
			return ToJava.translateExpr(expr, TypeInfo.ANY, type);
		}
	}
	public String translateAssign(TypeInfo valueType,ASTree right){
		if(nest == 0)
			return "(" + "v" + index + "="+ToJava.translateExpr(right, valueType, type)+")";
		else{
			String value = right.translate(null);
			return "";
		}
	}
}
