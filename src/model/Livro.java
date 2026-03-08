package model; // Indica que este arquivo pertence ao pacote model

public class Livro {
    private String titulo;
    private String autor;
    private int anoPublicacao;

    // Construtor: define como um livro é criado
    public Livro(String titulo, String autor, int anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }

    // Método para exibir os dados no CMD
    public void exibirDados() {
        System.out.println("Título: " + titulo + " | Autor: " + autor + " | Ano: " + anoPublicacao);
    }
	
	// Método para transformar o livro em uma linha de texto para o arquivo .txt
public String paraLinhaArquivo() {
    return titulo + ";" + autor + ";" + anoPublicacao;
}
}