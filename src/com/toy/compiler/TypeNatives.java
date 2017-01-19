package com.toy.compiler;

import com.toy.ast.TypeEnv;
import com.toy.ast.TypeInfo;
import com.toy.ast.Environment;
import com.toy.fun.currentTime;
import com.toy.fun.read;

public class TypeNatives extends Natives{
	protected TypeEnv typeEnv;
	public TypeNatives(TypeEnv te){typeEnv = te;}
	protected void append(Environment env,String name,Class<?> clazz,
			String methodName,TypeInfo type,Class<?>...params){
		append((com.toy.ast.Environment) env, name, clazz, methodName, params);
		int index = env.symbols().find(name);
		typeEnv.put(0, index, type);
	}
	protected void appendNatives(Environment env){
		append(env, "print", com.toy.fun.print.class, "m", TypeInfo.function(TypeInfo.INT, TypeInfo.ANY),Object.class);
		append(env, "read", read.class, "m", TypeInfo.function(TypeInfo.STRING ));
		append(env, "length", com.toy.fun.length.class, "m", TypeInfo.function(TypeInfo.INT, TypeInfo.STRING),String.class);
		append(env, "toInt", com.toy.fun.toInt.class, "m", TypeInfo.function(TypeInfo.INT, TypeInfo.ANY),Object.class);
		append(env, "currentTime", currentTime.class, "m", TypeInfo.function(TypeInfo.INT ));
	}
}
