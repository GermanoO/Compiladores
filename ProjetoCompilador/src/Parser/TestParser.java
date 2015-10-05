package Parser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestParser {

	
	//Método para testar os exemplos
	
	public static void main(String[] args) throws Exception {
		Parser parser = new Parser();
		InputStream entrada;

		System.out.println(" == TESTE DO PARSER ==\n");

		entrada = new FileInputStream("src/Código 3.txt");

		String msg = parser.parse(entrada);
		System.out.println(" >>" + msg + "\n");

		System.out.println(" == FIM ==");
	}
}

