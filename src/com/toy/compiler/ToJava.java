package com.toy.compiler;

import com.toy.ast.ASTree;
import com.toy.ast.TypeInfo;

public class ToJava {
	public static final String METHOD = "m";
	public static final String LOCAL = "v";
	public static final String ENV = "env";
	public static final String RESULT = "res";
	public static final String ENV_TYPE = "com.toy.ast.ArrayEnv";
	
	public static String translateExpr(ASTree ast,TypeInfo from,TypeInfo to){
		return translateExpr(ast, from, to);
	}
	public static String translateExpr(String expr,TypeInfo from,TypeInfo to){
		from = from.type();
		to = to.type();
		if(from == TypeInfo.INT){
			if(to == TypeInfo.ANY){
				return "new Integer(" + expr + ")";
			}else if(to == TypeInfo.STRING){
				return "Integer.toString("+expr+")";
			}
		}else if(from == TypeInfo.ANY){
			if(to == TypeInfo.STRING){
				return expr + ".toString()";
			}else if(to == TypeInfo.INT){
				return "((Integer)" + expr + ").intValue()";
			}
		}
		return expr;
	}
	public static String returnZero(TypeInfo to){
		if(to.type() == TypeInfo.ANY){
			return RESULT + "=new Integer(0);";
		}else{
			return RESULT + "=0;";
		}
	}
	
}
