package br.com.intelifiscal.fx.view.produto;

import br.com.intelifiscal.dto.produto.ProdutoVinculoDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProdutosVinculadosView extends BorderPane {

    // ============================================================
    // PESQUISA
    // ============================================================

    private final TextField txtPesquisa =
            new TextField();

    private final Button btLimpar =
            new Button("🧹 Limpar");


    // ============================================================
    // TABELA
    // ============================================================

    private final TableView<ProdutoVinculoDTO> tabela =
            new TableView<>();

    private final ObservableList<ProdutoVinculoDTO> dados =
            FXCollections.observableArrayList();

    private final ContextMenu menuContexto =
            new ContextMenu();

    private final MenuItem miDesvincular =
            new MenuItem("🔓 Desvincular produtos");


    // ============================================================
    // CONTADOR
    // ============================================================

    private final Label lblContador =
            new Label("0 vínculos encontrados");


    // ============================================================
    // BOTÕES
    // ============================================================

    private final Button btDesvincular =
            new Button("🔓 Desvincular");

    private final Button btFechar =
            new Button("✖ Fechar");


    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public ProdutosVinculadosView() {

        criarLayout();

        configurarTabela();
    }


    // ============================================================
    // LAYOUT
    // ============================================================

    private void criarLayout() {

        setPadding(new Insets(20));


        // ========================================================
        // CABEÇALHO
        // ========================================================

        Label titulo =
                new Label("🔗 Produtos Vinculados");

        titulo.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );


        Label subtitulo =
                new Label(
                        "Consulte e gerencie os vínculos entre produtos de compra e venda."
                );

        subtitulo.setStyle(
                "-fx-text-fill: #666666;"
        );


        VBox cabecalho =
                new VBox(
                        5,
                        titulo,
                        subtitulo
                );

        cabecalho.setPadding(
                new Insets(0, 0, 15, 0)
        );


        // ========================================================
        // PESQUISA
        // ========================================================

        Label lblPesquisa =
                new Label("Pesquisar:");

        txtPesquisa.setPromptText(
                "Código ou descrição do produto"
        );

        HBox.setHgrow(
                txtPesquisa,
                Priority.ALWAYS
        );


        HBox pesquisa =
                new HBox(
                        10,
                        lblPesquisa,
                        txtPesquisa,
                        btLimpar
                );

        pesquisa.setAlignment(
                Pos.CENTER_LEFT
        );

        pesquisa.setPadding(
                new Insets(0, 0, 15, 0)
        );


        // ========================================================
        // CONTADOR
        // ========================================================

        lblContador.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #17365D;"
        );


        // ========================================================
        // CENTRO
        // ========================================================

        VBox centro =
                new VBox(
                        pesquisa,
                        lblContador,
                        tabela
                );

        VBox.setMargin(
                lblContador,
                new Insets(0, 0, 8, 0)
        );

        VBox.setVgrow(
                tabela,
                Priority.ALWAYS
        );


        // ========================================================
        // BOTÕES INFERIORES
        // ========================================================

        btDesvincular.setPrefWidth(125);
        btFechar.setPrefWidth(100);


        Region espaco =
                new Region();

        HBox.setHgrow(
                espaco,
                Priority.ALWAYS
        );


        HBox botoes =
                new HBox(
                        10,
                        btDesvincular,
                        espaco,
                        btFechar
                );

        botoes.setAlignment(
                Pos.CENTER_RIGHT
        );

        botoes.setPadding(
                new Insets(15, 0, 0, 0)
        );


        // ========================================================
        // MONTAGEM
        // ========================================================

        setTop(cabecalho);

        setCenter(centro);

        setBottom(botoes);
    }


    // ============================================================
    // CONFIGURAR TABELA
    // ============================================================

    private void configurarTabela() {

        // --------------------------------------------------------
        // CÓDIGO COMPRA
        // --------------------------------------------------------

        TableColumn<ProdutoVinculoDTO, String> colCodigoCompra =
                new TableColumn<>("Código Compra");

        colCodigoCompra.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getCodigoCompra()
                                )
                        )
        );

        colCodigoCompra.setPrefWidth(110);


        // --------------------------------------------------------
        // DESCRIÇÃO COMPRA
        // --------------------------------------------------------

        TableColumn<ProdutoVinculoDTO, String> colDescricaoCompra =
                new TableColumn<>("Descrição Compra");

        colDescricaoCompra.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getDescricaoCompra()
                                )
                        )
        );

        colDescricaoCompra.setPrefWidth(240);


        // --------------------------------------------------------
        // UNIDADE COMPRA
        // --------------------------------------------------------

        TableColumn<ProdutoVinculoDTO, String> colUnidadeCompra =
                new TableColumn<>("UN.");

        colUnidadeCompra.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getUnidadeCompra()
                                )
                        )
        );

        colUnidadeCompra.setPrefWidth(55);


        // --------------------------------------------------------
        // CÓDIGO VENDA
        // --------------------------------------------------------

        TableColumn<ProdutoVinculoDTO, String> colCodigoVenda =
                new TableColumn<>("Código Venda");

        colCodigoVenda.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getCodigoVenda()
                                )
                        )
        );

        colCodigoVenda.setPrefWidth(110);


        // --------------------------------------------------------
        // DESCRIÇÃO VENDA
        // --------------------------------------------------------

        TableColumn<ProdutoVinculoDTO, String> colDescricaoVenda =
                new TableColumn<>("Descrição Venda");

        colDescricaoVenda.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getDescricaoVenda()
                                )
                        )
        );

        colDescricaoVenda.setPrefWidth(240);


        // --------------------------------------------------------
        // UNIDADE VENDA
        // --------------------------------------------------------

        TableColumn<ProdutoVinculoDTO, String> colUnidadeVenda =
                new TableColumn<>("UN.");

        colUnidadeVenda.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getUnidadeVenda()
                                )
                        )
        );

        colUnidadeVenda.setPrefWidth(55);


        // --------------------------------------------------------
        // ADICIONA COLUNAS
        // --------------------------------------------------------

        tabela.getColumns().addAll(
                colCodigoCompra,
                colDescricaoCompra,
                colUnidadeCompra,
                colCodigoVenda,
                colDescricaoVenda,
                colUnidadeVenda
        );


        tabela.setItems(dados);


        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        tabela.setPlaceholder(
                new Label(
                        "Nenhum produto vinculado encontrado."
                )
        );

        menuContexto.getItems().setAll(
                miDesvincular
        );

        tabela.setContextMenu(
                menuContexto
        );

    }


    // ============================================================
    // VALOR SEGURO
    // ============================================================

    private String valorSeguro(String valor) {

        return valor == null
                ? ""
                : valor;
    }


    // ============================================================
    // GETTERS
    // ============================================================

    public TextField getTxtPesquisa() {

        return txtPesquisa;
    }

    public Button getBtLimpar() {

        return btLimpar;
    }

    public Button getBtDesvincular() {

        return btDesvincular;
    }

    public Button getBtFechar() {

        return btFechar;
    }

    public TableView<ProdutoVinculoDTO> getTabela() {

        return tabela;
    }

    public ObservableList<ProdutoVinculoDTO> getDados() {

        return dados;
    }

    public Label getLblContador() {

        return lblContador;
    }

    public MenuItem getMiDesvincular() {
        return miDesvincular;
    }
}