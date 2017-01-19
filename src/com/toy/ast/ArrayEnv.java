package com.toy.ast;

import com.toy.compiler.JavaLoader;
import com.toy.compiler.ToyException;
import com.vm.Code;
import com.vm.ToyVM;


public class ArrayEnv implements Environment{

	protected Object[] values;
	protected Environment outer;
	protected JavaLoader jloader = new JavaLoader();
	public ArrayEnv(int size,Environment out) {
		values = new Object[size];
		outer = out;
	}
	public Object get(int nest,int index){
		if(nest == 0){
			return values[index];
		}else if(outer == null){
			return null;
		}else{
			return ((ArrayEnv)outer).get(nest-1,index);
		}
	}
	public void put(int nest,int index,Object value){
		if(nest == 0){
			values[index] = value;
		}else if(outer == null){
			throw new ToyException("no out environment");
		}else{
			((ArrayEnv)outer).put(nest-1, index,value);
		}
	}
	
	@Override
	public void put(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object get(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void putNew(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setOuter(Environment e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Environment where(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Symbols symbols() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ToyVM toyVM() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Code code() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public JavaLoader javaLoader() {
		return jloader;
	}

}
