package com.toy.fun;

import com.toy.ast.ArrayEnv;

public class print {
	public static int m(ArrayEnv env,Object obj){return  m(obj);}
	public static int m(Object obj){
		System.out.println(obj.toString());
		return 0;
	}
}
