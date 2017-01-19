package com.toy.compiler;

public class LexerRunner {
	public static void main(String[] args){
		Lexer l = new Lexer(new CodeDialog());
		for(Token t;(t = l.read()) != Token.EOF;){
			System.out.println("=>" + t.getText());
		}
	}
}
