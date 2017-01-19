package com.toy.ast;

import java.util.Iterator;
import java.util.List;

import com.toy.compiler.ToyException;
import com.vm.Code;

public class ASTList extends ASTree{
	
	protected List<ASTree> children;
	public ASTList(List<ASTree> list) {
		children = list;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ASTree next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ASTree child(int i) {
		return children.get(i);
	}

	@Override
	public int numChildren() {
		return children.size();
	}

	@Override
	public Iterator<ASTree> children() {
		return children.iterator();
	}

	@Override
	public String location() {
		for(ASTree t:children){
			String s = t.location();
			if(s != null){
				return s;
			}
		}
		return null;
	}
	public String toString(){
		StringBuilder builder = new StringBuilder();
		String sep = "(";
		if(numChildren() == 0){
			return builder.append(")").toString();
		}
		for(ASTree t:children){
			builder.append(sep);
			sep = " ";
			builder.append(t.toString());
		}
		return builder.append(")").toString();
	}

	@Override
	public Object eval(Environment env) {
		throw new ToyException("cannot eval:" + toString());
	}
	public void lookup(Symbols syms){
		for(ASTree t : children){
			t.lookup(syms);
		}
	}
	public void compile(Code c){
		for(ASTree t : children){
			t.compile(c);
		}
	}
}
