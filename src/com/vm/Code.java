package com.vm;

public class Code {
	
	protected ToyVM svm;
	protected int codeSize;
	protected int numofStrings;
	public int nextReg;
	public int frameSize;
	
	public Code(ToyVM toyVm){
		svm = toyVm;
		codeSize = 0;
		numofStrings = 0;
	}
	public int position(){
		return codeSize;
	}
	public void set(short value,int pos){
		svm.code()[pos] = (byte) (value>>>8);
		svm.code()[pos+1] = (byte)value;
	}
	public void add(byte b){
		svm.code()[codeSize++] = b;
	}
	public void add(short i){
		add((byte)(i >>> 8));
		add((byte)i);
	}
	public void add(int i){
		add((byte)(i>>>24));
		add((byte)(i>>>16));
		add((byte)(i>>>8));
		add((byte)i);
	}
	public int recode(String s){
		svm.strings()[numofStrings] = s;
		return numofStrings++;
	}
}
