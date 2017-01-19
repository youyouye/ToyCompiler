package com.toy.ast;

import java.util.HashMap;

import com.toy.compiler.JavaLoader;
import com.vm.Code;
import com.vm.ToyVM;

public class NestedEnv implements Environment{

	protected HashMap<String, Object> values;
	protected Environment outer;
	public NestedEnv() {
		this(null);
	}
	public NestedEnv(Environment e){
		values = new HashMap<>();
		outer = e;
	}
	public void setOuter(Environment e){
		outer = e;
	}
	@Override
	public void put(String name, Object value) {
		Environment e = where(name);
		if(e == null){
			e = this;
		}
		e.putNew(name, value);
	}
	@Override
	public Object get(String name) {
		Object v = values.get(name);
		if((v == null) && outer != null){
			return outer.get(name);
		}else{
			return v;
		}
	}
	public void putNew(String name,Object value){
		values.put(name, value);
	}
	public Environment where(String name){
		if(values.get(name) != null){
			return this;
		}else if(outer == null){
			return null;
		}else{
			return ((NestedEnv)outer).where(name);
		}
	}
	@Override
	public Symbols symbols() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void put(int nest, int index, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object get(int next, int index) {
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
		// TODO Auto-generated method stub
		return null;
	}
}
