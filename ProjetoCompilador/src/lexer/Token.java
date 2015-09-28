package lexer;

public class Token {

	private TokenType tipo;
	private String lexema;
	
	public Token(TokenType tipo){
		this.tipo = tipo;
	}
	
	public Token(TokenType tipo,String lexema){
		this.tipo = tipo;
		this.lexema = lexema;
	}

	public TokenType getTipo() {
		return tipo;
	}


	public String getLexema() {
		return lexema;
	}

	public String toString() {
		if (lexema == null || lexema.length() == 0) {
			return "[" + tipo + "]";	
		} else {
			return "[" + tipo + "," + lexema + "]";
		}
	}
	
	
}
