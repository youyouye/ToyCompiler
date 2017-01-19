package com.toy.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.toy.ast.ASTree;
import com.toy.ast.NestedEnv;
import com.toy.ast.NullStmnt;

public class InterpreterRunner {
	public static void main(String[] args) throws FileNotFoundException{
		NestedEnv env = new NestedEnv();
		new Natives().environment(env);
		FileReader fr= new FileReader(new File("D:\\workspace\\ToyCompiler\\Code.txt"));
		Lexer lexer = new Lexer(new BufferedReader(fr));
		MyParser p = new MyParser(lexer);
		while(lexer.peek(0) != Token.EOF){
			ASTree t = p.program();
			if(!(t instanceof NullStmnt)){
				Object r = t.eval(env);
				System.out.println("=>" + r);
			}
		}
	}
}
