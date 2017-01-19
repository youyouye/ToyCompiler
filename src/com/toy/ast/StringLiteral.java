package com.toy.ast;

import com.toy.compiler.Token;
import com.vm.Code;

import static com.vm.Opcode.*;

public class StringLiteral extends ASTLeaf{

	public StringLiteral(Token t) {
		super(t);
	}
	public String value(){
		return token().getText();
	}
	public Object eval(Environment env){
		return value();
	}
	public void compile(Code c){
		int i = c.recode(value());
		c.add(SCONST);
		c.add(encodeShortOffset(i));
		c.add(encodeRegister(c.nextReg++));
	}
	public TypeInfo typeCheck(TypeEnv tenv){
		return TypeInfo.STRING;
	}
	public String translate(TypeInfo result){
		StringBuilder code = new StringBuilder();
		String literal = value();
		code.append('"');
		for(int i = 0;i<literal.length();i++){
			char c = literal.charAt(i);
			if(c == '"')
				code.append("\\\"");
			else if(c == '\\')
				code.append("\\\\");
			else if(c == '\n')
				code.append("\\n");
			else
				code.append("c");
		}
		code.append('"');
		return code.toString();
	}
}






