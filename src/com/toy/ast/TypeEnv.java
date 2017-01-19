package com.toy.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.toy.ast.TypeInfo.UnknownType;
import com.toy.compiler.ToyException;

public class TypeEnv {
	public static class Equation extends ArrayList<UnknownType>{}
	protected TypeEnv outer;
	protected TypeInfo[] types;
	protected List<Equation> equations = new LinkedList<Equation>();
	public TypeEnv(){ this(8,null);}
	public TypeEnv(int size,TypeEnv out){
		outer = out;
		types = new TypeInfo[size];
	}
	public TypeInfo get(int nest,int index){
		if(nest == 0){
			if(index < types.length){
				return types[index];
			}else{
				return null;
			}
		}else if(outer == null){
			return null;
		}else{
			return outer.get(nest-1,index);
		}
	}
	public TypeInfo put(int nest,int index,TypeInfo value){
		TypeInfo oldValue;
		if(nest == 0){
			access(index);
			oldValue = types[index];
			types[index] = value;
			return oldValue;
		}else if(outer == null){
			throw new ToyException("no outer type environment");
		}else{
			return outer.put(nest-1, index, value);
		}
	}
	public void access(int index){
		if(index >= types.length){
			int newLen = types.length+1;
			if(index>= newLen){
				newLen = index+1;
			}
			types = Arrays.copyOf(types, newLen);
		}
	}
	public void addEquation(UnknownType t1,TypeInfo t2){
		if(t2.isUnknownType()){
			if(((UnknownType)t2).toUnknownType().resolved()){
				t2 = t2.type();
			}
		}
		Equation eq = find(t1);
		if(t2.isUnknownType()){
			eq.add(t2.toUnknownType());
		}else{
			for(UnknownType t:eq){
				t.setType(t2);
			}
			equations.remove(eq);
		}
	}
	public Equation find(UnknownType t){
		for(Equation e : equations){
			if(e.contains(t)){
				return e;
			}
		}
		Equation e = new Equation();
		e.add(t);
		equations.add(e);
		return e;
	}
}
