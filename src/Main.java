import java.util.Scanner;
import model.Biblioteca;
import model.Livro; // Importamos nossa nova classe gerenciadora

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        Biblioteca minhaBiblioteca = new Biblioteca(); // Criamos a biblioteca
		minhaBiblioteca.carregarArquivo();
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("\n--- SISTEMA DE BIBLIOTECA ---");
            System.out.println("1 - Cadastrar Novo Livro");
            System.out.println("2 - Listar Acervo");
            System.out.println("3 - Sair");
            System.out.print("Escolha: ");
            
            opcao = leitor.nextInt();
            leitor.nextLine(); 

            if (opcao == 1) {
                System.out.print("Título: ");
                String t = leitor.nextLine();
                System.out.print("Autor: ");
                String a = leitor.nextLine();
                System.out.print("Ano: ");
                int ano = leitor.nextInt();

                // Agora passamos a responsabilidade para a Biblioteca
                Livro novo = new Livro(t, a, ano);
                minhaBiblioteca.adicionar(novo);
                System.out.println("Adicionado com sucesso!");

            } else if (opcao == 2) {
                // A Main não precisa saber COMO listar, ela só pede para a biblioteca fazer
                minhaBiblioteca.listarTodos();
            }
        }

        System.out.println("Até a próxima!");
        leitor.close();
    
	// Antes de fechar, pedimos para a biblioteca salvar tudo no arquivo .txt
        System.out.println("Salvando dados...");
        minhaBiblioteca.salvarArquivo();

        System.out.println("Sistema encerrado. Até logo!");
        leitor.close();
    }

    
}