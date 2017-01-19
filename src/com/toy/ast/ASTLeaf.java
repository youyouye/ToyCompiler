package com.toy.ast;

import java.util.ArrayList;
import java.util.Iterator;

import com.toy.compiler.Token;
import com.toy.compiler.ToyException;

public class ASTLeaf extends ASTree{
	
	private static ArrayList<ASTree> empty = new ArrayList<ASTree>();
	protected Token token;
	public ASTLeaf(Token t) {
		token = t;
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
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int numChildren() {
		return 0;
	}

	@Override
	public Iterator<ASTree> children() {
		return empty.iterator();
	}

	@Override
	public String location() {
		return "at line" + token.getLineNumber();
	}
	public String toString(){
		return token.getText();
	}

	public Token token() {
		// TODO Auto-generated method stub
		return token;
	}
	@Override
	public Object eval(Environment env) {
		throw new ToyException("cannot eval: "+toString());
	}

}
