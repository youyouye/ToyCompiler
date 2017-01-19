package com.toy.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.toy.ast.ASTree;
import com.toy.ast.ResizableArrayEnv;
import com.toy.ast.TypeEnv;
import com.toy.ast.TypeInfo;
import com.vm.ToyVMEnv;

public class ParserRunner {
	public static void main(String[] args) throws FileNotFoundException, TypeException{
		FileReader fr= new FileReader(new File("D:\\workspace\\ToyCompiler\\Code.txt"));
		Lexer lexer = new Lexer(new BufferedReader(fr));
		MyParser p = new MyParser(lexer);
		ResizableArrayEnv env = new ResizableArrayEnv();
		TypeEnv tEnv = new TypeEnv();
		ToyVMEnv vm = new ToyVMEnv(10000, 10000, 1000);
		while(lexer.peek(0) != Token.EOF){
			ASTree t = p.program();
			System.out.println("=>" + t.toString());
			t.lookup(vm.symbols());
			TypeInfo type = t.typeCheck(tEnv);
			Object r = t.eval(vm);
			System.out.println("=>"+r+":"+type);
			//System.out.println("=>" + t.eval(vm));
		}
	}
}
