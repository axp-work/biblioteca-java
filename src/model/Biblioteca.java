package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Livro> acervo;

    public Biblioteca() {
        this.acervo = new ArrayList<>();
    }

    public void adicionar(Livro livro) {
        acervo.add(livro);
    }

    public void listarTodos() {
        if (acervo.isEmpty()) {
            System.out.println("O acervo está vazio no momento.");
        } else {
            for (Livro l : acervo) {
                l.exibirDados();
            }
        }
    }
	
	public void salvarArquivo() {
    try (PrintWriter escritor = new PrintWriter(new FileWriter("livros.txt"))) {
        for (Livro l : acervo) {
            escritor.println(l.paraLinhaArquivo());
        }
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
	
	public void remover(Livro livro) {
    acervo.remove(livro); // Remove da lista na memória
    salvarArquivo();      // Sobrescreve o arquivo .txt já sem esse livro
}
	
	public void carregarArquivo() {
    File arquivo = new File("livros.txt");
    
    // Se o arquivo não existir (primeira vez rodando), não faz nada
    if (!arquivo.exists()) {
        return;
    }

    try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
        String linha;
        while ((linha = leitor.readLine()) != null) {
            // Divide a linha: "Titulo;Autor;2024" -> ["Titulo", "Autor", "2024"]
            String[] partes = linha.split(";");
            if (partes.length == 3) {
                String titulo = partes[0];
                String autor = partes[1];
                int ano = Integer.parseInt(partes[2]);
                
                // Adiciona o livro recuperado à lista
                acervo.add(new Livro(titulo, autor, ano));
            }
        }
        System.out.println("Dados carregados com sucesso!");
    } catch (IOException e) {
        System.out.println("Erro ao carregar dados: " + e.getMessage());
    }
}
// Este método permite que a Interface pegue a lista de livros para mostrar na tabela
public List<Livro> getAcervo() {
    return acervo;
}

}   