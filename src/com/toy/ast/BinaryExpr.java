package com.toy.ast;
import java.util.List;

import com.toy.ast.ToyObject.AccessException;
import com.toy.compiler.ToyException;
import com.toy.compiler.TypeException;
import com.vm.Code;

import static com.vm.Opcode.*;

public class BinaryExpr extends ASTList{
	
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	protected TypeInfo leftType,rightType;
	
	public BinaryExpr(List<ASTree> list) {
		super(list);
	}
	public ASTree left(){return child(0);}
	public String operator(){
		return ((ASTLeaf)child(1)).token().getText();
	}
	public ASTree right(){
		return child(2);
	}
	
	public void lookup(Symbols syms){
		ASTree left = left();
		if("=".equals(operator())){
			if(left instanceof Name){
				((Name) left).lookupForAssign(syms);
				right().lookup(syms);
				return;
			}
		}
		left().lookup(syms);
		right().lookup(syms);
	}
	
	public Object eval(Environment env){
		String op = operator();
		if("=".equals(op)){
			Object right = ((ASTree)right()).eval(env);
			return computeAssign(env, right);
		}else{
			Object left = left().eval(env);
			Object right = right().eval(env);
			return computeOp(left,op,right);
		}
	}
	protected Object computeAssign(Environment env,Object rvalue){
		ASTree l = left();
		if(l instanceof Name){
			((Name) l).evalForAssign(env, rvalue);
			return rvalue;
		}else if(l instanceof FuncStmnt){
			FuncStmnt fs = (FuncStmnt)l;
			if(fs.child(1) != null && fs.child(1) instanceof Dot){
				Object o = fs.eval(env);
				if(o instanceof OptToyObject){
					return setField((OptToyObject)o, (Dot)fs.child(1), rvalue);
				}else if( o == null){
					throw new ToyException("class lack the value",this);
				}else{
					return setField((OptToyObject)fs.child(0).eval(env), (Dot)fs.child(1), rvalue);
				}
			}
		}
		throw new ToyException("bad assigment",this);

	}
	protected Object setField(OptToyObject obj,Dot expr,Object rvalue){
		String name = expr.name();
		try {
			obj.write(name, rvalue);
			return rvalue;
		} catch (com.toy.ast.OptToyObject.AccessException e) {
			throw new ToyException("bad member access" + location() + ": " + name);
		}
	}
	//应该说最大的问题是考虑清楚所有的情况
	protected Object computeOp(Object left,String op,Object right) {
		if(left instanceof Integer && right instanceof Integer){
			return computeNumber((Integer)left,op,(Integer)right);
		}
		else if(op.equals("+")){
			return String.valueOf(left)+String.valueOf(right);
		}else if(op.equals("==")){
			if(left == null)
				return right == null ? TRUE : FALSE;
			else
				return left.equals(right) ? TRUE : FALSE;
		}else{
			throw new ToyException("bad type",this);
		}
	}
	protected Object computeNumber(Integer left,String op,Integer right) {
		int a = left.intValue();
		int b = right.intValue();
		if (op.equals("+"))
            return a + b;
        else if (op.equals("-"))
            return a - b;
        else if (op.equals("*"))
            return a * b;
        else if (op.equals("/"))
            return a / b;
        else if (op.equals("%"))
            return a % b;
        else if (op.equals("=="))
            return a == b ? TRUE : FALSE;
        else if (op.equals(">"))
            return a > b ? TRUE : FALSE;
        else if (op.equals("<"))
            return a < b ? TRUE : FALSE;
        else
            throw new ToyException("bad operator", this);
	}
	public void compile(Code c){
		String op = operator();
		if(op.equals("=")){
			ASTree l = left();
			if(l instanceof Name){
				right().compile(c);
				((Name) l).compileAssign(c);
			}else{
				throw new ToyException("bad assigment");
			}
		}else{
			left().compile(c);
			right().compile(c);
			c.add(getOpcode(op));
			c.add(encodeRegister(c.nextReg-2));
			c.add(encodeRegister(c.nextReg-1));
			c.nextReg--;
		}
	}
	protected byte getOpcode(String op){
		if(op.equals("+")){
			return ADD;
		}else if(op.equals("-")){
			return SUB;
		}else if(op.equals("*")){
			return MUL;
		}else if(op.equals("/")){
			return DIV;
		}else if(op.equals("%")){
			return REM;
		}else if(op.equals("==")){
			return EQUAL;
		}else if(op.equals(">")){
			return MORE;
		}else if(op.equals("<")){
			return LESS;
		}else{
			throw new ToyException("bad operator");
		}
	}
	public TypeInfo typeCheck(TypeEnv tenv) throws TypeException{
		String op = operator();
		if("=".equals(op)){
			return typeCheckForAssign(tenv);
		}else{
			leftType = left().typeCheck(tenv);
			rightType = right().typeCheck(tenv);
			if("+".equals(op)){
				return leftType.plus(rightType, tenv);
			}else if("==".equals(op)){
				return TypeInfo.INT;
			}else{
				leftType.assertSubtypeOf(TypeInfo.INT, tenv, this);
				rightType.assertSubtypeOf(TypeInfo.INT, tenv, this);
				return TypeInfo.INT;
			}
		}
	}
	protected TypeInfo typeCheckForAssign(TypeEnv tenv) throws TypeException{
		rightType = right().typeCheck(tenv);
		ASTree le = left();
		if(le instanceof Name){
			return ((Name) le).typeCheckForAssign(tenv, rightType);
		}else{
			throw new TypeException("bad assignment", this);
		}
	}
}
