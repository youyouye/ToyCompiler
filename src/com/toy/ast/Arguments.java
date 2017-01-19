package com.toy.ast;

import java.util.List;

import com.toy.ast.TypeInfo.FunctionType;
import com.toy.compiler.ToyException;
import com.toy.compiler.TypeException;
import com.vm.Code;
import com.vm.ToyVM;
import com.vm.VmFunction;

import static com.vm.Opcode.*;

public class Arguments extends Protfix{

	protected TypeInfo[] argTypes;
	protected TypeInfo.FunctionType funcType;
	
	public Arguments(List<ASTree> list) {
		super(list);
	}
	public int size(){
		return numChildren();
	}
	public Object eval(Environment callerEnv,Object value){
		/*
		if(value instanceof NativeFunction){
			NativeFunction func = (NativeFunction)value;
			int nparams = func.numOfParameters();
			if(size() != nparams){
				throw new ToyException("bad number of NativeArguments",this);
			}
			Object[] args = new Object[nparams];
			int num = 0;
			for(int i = 0;i < size();i++){
				ASTree a = child(i);
				args[num++] = a.eval(callerEnv);
			}
			return func.invoke(args, this);
		}
		if(!(value instanceof Function)){
			throw new ToyException("not define function",this);
		}
		Function function = (Function)value;
		ParameterList params = function.parameters();
		if(size() != params.size()){
			throw new ToyException("bad number of arguments",this);
		}
		Environment newEnv = function.makeEnv();
		int num = 0;
		for(int i = 0;i<numChildren();i++){
			ASTree a = child(i);
			((ParameterList)params).eval(newEnv,num++,a.eval(callerEnv));
		}
		return function.block().eval(newEnv);
		*/
		if(!(value instanceof VmFunction)){
			throw new ToyException("bad function");
		}
		VmFunction func = (VmFunction) value;
		ParameterList params = func.parameters();
		if(size() != params.size()){
			throw new ToyException("bad member of arguments");
		}
		int num = 0;
		for(ASTree t : children){
			params.eval(callerEnv,num++,t.eval(callerEnv));
		}
		ToyVM svm = callerEnv.toyVM();
		svm.run(func.entry());
		return svm.stack()[0];
	}
	public void compile(Code c){
		int newOffset = c.frameSize;
		int numOfArgs = 0;
		for(ASTree t:children){
			t.compile(c);
			c.add(MOVE);
			c.add(encodeRegister(--c.nextReg));
			c.add(encodeOffset(newOffset++));
			numOfArgs++;
		}
		c.add(CALL);
		c.add(encodeRegister(--c.nextReg));
		c.add(encodeOffset(numOfArgs));
		c.add(MOVE);
		c.add(encodeOffset(c.frameSize));
		c.add(encodeRegister(c.nextReg++));
	}
	public TypeInfo typeCheck(TypeEnv tenv,TypeInfo target) throws TypeException{
		if(!(target instanceof TypeInfo.FunctionType)){
			throw new TypeException("bad function", this);
		}
		funcType = (FunctionType) target;
		TypeInfo[] params = funcType.parameterTypes;
		if(size() != params.length){
			throw new TypeException("bad number of arguments", this);
		}
		argTypes = new TypeInfo[params.length];
		int num = 0;
		for(ASTree a : children){
			TypeInfo t = argTypes[num] = a.typeCheck(tenv);
			t.assertSubtypeOf(params[num++], tenv, this);
		}
		return funcType.returnType;
	}
}
