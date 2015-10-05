package lexer;

public enum TokenType {
	IDENT,			//[0-9]*[a-zA-Z]([a-zA-Z]|[0-9])* 
	NUMERO_INT,		//Inteiro Literal
	NUMERO_REAL, 	//Numero Real
	MAIOR_QUE,		//Maior Que
	MENOR_QUE,		//Menor que
	MENOR_IGUAL, 	//Menor Igual
	MAIOR_IGUAL, 	//Maior Igual
	COMPARACAO,		//Igual
	DIFERENTE,		//Diferente
	MAIS,			//Soma
	MENOS,			//Subtraçãp
	VEZES,			//Multiplicação
	DIVISAO,		//Divisão
	MODULO,			//Modulo
	AND,			//Operador AND
	OR,				//Operador Or
	NOT,			//Operador Neg
	ATRIBUICAO,		//Atribuição
	PT_VIRG, 		//Ponto e Virgula
	ABRE_PAR,
	FECHA_PAR,
	ABRE_CHA,
	FECHA_CHA,
	DOIS_PONTOS,
	VIRGULA,
	MAIN,
	IF,
	ELSE,
	WHILE,
	INT,
	FLOAT,
	CHAR,
	CALL,
	PRINT,
	RETURN,
	CARACTERES_LIT,
	STRING_LIT,
	DEF,
	STRING,
	NEG,
	EOF
}
