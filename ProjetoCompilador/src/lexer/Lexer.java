package lexer;

import java.io.IOException;

import java.io.InputStream;
import java.util.HashMap;

import Exception.CompilerException;

public class Lexer {

	private InputStream input;
	private int nextChar;
	private HashMap<String, TokenType> palavrasReservadas;

	public Lexer() {
		this.nextChar = -1;
		listaPalavrasReservadas();

	}

	public void reset(InputStream in) throws CompilerException {
		try {
			// contem o codigo fonte a ser analisado
			this.input = in;
			// anda para o primeiro byte da entrada
			this.nextChar = in.read();

		} catch (IOException e) {
			throw new CompilerException(e.getMessage());
		}

	}

	private int readByte() throws CompilerException {
		int theByte;

		try {
			theByte = input.read();
		} catch (IOException e) {
			throw new CompilerException(e.getMessage());
		}

		return theByte;
	}


	public Token nextToken() throws CompilerException {
		String lexema = null;
		char aux;
		TokenType tipo = TokenType.EOF;

		

		while (nextChar == ' ' || nextChar == '\n' || nextChar == '\t' || nextChar == 13) {
			nextChar = this.readByte();
		}
		
		// Verifica o fim do arquivo
		if (this.nextChar == -1) {
					return new Token(TokenType.EOF);
		}

		if (nextChar == '+') { // Operadores Aritméticos
			tipo = TokenType.MAIS;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '-') {
			tipo = TokenType.MENOS;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '*') {
			tipo = TokenType.VEZES;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '/') {
			tipo = TokenType.DIVISAO;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '%') {
			tipo = TokenType.MODULO;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '(') { // Símbolos Especiais
			tipo = TokenType.ABRE_PAR;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == ')') {
			tipo = TokenType.FECHA_PAR;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '{') {
			tipo = TokenType.ABRE_CHA;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '}') {
			tipo = TokenType.FECHA_CHA;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == ':') {
			tipo = TokenType.DOIS_PONTOS;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == ',') {
			tipo = TokenType.VIRGULA;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == ';') {
			tipo = TokenType.PT_VIRG;
			lexema = "" + (char) nextChar;
			this.nextChar = this.readByte();
		} else if (nextChar == '!' && this.readByte() == '=') { // Diferente
			tipo = TokenType.DIFERENTE;
			lexema = "";
			this.nextChar = this.readByte();
		} else if (nextChar == '>') {
			nextChar = this.readByte();
			aux = '>';
			if (nextChar == '=') {
				tipo = TokenType.MAIOR_IGUAL;
				lexema = aux + "" + (char) nextChar;
				this.nextChar = this.readByte();
			} else {
				tipo = TokenType.MAIOR_QUE;
				lexema = "" + aux;
			}
		} else if (nextChar == '<') {
			nextChar = this.readByte();
			aux = '<';
			if (nextChar == '=') {
				tipo = TokenType.MENOR_IGUAL;
				lexema = aux + "" + (char) nextChar;
				this.nextChar = this.readByte();
			} else {
				tipo = TokenType.MENOR_QUE;
				lexema = "" + aux;
			}
		} else if (nextChar == '=') {// Atribuição || Comparação
			aux = '=';
			nextChar = this.readByte();
			if (nextChar == '=') {
				tipo = TokenType.COMPARACAO;
				lexema = aux + "" + (char) nextChar;
				this.nextChar = this.readByte();
			} else {
				tipo = TokenType.ATRIBUICAO;
				lexema = "" + aux;
			}
		} else if (nextChar >= '0' && nextChar <= '9') {// Números e
														// Identificadores
			aux = (char) nextChar;
			lexema = "" + aux;
			nextChar = this.readByte();
			if (Character.isLetter((char) nextChar)) {
				tipo = TokenType.IDENT;
				
				if((Character.isDigit((char) nextChar) && (Character.isLetter((char) nextChar+1)))){
					
					while (Character.isDigit((char) nextChar)
							|| Character.isLetter((char) nextChar)) {
						lexema += (char) nextChar;
						nextChar = this.readByte();
					}
				}
				
				
			} else if (Character.isDigit((char) nextChar)) {
				tipo = TokenType.NUMERO_INT;
				lexema += (char) nextChar;
				while (Character.isDigit((char) nextChar)) {
					nextChar = this.readByte();
					if (nextChar == '.') {
						tipo = TokenType.NUMERO_REAL;
						lexema += (char) nextChar;
						nextChar = this.readByte();
					} else if (Character.isDigit((char) nextChar)) {
						lexema += (char) nextChar;
					}
				}
			} else if (nextChar == '.') {
				aux = (char) nextChar;
				tipo = TokenType.NUMERO_REAL;
				lexema += aux;
				nextChar = this.readByte();
				while (Character.isDigit((char) nextChar)) {
					lexema += (char) nextChar;
					nextChar = this.readByte();
				}
			} else {
				tipo = TokenType.NUMERO_INT;
			}
		} else if (Character.isLetter((char) nextChar)) {// Palavras reservadas
			String palavra = "" + (char) nextChar;
			nextChar = this.readByte();
			while (Character.isLetter((char) nextChar)
					|| Character.isDigit((char) nextChar)) {
				palavra += (char) nextChar;
				nextChar = this.readByte();
			}
			if (palavrasReservadas.containsKey(palavra)) {
				tipo = palavrasReservadas.get(palavra);
				lexema = palavra;

			} else {
				tipo = TokenType.IDENT;
				lexema = palavra;

			}
		} else if(nextChar == '\"'){
			
			lexema = "" + (char) nextChar;
			nextChar = readByte();

			
			while(true){
				if(nextChar == '\\' && this.readByte() != 't'){
					
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == '\\' && this.readByte() != 'n'){
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == ' '){
					
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == ','){
					
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == '('){
					
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == ')'){
					
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == ':'){
					
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if (nextChar >= '0' && nextChar <= '9') {
					lexema += (char) nextChar;
					nextChar = readByte();

				} else if (Character.isLetter((char) nextChar)) {
					lexema += (char) nextChar;
					nextChar = readByte();
				}else if(nextChar == '\"'){
					
					break;
					
					
				}else{
					throw new CompilerException("Caracter inesperado: "
							+ (char) nextChar);
					
				}
			
			}
			tipo = TokenType.STRING_LIT;
			lexema += (char) nextChar;
			nextChar = readByte();
			
			
		}else if (nextChar == '\'') {// Caracteres Literais
		
			lexema = "" + (char) nextChar;
			nextChar = readByte();

			if (nextChar == '\\') {
				lexema += (char) nextChar;
				nextChar = readByte();
				if (nextChar == 'n' || nextChar == 't') {
					lexema += (char) nextChar;
					nextChar = readByte();
				} else {
					throw new CompilerException("Caracter inesperado: "
							+ (char) nextChar);
				}

			} else if (nextChar >= '0' && nextChar <= '9') {
				lexema += (char) nextChar;
				nextChar = readByte();

			} else if (Character.isLetter((char) nextChar)) {
				lexema += (char) nextChar;
				nextChar = readByte();
			}else if(nextChar == ' '){
				lexema += (char) nextChar;
				nextChar = readByte();
			}else{
				throw new CompilerException("Caracter inesperado: "
						+ (char) nextChar);
			}
			
			if (nextChar == '\'') {
				tipo = TokenType.CARACTERES_LIT;
				lexema += (char) nextChar;
				nextChar = readByte();
			} else {
				throw new CompilerException("Caracter inesperado: "
						+ (char) nextChar);
			}
		} else {
			throw new CompilerException("Caracter inesperado: "
					+ (char) nextChar);
		}
		
		
	
		return new Token(tipo, lexema);
	}

	public void listaPalavrasReservadas() {
		palavrasReservadas = new HashMap<String, TokenType>();
		palavrasReservadas.put("if", TokenType.IF);
		palavrasReservadas.put("else", TokenType.ELSE);
		palavrasReservadas.put("while", TokenType.WHILE);
		palavrasReservadas.put("int", TokenType.INT);
		palavrasReservadas.put("float", TokenType.FLOAT);
		palavrasReservadas.put("char", TokenType.CHAR);
		palavrasReservadas.put("call", TokenType.CALL);
		palavrasReservadas.put("print", TokenType.PRINT);
		palavrasReservadas.put("return", TokenType.RETURN);
		palavrasReservadas.put("def", TokenType.DEF);
		palavrasReservadas.put("and", TokenType.AND);
		palavrasReservadas.put("not", TokenType.NOT);
		palavrasReservadas.put("or", TokenType.OR);
		palavrasReservadas.put("string", TokenType.STRING);
	}
}
