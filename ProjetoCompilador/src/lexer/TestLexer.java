package lexer;

import java.io.FileInputStream;


import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public class TestLexer {

	public static void main(String[] args) throws Exception {
		Lexer lexer = new Lexer();
		Token token = null;

		System.out.println(" == Teste Do Lexer ==\n");
		System.out.println(" Digite o comando terminado em ; e tecle ENTER:\n\n  ");
		

		// passa a entrada padrão para o lexer
		lexer.reset(System.in);


		do {
			token = lexer.nextToken();
			System.out.println("\t" + token);

		} while (token.getTipo() != TokenType.PT_VIRG);

		System.out.println("\n ---- CONCLUIDO -----");

	}

}
