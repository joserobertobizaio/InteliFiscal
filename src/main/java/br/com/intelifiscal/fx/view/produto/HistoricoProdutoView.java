package br.com.intelifiscal.fx.view.produto;

import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.format.DateTimeFormatter;
import java.util.IdentityHashMap;
import java.util.Map;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class HistoricoProdutoView extends BorderPane {

    // ============================================================
    // FORMATADORES NUMÉRICOS - PADRÃO BRASILEIRO
    // ============================================================

    private final DecimalFormat formatoQuantidade =
            new DecimalFormat(
                    "#,##0.####",
                    DecimalFormatSymbols.getInstance(
                            Locale.forLanguageTag("pt-BR")
                    )
            );

    private final DecimalFormat formatoValor =
            new DecimalFormat(
                    "#,##0.00",
                    DecimalFormatSymbols.getInstance(
                            Locale.forLanguageTag("pt-BR")
                    )
            );

    // ============================================================
    // CAMPOS ANTIGOS — MANTIDOS PARA NÃO QUEBRAR O CONTROLLER ATUAL
    // ============================================================

    private final TextField txtCodigoProduto =
            new TextField();

    private final TextField txtDescricao =
            new TextField();

    private final DatePicker dtInicio =
            new DatePicker();

    private final DatePicker dtFim =
            new DatePicker();


    // ============================================================
    // NOVOS FILTROS
    // ============================================================

    private final CheckBox chkProdutosVendidos =
            new CheckBox("Produtos Vendidos");

    private final CheckBox chkProdutosComprados =
            new CheckBox("Produtos Comprados");

    private final ComboBox<String> cbPeriodo =
            new ComboBox<>();

    private final TextField txtPesquisa =
            new TextField();


    // ============================================================
    // BOTÕES
    // ============================================================

    private final Button btPesquisar =
            new Button("🔎 Pesquisar");

    private final Button btLimpar =
            new Button("🧹 Limpar");

    private final Button btComparar =
            new Button("⇄ Comparar selecionados");

    private final Button btVincular =
            new Button("🔗 Vincular");

    private final Button btDesvincular =
            new Button("🔓 Desvincular");

    private final Button btFechar =
            new Button("✖ Fechar");


    // ============================================================
    // TABELA
    // ============================================================

    private final TableView<ProdutoHistoricoDTO> tabela =
            new TableView<>();

    private final ObservableList<ProdutoHistoricoDTO> dados =
            FXCollections.observableArrayList();


    // ============================================================
    // CONTROLE DOS CHECKBOXES
    // ============================================================

    private final Map<ProdutoHistoricoDTO, SimpleBooleanProperty>
            selecionados = new IdentityHashMap<>();

    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public HistoricoProdutoView() {

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
                new Label("📊 Histórico de Produto");

        titulo.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        22
                )
        );


        Label subtitulo =
                new Label(
                        "Consulte produtos vendidos e comprados."
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
        // FILTRO TIPO
        // ========================================================

        chkProdutosVendidos.setSelected(true);
        chkProdutosComprados.setSelected(true);


        HBox tipos =
                new HBox(
                        20,
                        chkProdutosVendidos,
                        chkProdutosComprados
                );

        tipos.setAlignment(
                Pos.CENTER_LEFT
        );


        // ========================================================
        // PERÍODO
        // ========================================================

        Label lblPeriodo =
                new Label("Período:");

        cbPeriodo.getItems().addAll(
                "Últimos 30 dias",
                "Últimos 3 meses",
                "Últimos 6 meses",
                "Últimos 12 meses",
                "Últimos 24 meses",
                "Desde o início",
                "Período personalizado"
        );

        cbPeriodo.setValue(
                "Últimos 12 meses"
        );

        cbPeriodo.setPrefWidth(190);


        HBox periodo =
                new HBox(
                        10,
                        lblPeriodo,
                        cbPeriodo
                );

        periodo.setAlignment(
                Pos.CENTER_LEFT
        );


        // ========================================================
        // PESQUISA
        // ========================================================

        Label lblPesquisa =
                new Label("Pesquisar:");

        txtPesquisa.setPromptText(
                "Descrição, código, CNPJ ou fornecedor/emitente"
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
                        btPesquisar
                );

        pesquisa.setAlignment(
                Pos.CENTER_LEFT
        );


        // ========================================================
        // PAINEL DE FILTROS
        // ========================================================

        VBox filtros =
                new VBox(
                        12,
                        tipos,
                        periodo,
                        pesquisa
                );

        filtros.setPadding(
                new Insets(0, 0, 15, 0)
        );


        // ========================================================
        // TABELA
        // ========================================================

        VBox centro =
                new VBox(
                        filtros,
                        tabela
                );

        VBox.setVgrow(
                tabela,
                Priority.ALWAYS
        );


        setTop(cabecalho);

        setCenter(centro);


        // ========================================================
        // BOTÕES INFERIORES
        // ========================================================

        btComparar.setPrefWidth(180);
        btVincular.setPrefWidth(110);
        btDesvincular.setPrefWidth(125);
        btLimpar.setPrefWidth(100);
        btFechar.setPrefWidth(100);


        HBox botoes =
                new HBox(
                        10,
                        btComparar,
                        btVincular,
                        btDesvincular,
                        new Region(),
                        btLimpar,
                        btFechar
                );

        HBox.setHgrow(
                botoes.getChildren().get(3),
                Priority.ALWAYS
        );


        botoes.setAlignment(
                Pos.CENTER_RIGHT
        );

        botoes.setPadding(
                new Insets(15, 0, 0, 0)
        );


        setBottom(botoes);
    }


    // ============================================================
    // CONFIGURAR TABELA
    // ============================================================

    private void configurarTabela() {

        // ========================================================
        // SELEÇÃO
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, Boolean> colSelecionar =
                new TableColumn<>("☑");

        colSelecionar.setPrefWidth(45);
        colSelecionar.setMinWidth(45);
        colSelecionar.setMaxWidth(45);


        colSelecionar.setCellValueFactory(
                data -> {

                    ProdutoHistoricoDTO item =
                            data.getValue();

                    SimpleBooleanProperty propriedade =
                            selecionados.computeIfAbsent(
                                    item,
                                    chave ->
                                            new SimpleBooleanProperty(false)
                            );

                    return propriedade;
                }
        );


        colSelecionar.setCellFactory(
                coluna -> new TableCell<ProdutoHistoricoDTO, Boolean>() {

                    private final CheckBox checkBox =
                            new CheckBox();

                    /*
                     * Guarda a propriedade da linha à qual
                     * este CheckBox está atualmente ligado.
                     */
                    private SimpleBooleanProperty propriedadeAtual;


                    {
                        checkBox.setAlignment(
                                Pos.CENTER
                        );
                    }


                    @Override
                    protected void updateItem(
                            Boolean item,
                            boolean empty) {

                        super.updateItem(
                                item,
                                empty
                        );


                        // =================================================
                        // DESVINCULA A CÉLULA DA LINHA ANTERIOR
                        // =================================================

                        if (propriedadeAtual != null) {

                            checkBox.selectedProperty()
                                    .unbindBidirectional(
                                            propriedadeAtual
                                    );

                            propriedadeAtual = null;
                        }


                        // =================================================
                        // CÉLULA VAZIA
                        // =================================================

                        if (empty) {

                            setGraphic(null);

                            return;
                        }


                        // =================================================
                        // PEGA O PRODUTO DA LINHA ATUAL
                        // =================================================

                        ProdutoHistoricoDTO produto =
                                getTableView()
                                        .getItems()
                                        .get(getIndex());


                        // =================================================
                        // PEGA A PROPRIEDADE DA LINHA
                        // =================================================

                        propriedadeAtual =
                                selecionados.computeIfAbsent(
                                        produto,
                                        chave ->
                                                new SimpleBooleanProperty(false)
                                );


                        // =================================================
                        // LIGA O CHECKBOX À LINHA ATUAL
                        // =================================================

                        checkBox.selectedProperty()
                                .bindBidirectional(
                                        propriedadeAtual
                                );


                        setGraphic(checkBox);
                    }
                }
        );


        // ========================================================
        // TIPO
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colTipo =
                new TableColumn<>("Tipo");

        colTipo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getTipo()
                                )
                        )
        );

        colTipo.setPrefWidth(65);
        colTipo.setMinWidth(60);


        // ========================================================
        // NF-e
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colNumero =
                new TableColumn<>("NF-e");

        colNumero.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getNumeroNfe()
                                )
                        )
        );

        colNumero.setPrefWidth(65);
        colNumero.setMinWidth(60);

        // ========================================================
        // CÓDIGO DO PRODUTO
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colCodigoProduto =
                new TableColumn<>("Código");

        colCodigoProduto.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getCodigoProduto()
                                )
                        )
        );

        colCodigoProduto.setPrefWidth(90);
        colCodigoProduto.setMinWidth(75);



        // ========================================================
        // DESCRIÇÃO
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colDescricao =
                new TableColumn<>("Descrição");

        colDescricao.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getDescricao()
                                )
                        )
        );

        colDescricao.setPrefWidth(240);
        colDescricao.setMinWidth(180);

        // ========================================================
        // DATA
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colData =
                new TableColumn<>("Data");

        colData.setCellValueFactory(
                data -> {

                    if (data.getValue().getDataEmissao()
                            == null) {

                        return new SimpleStringProperty("");
                    }

                    return new SimpleStringProperty(
                            data.getValue()
                                    .getDataEmissao()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "dd/MM/yyyy"
                                            )
                                    )
                    );
                }
        );

        colData.setPrefWidth(85);
        colData.setMinWidth(80);

        // ========================================================
        // EMITENTE
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colEmitente =
                new TableColumn<>("Emitente");

        colEmitente.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getEmitente()
                                )
                        )
        );

        colEmitente.setPrefWidth(180);
        colEmitente.setMinWidth(150);

        // ========================================================
        // DESTINATÁRIO
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colDestinatario =
                new TableColumn<>("Destinatário");

        colDestinatario.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getDestinatario()
                                )
                        )
        );

        colDestinatario.setPrefWidth(180);
        colDestinatario.setMinWidth(150);

        // ========================================================
        // UNIDADE
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, String> colUnidade =
                new TableColumn<>("Un.");

        colUnidade.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                valorSeguro(
                                        data.getValue().getUnidade()
                                )
                        )
        );

        colUnidade.setPrefWidth(45);
        colUnidade.setMinWidth(40);

        // ========================================================
        // QUANTIDADE
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, Number> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleDoubleProperty(
                                data.getValue().getQuantidade()
                        )
        );

        colQuantidade.setPrefWidth(95);
        colQuantidade.setMinWidth(85);

        colQuantidade.setCellFactory(
                coluna -> new TableCell<ProdutoHistoricoDTO, Number>() {

                    @Override
                    protected void updateItem(
                            Number item,
                            boolean empty) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    formatoQuantidade.format(
                                            item.doubleValue()
                                    )
                            );
                        }
                    }
                }
        );

        // ========================================================
        // VALOR UNITÁRIO
        // ========================================================

        TableColumn<ProdutoHistoricoDTO, Number> colValorUnitario =
                new TableColumn<>("Valor Unitário");

        colValorUnitario.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleDoubleProperty(
                                data.getValue().getValorUnitario()
                        )
        );

        colValorUnitario.setPrefWidth(105);
        colValorUnitario.setMinWidth(95);

        colValorUnitario.setCellFactory(
                coluna -> new TableCell<ProdutoHistoricoDTO, Number>() {

                    @Override
                    protected void updateItem(
                            Number item,
                            boolean empty) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    formatoValor.format(
                                            item.doubleValue()
                                    )
                            );
                        }
                    }
                }
        );


        // ========================================================
        // ADICIONA COLUNAS
        // ========================================================

        tabela.getColumns().addAll(
                colSelecionar,
                colTipo,
                colNumero,
                colCodigoProduto,
                colDescricao,
                colData,
                colEmitente,
                colDestinatario,
                colUnidade,
                colQuantidade,
                colValorUnitario
        );


        tabela.setItems(dados);


        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        tabela.setPlaceholder(
                new Label(
                        "Nenhum histórico encontrado."
                )
        );
    }


    // ============================================================
    // VALOR SEGURO
    // ============================================================

    private String valorSeguro(
            String valor) {

        return valor == null
                ? ""
                : valor;
    }


    // ============================================================
    // GETTERS ANTIGOS
    // ============================================================

    public TextField getTxtCodigoProduto() {
        return txtCodigoProduto;
    }

    public TextField getTxtDescricao() {
        return txtDescricao;
    }

    public DatePicker getDtInicio() {
        return dtInicio;
    }

    public DatePicker getDtFim() {
        return dtFim;
    }


    // ============================================================
    // GETTERS NOVOS
    // ============================================================

    public CheckBox getChkProdutosVendidos() {
        return chkProdutosVendidos;
    }

    public CheckBox getChkProdutosComprados() {
        return chkProdutosComprados;
    }

    public ComboBox<String> getCbPeriodo() {
        return cbPeriodo;
    }

    public TextField getTxtPesquisa() {
        return txtPesquisa;
    }


    // ============================================================
    // BOTÕES
    // ============================================================

    public Button getBtPesquisar() {
        return btPesquisar;
    }

    public Button getBtLimpar() {
        return btLimpar;
    }

    public Button getBtComparar() {
        return btComparar;
    }

    public Button getBtVincular() {
        return btVincular;
    }

    public Button getBtDesvincular() {
        return btDesvincular;
    }

    public Button getBtFechar() {
        return btFechar;
    }


    // ============================================================
    // TABELA
    // ============================================================

    public TableView<ProdutoHistoricoDTO> getTabela() {
        return tabela;
    }

    public ObservableList<ProdutoHistoricoDTO> getDados() {
        return dados;
    }


    // ============================================================
    // PRODUTOS SELECIONADOS
    // ============================================================

    public ObservableList<ProdutoHistoricoDTO>
    getProdutosSelecionados() {

        ObservableList<ProdutoHistoricoDTO> lista =
                FXCollections.observableArrayList();

        for (ProdutoHistoricoDTO produto : dados) {

            SimpleBooleanProperty propriedade =
                    selecionados.get(produto);

            if (propriedade != null
                    && propriedade.get()) {

                lista.add(produto);
            }
        }

        return lista;
    }


    // ============================================================
    // LIMPAR SELEÇÕES
    // ============================================================

    public void limparSelecoes() {

        for (SimpleBooleanProperty propriedade :
                selecionados.values()) {

            propriedade.set(false);
        }
    }
}