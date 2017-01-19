package com.toy.compiler;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

	private LineNumberReader reader;
	private ArrayList<Token> tokens = new ArrayList<>();
	private boolean hasMore;
	public static String regexPat
      = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
        + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
	private Pattern pattern = Pattern.compile(regexPat);
	public Lexer(Reader r){
		hasMore = true;
		reader = new LineNumberReader(r);
	}
	public Token read(){
		if(tokens.size() <= 0){
			readLine();
			if(hasMore){
				return tokens.remove(0);
			}else{
				return Token.EOF;
			}
		}else{
			return tokens.remove(0);
		}
	}
	public Token peek(int i){
		while(tokens.size() <= i){
			readLine();
			if(hasMore){
			}else{
				return Token.EOF;
			}
		}
		return tokens.get(i);
	}
	
	protected void readLine(){
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(line == null){
			hasMore = false;
			return;
		}
		int lineNumber = reader.getLineNumber();
		Matcher matcher = pattern.matcher(line);
		matcher.useAnchoringBounds(true).useAnchoringBounds(false);
		int pos = 0;
		int endPos = line.length();
		while(pos < endPos){
			matcher.region(pos, endPos);
			if(matcher.lookingAt()){
				addToken(lineNumber, matcher);
				pos = matcher.end();
			}else{
				throw new ToyException("bad token at line"+lineNumber);
			}
		}
		tokens.add(new IdToken(lineNumber, Token.EOL));
	}
	protected void addToken(int lineNo,Matcher matcher) {
		String m =  matcher.group(1);
		if(m != null){
			if(matcher.group(2) == null){
				 Token token;
	                if (matcher.group(3) != null)
	                    token = new NumToken(lineNo, Integer.parseInt(m));
	                else if (matcher.group(4) != null)
	                    token = new StrToken(lineNo, toStringLiteral(m));
	                else
	                    token = new IdToken(lineNo, m);
	                tokens.add(token);
			}
		}
	}
	protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\')
                    c = s.charAt(++i);
                else if (c2 == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
	protected static class NumToken extends Token{
		
		private int value;
		protected NumToken(int line,int v) {
			super(line);
			value = v;
		}
		public boolean isNumber(){return true;}
		public String getText() {
			return Integer.toString(value);
		}
		public int getNumber(){
			return value;
		}
	}
    protected static class IdToken extends Token {
        private String text; 
        protected IdToken(int line, String id) {
            super(line);
            text = id;
        }
        public boolean isIdentifier() { return true; }
        public String getText() { return text; }
    }

    protected static class StrToken extends Token {
        private String literal;
        StrToken(int line, String str) {
            super(line);
            literal = str;
        }
        public boolean isString() { return true; }
        public String getText() { return literal; }
    }
}
