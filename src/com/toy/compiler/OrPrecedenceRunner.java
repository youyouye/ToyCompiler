package com.toy.compiler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.toy.ast.ASTree;

public class OrPrecedenceRunner {
	public static void main(String[] args){
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		Lexer lexer = new Lexer(bf);
		OrPrecedenceParser p = new OrPrecedenceParser(lexer);
		ASTree t = p.expression();
		System.out.println("=>"+t);
	}
}
