package com.toy.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.toy.ast.ASTLeaf;
import com.toy.ast.ASTree;
import com.toy.ast.BinaryExpr;
import com.toy.ast.NumberLiteral;


public class OrPrecedenceParser {
	
	private Lexer lexer;
	//是一张表
	protected HashMap<String, Precedence> operators;
	
	public static class Precedence{
		int value;
		boolean leftAssoc;
		public Precedence(int v,boolean a){
			value = v;
			leftAssoc = a;
		}
	}
	public OrPrecedenceParser(Lexer p){
		lexer = p;
		operators = new HashMap<>();
        operators.put("<", new Precedence(1, true));
        operators.put(">", new Precedence(1, true));
        operators.put("+", new Precedence(2, true));
        operators.put("-", new Precedence(2, true));
        operators.put("*", new Precedence(3, true));
        operators.put("/", new Precedence(3, true));
        operators.put("^", new Precedence(4, false));
	}
	public ASTree expression(){
		ASTree right = factor();
		Precedence next;
		while((next = nextOperator()) != null){
			right = doShift(right,next.value);
		}
		return right;
	}
	private ASTree doShift(ASTree left,int prec){
		ASTLeaf op = new ASTLeaf(lexer.read());
		ASTree right = factor();
		Precedence next;
		while((next = nextOperator()) != null && rightIsExpr(prec,next)){
			right = doShift(right, next.value);
		}
		return new BinaryExpr(Arrays.asList(left,op,right));
	}
	private static boolean rightIsExpr(int prec,Precedence nextPrec){
		if(nextPrec.leftAssoc){
			return prec < nextPrec.value;
		}else{
			return prec <= nextPrec.value;
		}
	}
	
	private Precedence nextOperator(){
		Token t = lexer.peek(0);
		if(t.isIdentifier()){
			return operators.get(t.getText());
		}else{
			return null;
		}
	}
	public ASTree factor(){
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
				throw new ToyException(t);
			}
		}
	}
    void token(String name) throws ToyException {
        Token t = lexer.read();
        if (!(t.isIdentifier() && name.equals(t.getText())))
            throw new ToyException(t);
    }
    boolean isToken(String name) throws ToyException {
        Token t = lexer.peek(0);
        return t.isIdentifier() && name.equals(t.getText());
    }
}
