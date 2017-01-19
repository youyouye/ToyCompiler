package com.toy.ast;

import java.util.List;

import com.toy.compiler.TypeException;
import com.vm.ToyVM;

public class ParameterList extends ASTList{

	protected int[] offset = null;
	
	public ParameterList(List<ASTree> list) {
		super(list);
	}
	public String name(int i){
		return ((ASTLeaf)child(i)).token().getText();
	}
	public int size(){
		return numChildren();
	}
	public void eval(Environment env,int index,Object value){
		/*
		env.put(0,offset[index],value);
		*/
		ToyVM vm = env.toyVM();
		vm.stack()[offset[index]] = value;
	}
	public void lookup(Symbols syms){
		int s = size();
		offset = new int[s];
		for(int i =0; i<s ;i++){
			offset[i] = syms.putNew(name(i));
		}
	}
	public TypeTag typeTag(int i){
		return (TypeTag) ((Name)child(i)).typeTag();
	}
	public TypeInfo[] types() throws TypeException{
		int s = size();
		TypeInfo[] result = new TypeInfo[s];
		for(int i = 0;i<s;i++){
			result[i] = TypeInfo.get(typeTag(i));
		}
		return result;
	}
}
