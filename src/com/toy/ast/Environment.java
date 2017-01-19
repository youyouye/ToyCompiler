package com.toy.ast;

import com.toy.compiler.JavaLoader;
import com.vm.Code;
import com.vm.ToyVM;

public interface Environment {
	void put(String name,Object value);
	Object get(String name);
	void putNew(String name,Object value);
	void setOuter(Environment e);
	Environment where(String name);
	Symbols symbols();
	void put(int nest,int index,Object value);
	Object get(int next,int index);
	ToyVM toyVM();
	Code code();
	JavaLoader javaLoader();
}
