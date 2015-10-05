package Parser;

import java.io.InputStream;

import Exception.CompilerException;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public class Parser {

	private Lexer lexer;
	private Token currentToken;

	public Parser() {
		lexer = new Lexer();
	}

	public String parse(InputStream input) throws CompilerException {

		// reinicia o lexer e lê o primeiro token
		lexer.reset(input);
		currentToken = lexer.nextToken();
		parseProgram();

		// tenta reconhecer algo que case com o símbolo "program",
		// que eh o símbolo inicial da gramática
		// TODO

		// se nao der exceção antes de chegar aqui, então o programa
		// está sintaticamente correto
		return "Sintaxe OK";
	}

	private void acceptToken() throws CompilerException {
		currentToken = lexer.nextToken();

	}

	private void acceptToken(TokenType tp) throws CompilerException {
		if (currentToken.getTipo() == tp) {
			currentToken = lexer.nextToken();

		} else {
			throw new CompilerException("Token inesperado: " + "foi lido um \""
					+ currentToken.getTipo() + "\", quando o esperado era \""
					+ tp + "\".");
		}

	}

	//<programa> ::= <decl_global>*
	private void parseProgram() throws CompilerException {
		parseDeclGlobal();
		while (currentToken.getTipo() != TokenType.EOF) {
			parseDeclGlobal();
		}

	}

	//<decl_global> :: = <decl_variavel> | <decl_funcao>
	private void parseDeclGlobal() throws CompilerException {
		if (currentToken.getTipo() == TokenType.IDENT) {
			acceptToken();

			parseDeclVarGlobal();
		} else if (currentToken.getTipo() == TokenType.DEF) {
			parseDeclFunc();
		} else {
			throw new CompilerException("Expressão inválida: " + currentToken);
		}
	}


	//<decl_variavel> ::= <lista_idents> ":" <tipo> ";"
	private void parseDeclVarGlobal() throws CompilerException {
		parseListaIdents();
		acceptToken(TokenType.DOIS_PONTOS);
		parseListaTipos();
		acceptToken(TokenType.PT_VIRG);
	}


	//O primeiro identificador sempre é consumido pelos métodos que chamam parseListaIdents()

	//<lista_idents> ::= IDENTIFICADOR ("," IDENTIFICADOR)*
	private void parseListaIdents() throws CompilerException {
		while (currentToken.getTipo() == TokenType.VIRGULA) {
			acceptToken();
			acceptToken(TokenType.IDENT);
		}
	}

	//<tipo> ::= "int" | "char" | "float" | "string"
	private void parseListaTipos() throws CompilerException {
		parseTipo();
		while (currentToken.getTipo() == TokenType.VIRGULA) {
			acceptToken();
			parseTipo();
		}
	}

	//Tipos Básicos
	private void parseTipo() throws CompilerException {
		if (currentToken.getTipo() == TokenType.CHAR
				|| currentToken.getTipo() == TokenType.INT
				|| currentToken.getTipo() == TokenType.FLOAT
				|| currentToken.getTipo() == TokenType.STRING) {
			acceptToken();

		} else {
			throw new CompilerException("Expressão inválida: " + currentToken);
		}
	}

	//<decl_funcao> ::= <assinatura> <bloco>
	private void parseDeclFunc() throws CompilerException {
		parseAssinatura();
		parseBloco();
	}

	//<assinatura> ::= "def" IDENTIFICADOR "(" <param_formais> ")" ":" <tipo>
	//				   |"def" IDENTIFICADOR "(" <param_formais> ")"
	private void parseAssinatura() throws CompilerException {
		acceptToken(TokenType.DEF);
		if (currentToken.getTipo() == TokenType.IDENT) {
			acceptToken();
			acceptToken(TokenType.ABRE_PAR);
			parseParamFormais();
			acceptToken(TokenType.FECHA_PAR);
			if (currentToken.getTipo() == TokenType.DOIS_PONTOS) {
				acceptToken();
				parseListaTipos();
			} else {
				// Não faz Nada
			}

		}
	}

	//<param_formais> ::= IDENTIFICADOR ":" <tipo> ( "," IDENTIFICADOR ":" <tipo> )* | vazio
	private void parseParamFormais() throws CompilerException {
		if (currentToken.getTipo() == TokenType.IDENT) {
			acceptToken();
			acceptToken(TokenType.DOIS_PONTOS);
			parseTipo();
			while (currentToken.getTipo() == TokenType.VIRGULA) {
				acceptToken();
				acceptToken(TokenType.IDENT);
				acceptToken(TokenType.DOIS_PONTOS);
				parseTipo();

			}
		} else {
			// Não faz Nada
		}
	}

	//<bloco> ::= "{" <lista_comandos> "}"
	private void parseBloco() throws CompilerException {
		acceptToken(TokenType.ABRE_CHA);
		parseListaComandos();
		acceptToken(TokenType.FECHA_CHA);

	}

	//<lista_comandos> ::= (<comando>)*
	private void parseListaComandos() throws CompilerException {
		while (currentToken.getTipo() == TokenType.IDENT
				|| currentToken.getTipo() == TokenType.WHILE
				|| currentToken.getTipo() == TokenType.IF
				|| currentToken.getTipo() == TokenType.PRINT
				|| currentToken.getTipo() == TokenType.RETURN
				|| currentToken.getTipo() == TokenType.CALL) {
			parseComando();

		}
	}

	//<comando> ::= <decl_variavel> | <atribuicao> | <iteracao> | <decisao>	| <escrita>	| <retorno>	| <bloco> | <chamada_func_cmd>
	private void parseComando() throws CompilerException {
		if (currentToken.getTipo() == TokenType.CALL) {
			parseChamadaFuncCmd();
		} else if (currentToken.getTipo() == TokenType.IDENT) {
			acceptToken();
			parseListaIdents();
			if (currentToken.getTipo() == TokenType.DOIS_PONTOS) {
				parseDeclaracao();
			} else if (currentToken.getTipo() == TokenType.ATRIBUICAO) {
				parseAtribuicao();
			}else {
				throw new CompilerException("Expressão inválida: " + currentToken);				
			}

		} else if (currentToken.getTipo() == TokenType.WHILE) {
			parseIteracao();
		} else if (currentToken.getTipo() == TokenType.IF) {
			parseDecisao();
		} else if (currentToken.getTipo() == TokenType.PRINT) {
			parseEscrita();
		} else if (currentToken.getTipo() == TokenType.RETURN) {
			parseRetorno();
		} else if (currentToken.getTipo() == TokenType.ABRE_CHA) {
			parseBloco();
		} else {
			// Não Faz Nada
		}
	}


	// <declaracao> ::= <listaIdents> ":" <listaTipos>";"
	private void parseDeclaracao() throws CompilerException {
		acceptToken(TokenType.DOIS_PONTOS);
		parseListaTipos();
		acceptToken(TokenType.PT_VIRG);
	}

	//<atribuicao> ::= <listaIdents> "=" <expr>";"
	private void parseAtribuicao() throws CompilerException {
		acceptToken(TokenType.ATRIBUICAO);
		parseExpr();
		acceptToken(TokenType.PT_VIRG);
	}


	//<iteracao> ::= "while" "(" <expr> ")" <comando>
	private void parseIteracao() throws CompilerException {
		acceptToken(TokenType.WHILE);
		if (currentToken.getTipo() == TokenType.ABRE_PAR) {
			acceptToken();
			parseExpr();
			acceptToken(TokenType.FECHA_PAR);
			parseComando();

		}
	}

	
	//<decisao> ::= if "("<expr>")" <comando> <restoDecisao>
	private void parseDecisao() throws CompilerException {
		acceptToken(TokenType.IF);
		acceptToken(TokenType.ABRE_PAR);
		parseExpr();
		acceptToken(TokenType.FECHA_PAR);
		parseComando();
		parseRestoDecisao();

	}

	
	//<restoDecisao> ::= else <comando> | <VAZIO>
	private void parseRestoDecisao() throws CompilerException {
		if (currentToken.getTipo() == TokenType.ELSE) {
			acceptToken();
			parseComando();

		} else {
			// Não faz Nada
		}
	}

	
	//<escrita> ::= "print" "(" <expr> ")" ";"
	private void parseEscrita() throws CompilerException {
		acceptToken(TokenType.PRINT);
		acceptToken(TokenType.ABRE_PAR);
		parseExpr();
		acceptToken(TokenType.FECHA_PAR);
		acceptToken(TokenType.PT_VIRG);

	}

	
	// <retorno> ::= "return" <listaExpr> ";"
	private void parseRetorno() throws CompilerException {
		acceptToken(TokenType.RETURN);
		parseExpr();
		acceptToken(TokenType.PT_VIRG);

	}

	//<chamada_func_cmd> ::= "call" <chamada_func> ";"
	private void parseChamadaFuncCmd() throws CompilerException {
		acceptToken(TokenType.CALL);
		acceptToken(TokenType.IDENT);
		parseChamFunc();
		acceptToken(TokenType.PT_VIRG);
	}
	
	//<chamada_func> ::= IDENTIFICADOR "(" <lista_exprs> ")" ";"
	private void parseChamFunc() throws CompilerException {
		acceptToken(TokenType.ABRE_PAR);
		parseListaExpr();
		acceptToken(TokenType.FECHA_PAR);
	}

	//<lista_exprs> ::= vazio | <expressao> ("," <expressao>)*
	private void parseListaExpr() throws CompilerException {
		if (currentToken.getTipo() == TokenType.NUMERO_INT
				|| currentToken.getTipo() == TokenType.CHAR
				|| currentToken.getTipo() == TokenType.NUMERO_REAL
				|| currentToken.getTipo() == TokenType.IDENT
				|| currentToken.getTipo() == TokenType.STRING) {
			acceptToken();
			parseExpr();
			parseRestoListaExpr();

		} else {
			// Não faz nada
		}

	}

	
	// <restoListaExpr> ::= ("," <expr>)*
	private void parseRestoListaExpr() throws CompilerException {
		while (currentToken.getTipo() == TokenType.VIRGULA) {
			acceptToken();
			parseExpr();

		}
	}
	
	
	// <expr> ::= <exprA>
	private void parseExpr() throws CompilerException {
		parseExprA();

	}

	
	//<exprA> ::= <exprB> <restoExprA>
	private void parseExprA() throws CompilerException {
		parseExprB();
		parseRestoExprA();

	}

	
	// <restoExprA> ::= ("or"<exprB>) | ("and"<exprB>)
	private void parseRestoExprA() throws CompilerException {
		if (currentToken.getTipo() == TokenType.OR) {
			acceptToken();
			parseExprB();

		} else if (currentToken.getTipo() == TokenType.AND) {
			acceptToken();
			parseExprB();

		}
	}

	
	// <exprB> ::= <exprC> <restoExprB>
	private void parseExprB() throws CompilerException {
		parseExprC();
		parseRestoExprB();

	}

	
	// <restoExprB> ::= ("=="<exprC>) | ("!="<exprC>) | ("<"<exprC>) |
	// (">"<exprC>) | ("<="<exprC>) | (">="<exprC>)
	private void parseRestoExprB() throws CompilerException {
		if (currentToken.getTipo() == TokenType.COMPARACAO) {
			acceptToken();
			parseExprC();

		} else if (currentToken.getTipo() == TokenType.DIFERENTE) {
			acceptToken();
			parseExprC();

		} else if (currentToken.getTipo() == TokenType.MENOR_QUE) {

			acceptToken();
			parseExprC();

		} else if (currentToken.getTipo() == TokenType.MAIOR_QUE) {

			acceptToken();
			parseExprC();

		} else if (currentToken.getTipo() == TokenType.MAIOR_IGUAL) {

			acceptToken();
			parseExprC();
		} else if (currentToken.getTipo() == TokenType.MENOR_IGUAL) {

			acceptToken();
			parseExprC();
		}
	}

	
	// <exprC> ::= <exprD> <restoExprC>
	private void parseExprC() throws CompilerException {
		parseExprD();
		parseRestoExprC();

	}

	
	// <restoExprC> ::= ("+"<exprD>) | ("-"<exprD>)
	private void parseRestoExprC() throws CompilerException {
		if (currentToken.getTipo() == TokenType.MAIS) {
			acceptToken();
			parseExprD();
		} else if (currentToken.getTipo() == TokenType.MENOS) {
			acceptToken();
			parseExprD();

		}
	}

	
	//<exprD> ::= <exprE> <restoExprD>
	private void parseExprD() throws CompilerException {
		parseExprE();
		parseRestoExprD();

	}

	
	//<restoExprD> ::= ("*"<exprE>) | ("/"<exprE>) | ("%"<exprE>)
	private void parseRestoExprD() throws CompilerException {
		if (currentToken.getTipo() == TokenType.VEZES) {
			acceptToken();
			parseExprE();

		} else if (currentToken.getTipo() == TokenType.DIVISAO) {
			acceptToken();
			parseExprE();

		} else if (currentToken.getTipo() == TokenType.MODULO) {
			acceptToken();
			parseExprE();

		}
	}

	
	//<exprE> ::= <exprBasica> <restoExprE>
	private void parseExprE() throws CompilerException {
		parseExprBasica();
		parseRestoExprE();

	}

	
	//<restoExprE> ::= ("not"<exprBasica>) | ("-"<exprBasica>)
	private void parseRestoExprE() throws CompilerException {
		if (currentToken.getTipo() == TokenType.NOT) {
			acceptToken();
			parseExprBasica();

		} else if (currentToken.getTipo() == TokenType.MENOS) {

			acceptToken();
			parseExprBasica();

		}
	}
	
	//<expr_basica> ::= "(" <expressao> ")"	| "not" <expressao_basica>	| "-" <expressao_basica>	| INT_LITERAL
	//| CHAR_LITERAL | FLOAT_LITERAL	| STRING_LITERAL	| IDENTIFICADOR	| <chamada_func>
	private void parseExprBasica() throws CompilerException {
		if (currentToken.getTipo() == TokenType.ABRE_PAR) {

			acceptToken();
			parseExpr();
			acceptToken(TokenType.FECHA_PAR);
		} else if (currentToken.getTipo() == TokenType.NUMERO_INT
				|| currentToken.getTipo() == TokenType.NUMERO_REAL
				|| currentToken.getTipo() == TokenType.CHAR
				|| currentToken.getTipo() == TokenType.STRING) {
			acceptToken();
		} else if (currentToken.getTipo() == TokenType.CARACTERES_LIT) {
			acceptToken();
		} else if (currentToken.getTipo() == TokenType.STRING_LIT) {
			acceptToken();
		} else if (currentToken.getTipo() == TokenType.IDENT) {
			acceptToken();
			if (currentToken.getTipo() == TokenType.ABRE_PAR) {
				parseChamFunc();
			} else {
				// Não faz nada
			}
		} 
	}


	





}
