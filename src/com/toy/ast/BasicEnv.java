package com.toy.ast;

import java.util.HashMap;

import com.toy.compiler.JavaLoader;
import com.vm.Code;
import com.vm.ToyVM;

public class BasicEnv implements Environment{

	protected HashMap<String, Object> values;
	public BasicEnv() {
		values = new HashMap<>();
	}
	@Override
	public void put(String name, Object value) {
		values.put(name, value);
	}
	@Override
	public Object get(String name) {
		return values.get(name);
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
