package com.toy.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.toy.ast.ASTLeaf;
import com.toy.ast.ASTList;
import com.toy.ast.ASTree;
import com.toy.ast.Arguments;
import com.toy.ast.ArrayLiteral;
import com.toy.ast.ArrayRef;
import com.toy.ast.BinaryExpr;
import com.toy.ast.BlankLibteral;
import com.toy.ast.BlockStmnt;
import com.toy.ast.ClassBody;
import com.toy.ast.ClassStmnt;
import com.toy.ast.DefStmnt;
import com.toy.ast.Dot;
import com.toy.ast.Fun;
import com.toy.ast.FuncStmnt;
import com.toy.ast.IfStmnt;
import com.toy.ast.Name;
import com.toy.ast.NegativeExpr;
import com.toy.ast.NullStmnt;
import com.toy.ast.NumberLiteral;
import com.toy.ast.ParameterList;
import com.toy.ast.Protfix;
import com.toy.ast.StringLiteral;
import com.toy.ast.TypeTag;
import com.toy.ast.VarStmnt;
import com.toy.ast.WhileStmnt;

public class MyParser {
	private Lexer lexer;
	protected HashMap<String, Precedence> operators;
	public static class Precedence{
		int value;
		boolean leftAssoc;
		public Precedence(int v,boolean a){
			value = v;
			leftAssoc = a;
		}
	}
	
	public MyParser(Lexer p){
		lexer = p;
		operators = new HashMap<>();
		operators.put("=", new Precedence(1, false));
		operators.put("==", new Precedence(2, true));
        operators.put("<", new Precedence(2, true));
        operators.put(">", new Precedence(2, true));
        operators.put("+", new Precedence(3, true));
        operators.put("-", new Precedence(3, true));
        operators.put("*", new Precedence(4, true));
        operators.put("/", new Precedence(4, true));
        operators.put("%", new Precedence(4, true));
        operators.put("^", new Precedence(5, false));
	}
	public ASTree primary(){
		if(isToken("fun")){
			token("fun");
			ASTree p = param_list();
			ASTree b = block();
			return new Fun(Arrays.asList(p,b));
		}
		if(isToken("[")){
			token("[");
			if(!isToken("]")){
				ASTree ele = elements();
				token("]");
				return ele;
			}			
		}
		if(isToken("(")){
			token("(");
			ASTree e = expr();
			token(")");
			return e;
		}else{
			Token t = lexer.read();
			if(t.isNumber()){
				NumberLiteral n = new NumberLiteral(t);
				return n;
			}else if(t.isIdentifier()){
				Name name = new Name(t);
				ASTree p = postfix();
				if(p == null){
					return name;
				}else if(p instanceof ArrayLiteral){
					return new ArrayRef(Arrays.asList(name,p));
				}else {
					return new FuncStmnt(Arrays.asList(name,p));
				}
			}else if(t.isString()){
				StringLiteral s = new StringLiteral(t);
				return s;
			}else{
				throw new ToyException("primary error!");
			}
		}
	}
	public ASTree factor(){
		if(isToken("-")){
			token("-");
			ASTree e = primary();
			return new NegativeExpr(Arrays.asList(e));
		}else{
			ASTree t = primary();
			return t;
		}
	}
	public ASTree expr(){
		ASTree right = factor();
		Precedence next;
		while((next = nextOperator()) != null){
			right = doShift(right, next.value);
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
	
	public ASTree block(){
		if(isToken("{")){
			token("{");
			while(isToken(";") || isToken(Token.EOL)){
				lexer.read();
			}
			if(isToken("}")){
				token("}");
				return new BlockStmnt(Arrays.asList(new NullStmnt(null)));
			}
			List<ASTree> arrays = new ArrayList<ASTree>();
			while(!isToken("}")){
				ASTree s1 = statement();
				arrays.add(s1);
				while(isToken(";") || isToken(Token.EOL)){
					lexer.read();
				}
				if(isToken("}")){
					token("}");
					return new BlockStmnt(arrays);
				}
			}
		}
		throw new ToyException("括号内错误");
	}
	public ASTree simple(){
		ASTree e = expr();
		return e;
	}
	public ASTree statement(){
		if(isToken("if")){
			token("if");
			ASTree e = expr();
			ASTree b = block();
			if(isToken("else")){
				token("else");
				ASTree b2 = block();
				return new IfStmnt(Arrays.asList(e,b,b2));
			}
			return new IfStmnt(Arrays.asList(e,b));
		}else if(isToken("while")){
			token("while");
			ASTree e = expr();
			ASTree b = block();
			return new WhileStmnt(Arrays.asList(e,b));
		}else if(isToken("var")){
			return variable();
		}else{
			ASTree s = simple();
			return s;
		}
	}
	public ASTree program(){
		while(isToken("\\n")){
			lexer.read();
		}
		if(isToken("class")){
			ASTree c = defclass();
			return c;
		}
		if(isToken("def")){
			ASTree d = def();
			return d;
		}
		ASTree s = statement();
		if(s != null){
			if(isToken(";") || isToken(Token.EOL)){
				lexer.read();
			}
			return s;
		}else{
			if(isToken(";") || isToken(Token.EOL)){
				ASTLeaf e = new ASTLeaf(lexer.read());
				return e;
			}
			throw new ToyException("结束符错误！");
		}
	}
	//增加了函数功能:
	public ASTree param(){
		if(isToken(")")){
			return null;
		}
		Token t = lexer.read();
		if(t.isIdentifier()){
			Name n = new Name(t);
			ASTree t1 = type_tag();
			return new Name(t,(TypeTag) t1);
		}else{
			throw new ToyException("函数参数不是Indentifier型",t);
		}	
	} 
	public ASTree params(){
		ASTree p = param();
		List<ASTree> params = new ArrayList<>();
		if(p != null){
			params.add(p);
		}
		while(isToken(",")){
			token(",");
			params.add(param());
		}
		return new ParameterList(params);
	}
	public ASTree param_list(){
		if(isToken("(")){
			token("(");
			ASTree e = params();
			token(")");
			return e;
		}else{
			throw new ToyException("param_list格式 错误");
		}
	}
	public ASTree def(){
		if(isToken("def")){
			token("def");
			Token t = lexer.read();
			if(t.isIdentifier()){
				Name n = new Name(t);
				ASTree e = param_list();
				ASTree type = type_tag();
				ASTree b = block();
				return new DefStmnt(Arrays.asList(n,e,type,b));
			}else{
				throw new ToyException("函数名错误",t);
			}
		}else{
			throw new ToyException("函数缺乏def");
		}
	}
	public ASTree args(){
		if(isToken(")")){
			return new Arguments(new ArrayList<ASTree>());
		}
		ASTree e = expr();
		if(e instanceof BlankLibteral){
			return e;
		}
		List<ASTree> args = new ArrayList<>();
		args.add(e);
		while(isToken(",")){
			token(",");
			ASTree t = expr();
			args.add(t);
		}
		return new Arguments(args);
	}
	public ASTree postfix(){
		if(isToken(".")){
			token(".");
			Token t = lexer.read();
			if(t.isIdentifier()){
				Name n = new Name(t);
				if(isToken("(")){
					ASTree args = postfix();
					return new Dot(Arrays.asList(n,args));
				}
				return new Dot(Arrays.asList(n));
			}else{
				throw new ToyException(". behind error",t);
			}
		}
		if(isToken("(")){
			token("(");
			ASTree e = args();
			token(")");
			return e;
		}else if(isToken("[")){
			ASTree ex = primary();
			return ex;
		}else{
			return null;
		}
	}
	//增加类的实现
	public ASTree member(){
		if(isToken("def")){
			ASTree d = def();
			return d;
		}else{
			ASTree s = simple();
			return s;
		}
	}
	public ASTree class_body(){
		if(isToken("{")){
			token("{");
			while(isToken("\\n")){
				lexer.read();
			}
			if(isToken("}")){
				token("}");
				return new ClassBody(new ArrayList<ASTree>());
			}
			List<ASTree> arrays = new ArrayList<>();
			while(!isToken("}")){
				ASTree m = member();
				arrays.add(m);
				while(isToken(";")|| isToken(Token.EOL)){
					lexer.read();
				}
				if(isToken("}")){
					token("}");
					return new ClassBody(arrays);
				}
			}
		}
		throw new ToyException("class lack {}");
	}
	public ASTree defclass(){
		if(isToken("class")){
			token("class");
			Token t = lexer.read();
			if(t.isIdentifier()){
				Name className = new Name(t);
				if(isToken("extends")){
					token("extends");
					Token e = lexer.read();
					if(e.isIdentifier()){
						Name superName = new Name(e);
						ASTree body = class_body();
						return new ClassStmnt(Arrays.asList(className,superName,body));
					}else{
						throw new ToyException("extends error",e);
					}
				}else{
					ASTree body = class_body();
					return new ClassStmnt(Arrays.asList(className,body));
				}
			}else{
				throw new ToyException("identifier error",t);
			}
		}else{
			throw new ToyException("class error");
		}
	}
	//增加数组：
	public ASTree elements(){
		ASTree e = expr();
		List<ASTree> arrays = new ArrayList<>();
		arrays.add(e);
		while(isToken(",")){
			token(",");
			ASTree ex = expr();
			arrays.add(ex);
		}
		return new ArrayLiteral(arrays);
	}
	//增加数据类型:
	public ASTree type_tag(){
		if(isToken(":")){
			token(":");
			Token e = lexer.read();
			if(e.isIdentifier()){
				StringLiteral name = new StringLiteral(e);
				return new TypeTag(Arrays.asList(name));
			}else{
				throw new ToyException("类型定义错误");
			}
		}else{
			return new TypeTag(new ArrayList<ASTree>());
		}
	} 
	public ASTree variable(){
		if(isToken("var")){
			token("var");
			Token e = lexer.read();
			if(e.isIdentifier()){
				Name name = new Name(e);
				ASTree t1 = type_tag();
				if(isToken("=")){
					token("=");
					ASTree t2 = expr();
					return new VarStmnt(Arrays.asList(name,t1,t2));
				}else{
					throw  new ToyException("声明定义错误");
				}
			}else{
				throw new ToyException("类型定义错误");
			}
		}else{
			return null;
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