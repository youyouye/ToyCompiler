package com.vm;

import com.toy.ast.ResizableArrayEnv;

public class ToyVMEnv extends ResizableArrayEnv implements HeapMemory{

	protected ToyVM svm;
	protected Code code;
	
	public ToyVMEnv(int codeSize,int stackSize,int stringSize) {
		// TODO Auto-generated constructor stub
		svm = new ToyVM(codeSize, stackSize, stringSize, this);
		code = new Code(svm);
	}
	public ToyVM toyVM(){return svm;}
	public Code code(){return code;}
	
	@Override
	public Object read(int index) {
		// TODO Auto-generated method stub
		return values[index];
	}

	@Override
	public void write(int index, Object v) {
		// TODO Auto-generated method stub
		values[index] = v;
	}
	
}
