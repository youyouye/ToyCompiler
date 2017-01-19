package com.toy.ast;

import com.toy.compiler.TypeException;

public class TypeInfo {
	
	public static final TypeInfo ANY = new TypeInfo(){
		@Override public String toString(){return "Any";}
	};
	public static final TypeInfo INT = new TypeInfo(){
		@Override public String toString(){return "Int";}
	};
	public static final TypeInfo STRING = new TypeInfo(){
		@Override public String toString(){return "String";}
	};
	public TypeInfo type(){return this;}
	public boolean match(TypeInfo obj){
		return type() == obj.type();
	}
	public boolean subtypeOf(TypeInfo superType){
		superType = superType.type();
		return type() == superType || superType == ANY;
	}
	public void assertSubtypeOf(TypeInfo type,TypeEnv env,ASTree where) throws TypeException {
		if(type.isUnknownType()){
			((UnknownType)type).toUnknownType().assertSubtypeOf(this, env, where);
		}else if(!subtypeOf(type)){
			throw new TypeException("type mismatch:cannot convert from "+this+"to"+type, where);
		}
	}
	public TypeInfo union(TypeInfo right,TypeEnv tenv){
		if(right.isUnknownType()){
			return right.union(this, tenv);
		}
		if(match(right)){
			return type();
		}else{
			return ANY;
		}
	}
	public TypeInfo plus(TypeInfo right,TypeEnv env){
		if(right.isUnknownType()){
			return right.plus(this, env);
		}
		if(INT.match(this) && INT.match(right)){
			return INT;
		}else if(STRING.match(this) || STRING.match(right)){
			return STRING;
		}else{
			return ANY;
		}
	}
	public static TypeInfo get(TypeTag tag) throws TypeException{
		String tname = tag.type();
		if(INT.toString().equals(tname)){
			return INT;
		}else if(STRING.toString().equals(tname)){
			return STRING;
		}else if(ANY.toString().equals(tname)){
			return ANY;
		}else if(TypeTag.UNDEF.equals(tname)){
			return new UnknownType();
		}else{
			throw new TypeException("unknown type" + tname,tag);
		}
	}
	public static FunctionType function(TypeInfo ret,TypeInfo... params){
		return new FunctionType(ret,params);
	}
	public boolean isFunctionType(){return false;}
	public FunctionType toFunctionType(){return null;}
	public boolean isUnknownType(){return false;}
	public UnknownType toUnknownType(){return null;}
	
	public static class UnknownType extends TypeInfo{
		protected TypeInfo type = null;
		public boolean resolved(){return type != null;}
		public void setType(TypeInfo t){
			type = t;
		}
		@Override public TypeInfo type(){return type==null? ANY : type ;}
		@Override public String toString(){return type().toString();}
		@Override public boolean isUnknownType(){return true;}
		@Override public UnknownType toUnknownType(){return this;}
		@Override
		public void assertSubtypeOf(TypeInfo t,TypeEnv tenv,ASTree where) throws TypeException{
			if(resolved()){
				type.assertSubtypeOf(t, tenv, where);
			}else{
				tenv.addEquation(this, t);
			}
		}
		@Override
		public TypeInfo union(TypeInfo right,TypeEnv tenv){
			if(resolved()){
				return type.union(right, tenv);
			}else{
				tenv.addEquation(this, right);
				return right;
			}
		}
		@Override
		public TypeInfo plus(TypeInfo right,TypeEnv tenv){
			if(resolved()){
				return type.plus(right, tenv);
			}else{
				tenv.addEquation(this, INT);
				return right.plus(INT, tenv);
			}
		}
		
	}
	public static class FunctionType extends TypeInfo{
		
		public TypeInfo returnType;
		public TypeInfo[] parameterTypes;
		public FunctionType(TypeInfo ret,TypeInfo... params){
			returnType = ret;
			parameterTypes = params;
		}
		@Override public boolean isFunctionType(){return true;}
		@Override public FunctionType toFunctionType(){return this;}
		@Override public boolean match(TypeInfo obj){
			if(!(obj instanceof FunctionType)){
				return false;
			}
			FunctionType func = (FunctionType)obj;
			if(parameterTypes.length != func.parameterTypes.length){
				return false;
			}
			for(int i = 0;i<parameterTypes.length;i++){
				if(!parameterTypes[i].match(func.parameterTypes[i])){
					return false;
				}
			}
			return returnType.match(func.returnType);
		}
		@Override public String toString(){
			StringBuilder sb = new StringBuilder();
			if(parameterTypes.length == 0){
				sb.append("Unit");
			}else{
				for(int i = 0;i<parameterTypes.length;i++){
					if(i > 0){
						sb.append("+");
					}
					sb.append(parameterTypes[i]);
				}
			}
			sb.append("->").append(returnType);
			return sb.toString();
		}
	}
	
}
