package com.toy.ast;

import java.util.List;

import com.toy.ast.TypeInfo.FunctionType;
import com.toy.ast.TypeInfo.UnknownType;
import com.toy.compiler.TypeException;
import com.vm.Code;
import com.vm.ToyVM;
import com.vm.VmFunction;

import static com.vm.Opcode.*;

public class DefStmnt extends ASTList{

	protected TypeInfo.FunctionType funcType;
	protected TypeEnv bodyEnv;
	
	public DefStmnt(List<ASTree> list) {
		super(list);
	}
	public String name(){
		return ((ASTLeaf)child(0)).token().getText();
	}
	public ParameterList parameters(){
		return (ParameterList)child(1);
	}
	public BlockStmnt body(){
		return (BlockStmnt)child(3);
	}
	public String toString(){
		return "(def"+ name() + " " + parameters() + " " +type()+" "+ body() + ")";
	}
	public Object eval(Environment env){
		/*
		env.put(0,index, new OptFunction(parameters(), body(), env,size));
		return name();
		*/
		String funcName = name();
		Code code = env.code();
		int entry = code.position();
		compile(code);
		env.putNew(funcName, new VmFunction(parameters(), body(), env, entry));
		return funcName;
	}
	public void lookup(Symbols syms){
		index = syms.putNew(name());
		size = Fun.lookup(syms, parameters(), body());
	}
	public int locals(){return size;}
	public void lookupAsMethod(Symbols syms){
		Symbols newSyms = new Symbols(syms);
		newSyms.putNew(SymbolThis.NAME);
		parameters().lookup(newSyms);
		body().lookup(newSyms);
		size = newSyms.size();
	}
	protected int index,size;
	
	public void compile(Code c){
		c.nextReg = 0;
		c.frameSize = size+ToyVM.SAVE_AREA_SIZE;
		c.add(SAVE);
		c.add(encodeOffset(size));
		body().compile(c);
		c.add(MOVE);
		c.add(encodeRegister(c.nextReg-1));
		c.add(encodeOffset(0));
		c.add(RESTORE);
		c.add(encodeOffset(size));
		c.add(RETURN);
	}
	public TypeTag type(){return (TypeTag)child(2);}
	
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		TypeInfo[] params = parameters().types();
		TypeInfo retType = TypeInfo.get(type());
		funcType = TypeInfo.function(retType, params);
		TypeInfo oldType = tenv.put(0, index, funcType);
		if(oldType != null){
			throw new TypeException("function reefinition"+name(), this);
		}
		bodyEnv = new TypeEnv(size, tenv);
		for(int i =0;i<params.length;i++){
			bodyEnv.put(0, i, params[i]);
		}
		TypeInfo bodyType = body().typeCheck(bodyEnv);
		bodyType.assertSubtypeOf(retType, tenv, this);
		
		FunctionType func = funcType.toFunctionType();
		for(TypeInfo t : func.parameterTypes){
			fixUnknown(t);
		}
		fixUnknown(func.returnType);
		return func;
	}	
	public void fixUnknown(TypeInfo t){
		if(t.isUnknownType()){
			UnknownType ut = t.toUnknownType();
			if(!ut.resolved()){
				ut.setType(TypeInfo.ANY);
			}
		}
	}
}
