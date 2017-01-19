package com.toy.compiler;

import java.util.Arrays;
import com.toy.ast.ASTLeaf;
import com.toy.ast.ASTree;
import com.toy.ast.BinaryExpr;
import com.toy.ast.NumberLiteral;

public class BasicParser {
	private Lexer lexer;
	public BasicParser(Lexer p){
		lexer = p;
	}
	public ASTree facotr(){
		if(isToken("(")){
			token("(");
			ASTree e = expression();
			token(")");
			return e;
		}else{
			Token t = lexer.read();
			if(t.isNumber()){
				NumberLiteral n = new NumberLiteral(t);
				return n;
			}else{
				throw new ToyException("²»ÊÇÊý×Ö");
			}
		}
	}
	public ASTree term(){
		ASTree left = facotr();
		while(isToken("*") || isToken("/")){
			ASTLeaf op = new ASTLeaf(lexer.read());
			ASTree right = facotr();
			left = new BinaryExpr(Arrays.asList(left,op,right));
		}
		return left;
	}
	public ASTree expression(){
		ASTree left = term();
		while(isToken("+") || isToken("-")){
			ASTLeaf op = new ASTLeaf(lexer.read());
			ASTree right = term();
			left = new BinaryExpr(Arrays.asList(left,op,right));
		}
		return left;
	}
	void token(String name){
		Token t = lexer.read();
		if(!(t.isIdentifier() && name.equals(t.getText()))){
			throw new ToyException("Æ¥Åä´íÎó");
		}
	}
	boolean isToken(String name){
		Token t = lexer.peek(0);
		return t.isIdentifier() && name.equals(t.getText());
	}
}
