import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // Nova importação
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
    private ObservableList<Livro> listaMaster;

    @Override
    public void start(Stage palco) {
        minhaBiblioteca.carregarArquivo();
        listaMaster = FXCollections.observableArrayList(minhaBiblioteca.getAcervo());

        // --- CONFIGURAÇÃO DO FILTRO (A MÁGICA DA BUSCA) ---
        FilteredList<Livro> listaFiltrada = new FilteredList<>(listaMaster, p -> true);

        // Campo de busca
        TextField campoBusca = new TextField();
        campoBusca.setPromptText("🔍 Digite para buscar por título ou autor...");
        
        // Evento que escuta cada tecla digitada
        campoBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(livro -> {
                // Se o campo estiver vazio, mostra todos
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filtroMinusculo = newValue.toLowerCase();

                if (livro.getTitulo().toLowerCase().contains(filtroMinusculo)) {
                    return true; // Encontrou no título
                } else if (livro.getAutor().toLowerCase().contains(filtroMinusculo)) {
                    return true; // Encontrou no autor
                }
                return false; // Não encontrou nada
            });
        });

        // --- TABELA ---
        TableColumn<Livro, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<Livro, String> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        TableColumn<Livro, Integer> colAno = new TableColumn<>("Ano");
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoPublicacao"));

        tabela.getColumns().setAll(colTitulo, colAutor, colAno);
        tabela.setItems(listaFiltrada); // Agora a tabela usa a lista FILTRADA

        // --- CAMPOS DE CADASTRO ---
        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título");
        TextField campoAutor = new TextField();
        campoAutor.setPromptText("Autor");
        TextField campoAno = new TextField();
        campoAno.setPromptText("Ano");

        Button btnCadastrar = new Button("Cadastrar Livro");
        btnCadastrar.setMaxWidth(Double.MAX_VALUE);
        btnCadastrar.setOnAction(e -> {
            try {
                String t = campoTitulo.getText().trim();
                String a = campoAutor.getText().trim();
                int ano = Integer.parseInt(campoAno.getText().trim());

                if (!t.isEmpty() && !a.isEmpty()) {
                    Livro novo = new Livro(t, a, ano);
                    minhaBiblioteca.adicionar(novo);
                    minhaBiblioteca.salvarArquivo();
                    listaMaster.add(novo); // Adiciona na lista principal

                    campoTitulo.clear();
                    campoAutor.clear();
                    campoAno.clear();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar!").show();
            }
        });

        Button btnRemover = new Button("Remover Selecionado");
        btnRemover.setMaxWidth(Double.MAX_VALUE);
        btnRemover.setStyle("-fx-base: #ff6666;");
        btnRemover.setOnAction(e -> {
            Livro selecionado = tabela.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                minhaBiblioteca.remover(selecionado);
                listaMaster.remove(selecionado);
            }
        });

        // --- LAYOUT ---
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
            new Label("Busca:"), campoBusca, 
            new Separator(),
            new Label("Novo Livro:"), campoTitulo, campoAutor, campoAno, btnCadastrar,
            new Separator(),
            btnRemover, tabela
        );

        Scene cena = new Scene(layout, 550, 650);
        palco.setTitle("Biblioteca Inteligente");
        palco.setScene(cena);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}