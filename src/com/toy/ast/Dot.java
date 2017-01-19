package com.toy.ast;

import java.util.List;

import com.toy.ast.ToyObject.AccessException;
import com.toy.compiler.ToyException;
import com.toy.compiler.TypeException;

public class Dot extends Protfix{

	protected OptClassInfo classInfo = null;
	protected boolean isField;
	protected int index;
	
	public Dot(List<ASTree> list) {
		super(list);
	}
	public String name(){
		return ((ASTLeaf)child(0)).token().getText();
	}
	public Object posfix(){
			return child(1);
	}
	public String toString(){
		return "." + name();
	}
	public Object eval(Environment env,Object value){
		String member = name();
		if(value instanceof OptClassInfo){
			if(member.equals("new")){
				OptClassInfo ci = (OptClassInfo)value;
				ArrayEnv newEnv = new ArrayEnv(1, ci.environment());
				OptToyObject to = new OptToyObject(ci,ci.size());
				newEnv.put(0,0, to);
				initObject(ci, to,newEnv);
				return to;
			}
		}else if(value instanceof OptToyObject){
			/*
			OptToyObject target = (OptToyObject)value;
			if(target.classInfo() != classInfo){
				updateCache(target);
			}
			if(isField){
				return target.read(index);
			}else{
				return target.method(index);
			}
			*/
			
			if(numChildren() == 2){
				Object func;
				try {
					func = ((OptToyObject) value).read(member);
					return ((Arguments)child(1)).eval(env, func);
				} catch (com.toy.ast.OptToyObject.AccessException e) {
					e.printStackTrace();
				}
			}else{
				try {
					return ((OptToyObject) value).read(member);
				}catch (com.toy.ast.OptToyObject.AccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		throw new ToyException("bad member access: " + member,this);
	}
	protected void initObject(OptClassInfo ci,OptToyObject obj,Environment env){
		if(ci.superClass() != null){
			initObject(ci.superClass(),obj,env);
		}
		ci.body().eval(env);
	}
	public void lookup(Symbols syms){	
	}
	public void updateCache(OptToyObject target){
		String member = name();
		classInfo = target.classInfo();
		Integer i =classInfo.fieldIndex(member);
		if(i != null){
			isField = true;
			index = i;
			return;
		}
		i = classInfo.methodIndex(member);
		if(i != null){
			isField = false;
			index = i;
			return;
		}
		throw new ToyException("bad member access:"+member);
	}
	@Override
	public TypeInfo typeCheck(TypeEnv tenv, TypeInfo target)
			throws TypeException {
		// TODO Auto-generated method stub
		return null;
	}
}
