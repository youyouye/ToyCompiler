package com.toy.ast;

import java.util.ArrayList;
import java.util.List;

public class ClassStmnt extends ASTList{

	public ClassStmnt(List<ASTree> list) {
		super(list);
	}
	public String name(){
		return ((ASTLeaf)child(0)).token().getText();
	}
	public String superClass(){
		if(numChildren() < 3){
			return null;
		}else{
			return ((ASTLeaf)child(1)).token().getText();
		}
	}
	public ClassBody body(){
		return (ClassBody)child(numChildren()-1);
	}
	public String toString(){
		String parent = superClass();
		if(parent == null){
			parent = "*";
		}
		return "(class" + name() + " " + parent + " " + body() + ")";
	}
	public Object eval(Environment env){
		/*Éý¼¶ÁË
		ClassInfo cl = new ClassInfo(this, env);
		env.put(name(), cl);
		return name();
		*/
		Symbols methodNames = new MemberSymbols(env.symbols(), MemberSymbols.METHOD);
		Symbols fieldNames = new MemberSymbols(methodNames, MemberSymbols.FIELD);
		OptClassInfo ci = new OptClassInfo(this, env, methodNames, fieldNames);
		env.put(name(), ci);
		ArrayList<DefStmnt> methods = new ArrayList<DefStmnt>();
		if(ci.superClass()!=null){
			ci.superClass().copyTo(fieldNames, methodNames, methods);
		}
		Symbols newSyms = new SymbolThis(fieldNames);
		body().lookup(newSyms,methodNames,fieldNames,methods);
		ci.setMethods(methods);
		return name();
	}
	public void lookup(Symbols syms){}
}
