import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Biblioteca;
import model.Livro;

public class InterfaceGrafica extends Application {

    private Biblioteca minhaBiblioteca = new Biblioteca();
    private TableView<Livro> tabela = new TableView<>();
    private ObservableList<Livro> listaObservavel;

    @Override
    public void start(Stage palco) {
        // 1. Carregar dados existentes
        minhaBiblioteca.carregarArquivo();
        listaObservavel = FXCollections.observableArrayList(minhaBiblioteca.getAcervo());

        // 2. Configurar colunas da tabela
        TableColumn<Livro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Livro, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));

        TableColumn<Livro, Integer> colAno = new TableColumn<>("Ano");
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));

        tabela.getColumns().clear(); // Limpa colunas duplicadas se houver
        tabela.getColumns().addAll(colTitulo, colAutor, colAno);
        tabela.setItems(listaObservavel);

        // 3. Criar campos de entrada
        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título");
        
        TextField campoAutor = new TextField();
        campoAutor.setPromptText("Autor");
        
        TextField campoAno = new TextField();
        campoAno.setPromptText("Ano (Ex: 2024)");

        // 4. Criar botões
        Button btnCadastrar = new Button("Cadastrar Livro");
        btnCadastrar.setMaxWidth(Double.MAX_VALUE); // Botão ocupa largura toda

        Button btnRemover = new Button("Remover Selecionado");
        btnRemover.setMaxWidth(Double.MAX_VALUE);
        btnRemover.setStyle("-fx-base: #ff6666;"); // Cor avermelhada para alerta

        // --- AÇÃO DE CADASTRAR ---
        btnCadastrar.setOnAction(e -> {
            String t = campoTitulo.getText().trim();
            String a = campoAutor.getText().trim();
            String anoTxt = campoAno.getText().trim();

            if (t.isEmpty() || a.isEmpty() || anoTxt.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!").show();
                return;
            }

            try {
                int ano = Integer.parseInt(anoTxt);
                if (ano < 1450 || ano > 2026) {
                    new Alert(Alert.AlertType.WARNING, "Ano inválido!").show();
                    return;
                }

                Livro novo = new Livro(t, a, ano);
                minhaBiblioteca.adicionar(novo);
                minhaBiblioteca.salvarArquivo();
                listaObservavel.add(novo);

                campoTitulo.clear();
                campoAutor.clear();
                campoAno.clear();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Ano deve ser um número!").show();
            }
        });

        // --- AÇÃO DE REMOVER ---
        btnRemover.setOnAction(e -> {
            Livro selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                minhaBiblioteca.remover(selecionado);
                listaObservavel.remove(selecionado);
            } else {
                new Alert(Alert.AlertType.WARNING, "Selecione um livro na tabela para remover!").show();
            }
        });

        // 5. Organizar Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
            new Label("Novo Livro:"), campoTitulo, campoAutor, campoAno, 
            btnCadastrar, btnRemover, 
            new Separator(), new Label("Acervo Atual:"), tabela
        );

        Scene cena = new Scene(layout, 500, 600);
        palco.setTitle("Gerenciador de Biblioteca");
        palco.setScene(cena);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}