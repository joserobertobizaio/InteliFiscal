package br.com.intelifiscal.fx.view.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.CrudButtonBar;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.util.FormatadorNumero;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProdutoView extends BaseView {

    //==================================================
    // PESQUISA
    //==================================================

    private final TextField txtPesquisa =
            new TextField();


    //==================================================
    // FORMULÁRIO DO PRODUTO
    //==================================================

    private final TextField txtCodigoProduto =
            new TextField();

    private final TextField txtCodigoBarras =
            new TextField();

    private final TextField txtDescricao =
            new TextField();

    private final TextField txtNcm =
            new TextField();

    private final TextField txtCest =
            new TextField();

    private final TextField txtUnidade =
            new TextField();

    private final CheckBox chkAtivo =
            new CheckBox("Produto ativo");


    //==================================================
    // TABELAS
    //==================================================

    private final TableView<ProdutoDTO> tabelaProdutos =
            new TableView<>();

    private final TableView<ProdutoHistoricoDTO> tabelaHistorico =
            new TableView<>();


    //==================================================
    // FILTRO DO HISTÓRICO
    //==================================================

    private final ComboBox<String> cmbPeriodo =
            new ComboBox<>();

    private final DatePicker dtInicio =
            new DatePicker();

    private final DatePicker dtFim =
            new DatePicker();

    private final Button btConsultarHistorico =
            new Button("Consultar");


    //==================================================
    // BOTÕES
    //==================================================

    private final CrudButtonBar crudButtonBar =
            new CrudButtonBar();

    private ScrollPane scrollPane;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ProdutoView() {

        super(
                "Produtos",
                "Cadastro e consulta dos produtos"
        );

        initialize();

        configurarBotoesInicial();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void initialize() {

        //---------------------------------------------
        // PESQUISA
        //---------------------------------------------

        txtPesquisa.setPromptText(
                "Pesquisar por código, código de barras ou descrição..."
        );

        txtPesquisa.setPrefWidth(500);

        HBox barraPesquisa =
                new HBox(
                        12,
                        txtPesquisa
                );

        barraPesquisa.setAlignment(
                Pos.CENTER_LEFT
        );


        //---------------------------------------------
        // FORMULÁRIO
        //---------------------------------------------

        SectionTitle secDados =
                new SectionTitle(
                        "Dados do produto"
                );

        configurarFormulario();


        //---------------------------------------------
        // TÍTULOS
        //---------------------------------------------

        SectionTitle secProdutos =
                new SectionTitle(
                        "Produtos cadastrados"
                );

        SectionTitle secHistorico =
                new SectionTitle(
                        "Histórico do produto"
                );


        //---------------------------------------------
        // TABELAS
        //---------------------------------------------

        configurarTabela();

        configurarTabelaHistorico();


        //---------------------------------------------
        // FILTRO DO HISTÓRICO
        //---------------------------------------------

        HBox filtroHistorico =
                criarFiltroHistorico();


        //---------------------------------------------
        // CONTEÚDO
        //---------------------------------------------

        VBox conteudo =
                new VBox(18);

        conteudo.setPadding(
                new Insets(20)
        );

        conteudo.getChildren().addAll(

                barraPesquisa,

                secDados,

                criarFormulario(),

                secProdutos,

                tabelaProdutos,

                secHistorico,

                filtroHistorico,

                tabelaHistorico,

                crudButtonBar
        );


        //---------------------------------------------
        // CARD
        //---------------------------------------------

        Card card =
                new Card(conteudo);

        card.setWidthPercentage(0.92);

        card.setMaxContentWidth(1100);


        //---------------------------------------------
        // PAINEL
        //---------------------------------------------

        VBox painel =
                new VBox(card);

        painel.setPadding(
                new Insets(10)
        );


        //---------------------------------------------
        // SCROLL
        //---------------------------------------------

        scrollPane =
                new ScrollPane(painel);

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setPannable(true);


        //---------------------------------------------
        // CONTENT DA BASE
        //---------------------------------------------

        setContent(scrollPane);
    }


    //==================================================
    // CONFIGURA FILTRO DO HISTÓRICO
    //==================================================

    private HBox criarFiltroHistorico() {

        Label lblPeriodo =
                new Label("Período:");

        Label lblDe =
                new Label("De:");

        Label lblAte =
                new Label("Até:");


        cmbPeriodo.getItems().setAll(

                "Últimos 30 dias",

                "Últimos 90 dias",

                "Últimos 6 meses",

                "Últimos 12 meses",

                "Últimos 24 meses",

                "Desde o início",

                "Período personalizado"
        );


        cmbPeriodo.setValue(
                "Últimos 12 meses"
        );


        cmbPeriodo.setPrefWidth(180);

        dtInicio.setPrefWidth(120);

        dtFim.setPrefWidth(120);

        btConsultarHistorico.setPrefWidth(100);


        HBox filtro =
                new HBox(
                        10,
                        lblPeriodo,
                        cmbPeriodo,
                        lblDe,
                        dtInicio,
                        lblAte,
                        dtFim,
                        btConsultarHistorico
                );


        filtro.setAlignment(
                Pos.CENTER_LEFT
        );

        filtro.setPadding(
                new Insets(0, 0, 5, 0)
        );


        return filtro;
    }

    //==================================================
    // CONTROLE DOS CAMPOS DE PERÍODO
    //==================================================

    public void atualizarControlesPeriodo() {

        String periodo =
                cmbPeriodo.getValue();


        //---------------------------------------------
        // DESDE O INÍCIO
        //---------------------------------------------

        if ("Desde o início".equals(periodo)) {

            dtInicio.setValue(null);

            dtFim.setValue(null);

            dtInicio.setVisible(false);
            dtInicio.setManaged(false);

            dtFim.setVisible(false);
            dtFim.setManaged(false);

            dtInicio.setDisable(true);
            dtFim.setDisable(true);

            return;
        }


        //---------------------------------------------
        // MOSTRA OS CALENDÁRIOS
        //---------------------------------------------

        dtInicio.setVisible(true);
        dtInicio.setManaged(true);

        dtFim.setVisible(true);
        dtFim.setManaged(true);


        //---------------------------------------------
        // PERÍODO PERSONALIZADO
        //---------------------------------------------

        if ("Período personalizado".equals(periodo)) {

            dtInicio.setDisable(false);
            dtFim.setDisable(false);

            dtInicio.setValue(null);

            dtFim.setValue(null);


            Platform.runLater(() ->
                    dtInicio.requestFocus()
            );

            return;
        }


        //---------------------------------------------
        // PERÍODOS AUTOMÁTICOS
        //---------------------------------------------

        dtInicio.setDisable(true);
        dtFim.setDisable(true);

    }


    //==================================================
    // CONFIGURA FORMULÁRIO
    //==================================================

    private void configurarFormulario() {

        txtCodigoProduto.setPromptText(
                "Código do produto"
        );

        txtCodigoBarras.setPromptText(
                "Código de barras"
        );

        txtDescricao.setPromptText(
                "Descrição"
        );

        txtNcm.setPromptText(
                "NCM"
        );

        txtCest.setPromptText(
                "CEST"
        );

        txtUnidade.setPromptText(
                "Unidade"
        );


        txtCodigoProduto.setPrefWidth(180);

        txtCodigoBarras.setPrefWidth(220);

        txtDescricao.setPrefWidth(400);

        txtNcm.setPrefWidth(150);

        txtCest.setPrefWidth(150);

        txtUnidade.setPrefWidth(100);


        chkAtivo.setSelected(true);
    }


    //==================================================
    // CRIA FORMULÁRIO
    //==================================================

    private GridPane criarFormulario() {

        GridPane grid =
                new GridPane();

        grid.setHgap(12);

        grid.setVgap(10);

        grid.setPadding(
                new Insets(5, 0, 10, 0)
        );


        //---------------------------------------------
        // LINHA 0
        //---------------------------------------------

        Label lblCodigo =
                new Label("Código:");

        Label lblCodigoBarras =
                new Label("Código de barras:");


        grid.add(
                lblCodigo,
                0,
                0
        );

        grid.add(
                txtCodigoProduto,
                1,
                0
        );

        grid.add(
                lblCodigoBarras,
                2,
                0
        );

        grid.add(
                txtCodigoBarras,
                3,
                0
        );


        //---------------------------------------------
        // LINHA 1
        //---------------------------------------------

        Label lblDescricao =
                new Label("Descrição:");


        grid.add(
                lblDescricao,
                0,
                1
        );

        grid.add(
                txtDescricao,
                1,
                1,
                3,
                1
        );


        //---------------------------------------------
        // LINHA 2
        //---------------------------------------------

        Label lblNcm =
                new Label("NCM:");

        Label lblCest =
                new Label("CEST:");


        grid.add(
                lblNcm,
                0,
                2
        );

        grid.add(
                txtNcm,
                1,
                2
        );

        grid.add(
                lblCest,
                2,
                2
        );

        grid.add(
                txtCest,
                3,
                2
        );


        //---------------------------------------------
        // LINHA 3
        //---------------------------------------------

        Label lblUnidade =
                new Label("Unidade:");


        grid.add(
                lblUnidade,
                0,
                3
        );

        grid.add(
                txtUnidade,
                1,
                3
        );

        grid.add(
                chkAtivo,
                3,
                3
        );


        return grid;
    }


    //==================================================
    // CONFIGURA TABELA
    //==================================================

    private void configurarTabela() {

        tabelaProdutos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaProdutos.setPlaceholder(
                new Label(
                        "Nenhum produto cadastrado."
                )
        );


        //---------------------------------------------
        // COLUNAS
        //---------------------------------------------

        TableColumn<ProdutoDTO, String> colCodigo =
                new TableColumn<>("Código");

        TableColumn<ProdutoDTO, String> colCodigoBarras =
                new TableColumn<>("Código de Barras");

        TableColumn<ProdutoDTO, String> colDescricao =
                new TableColumn<>("Descrição");

        TableColumn<ProdutoDTO, String> colNcm =
                new TableColumn<>("NCM");

        TableColumn<ProdutoDTO, String> colCest =
                new TableColumn<>("CEST");

        TableColumn<ProdutoDTO, String> colUnidade =
                new TableColumn<>("Unidade");

        TableColumn<ProdutoDTO, String> colAtivo =
                new TableColumn<>("Ativo");


        tabelaProdutos.getColumns().addAll(

                colCodigo,

                colCodigoBarras,

                colDescricao,

                colNcm,

                colCest,

                colUnidade,

                colAtivo
        );


        tabelaProdutos.setPrefHeight(220);

        tabelaProdutos.setMinHeight(220);

        tabelaProdutos.setMaxHeight(220);
    }


    //==================================================
    // CONFIGURA TABELA HISTÓRICO
    //==================================================

    private void configurarTabelaHistorico() {

        tabelaHistorico.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaHistorico.setPlaceholder(
                new Label(
                        "Nenhum histórico encontrado."
                )
        );


        //---------------------------------------------
        // TIPO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colTipo =
                new TableColumn<>("Tipo");

        colTipo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getTipo()
                        )
        );


        //---------------------------------------------
        // NF
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colNf =
                new TableColumn<>("NF");

        colNf.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getNumeroNfe()
                        )
        );


        //---------------------------------------------
        // SÉRIE
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colSerie =
                new TableColumn<>("Série");

        colSerie.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getSerie()
                        )
        );


        //---------------------------------------------
        // EMISSÃO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, LocalDateTime> colData =
                new TableColumn<>("Emissão");

        colData.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getDataEmissao()
                        )
        );


        colData.setCellFactory(
                coluna ->
                        new javafx.scene.control.TableCell<>() {

                            private final DateTimeFormatter formato =
                                    DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy"
                                    );

                            @Override
                            protected void updateItem(
                                    LocalDateTime item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                if (empty || item == null) {

                                    setText(null);

                                } else {

                                    setText(
                                            formato.format(item)
                                    );
                                }
                            }
                        }
        );


        //---------------------------------------------
        // EMITENTE
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colEmitente =
                new TableColumn<>("Emitente");

        colEmitente.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getEmitente()
                        )
        );


        //---------------------------------------------
        // DESTINATÁRIO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, String> colDestinatario =
                new TableColumn<>("Destinatário");

        colDestinatario.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getDestinatario()
                        )
        );


        //---------------------------------------------
        // QUANTIDADE
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, Number> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getQuantidade()
                        )
        );

        FormatadorNumero.aplicarQuantidade(
                colQuantidade
        );


        //---------------------------------------------
        // VALOR UNITÁRIO
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, Number> colValorUnitario =
                new TableColumn<>("Valor Unitário");

        colValorUnitario.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getValorUnitario()
                        )
        );

        FormatadorNumero.aplicarValorUnitario(
                colValorUnitario
        );


        //---------------------------------------------
        // VALOR TOTAL
        //---------------------------------------------

        TableColumn<ProdutoHistoricoDTO, Number> colValorTotal =
                new TableColumn<>("Valor Total");

        colValorTotal.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue()
                                        .getValorTotal()
                        )
        );

        FormatadorNumero.aplicar(
                colValorTotal
        );


        //---------------------------------------------
        // ADICIONA COLUNAS
        //---------------------------------------------

        tabelaHistorico.getColumns().setAll(

                colTipo,

                colNf,

                colSerie,

                colData,

                colEmitente,

                colDestinatario,

                colQuantidade,

                colValorUnitario,

                colValorTotal
        );


        tabelaHistorico.setPrefHeight(220);

        tabelaHistorico.setMinHeight(220);

        tabelaHistorico.setMaxHeight(220);
    }


    //==================================================
    // GETTERS
    //==================================================

    public TextField getTxtPesquisa() {
        return txtPesquisa;
    }

    public TextField getTxtCodigoProduto() {
        return txtCodigoProduto;
    }

    public TextField getTxtCodigoBarras() {
        return txtCodigoBarras;
    }

    public TextField getTxtDescricao() {
        return txtDescricao;
    }

    public TextField getTxtNcm() {
        return txtNcm;
    }

    public TextField getTxtCest() {
        return txtCest;
    }

    public TextField getTxtUnidade() {
        return txtUnidade;
    }

    public CheckBox getChkAtivo() {
        return chkAtivo;
    }

    public ComboBox<String> getCmbPeriodo() {
        return cmbPeriodo;
    }

    public DatePicker getDtInicio() {
        return dtInicio;
    }

    public DatePicker getDtFim() {
        return dtFim;
    }

    public Button getBtConsultarHistorico() {
        return btConsultarHistorico;
    }

    public TableView<ProdutoDTO> getTabelaProdutos() {
        return tabelaProdutos;
    }

    public TableView<ProdutoHistoricoDTO> getTabelaHistorico() {
        return tabelaHistorico;
    }

    public CrudButtonBar getCrudButtonBar() {
        return crudButtonBar;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }


    //==================================================
    // CONTROLE DOS BOTÕES
    //==================================================
    public void configurarBotoesInicial() {

        crudButtonBar.getBtNovo()
                .setDisable(true);

        crudButtonBar.getBtExcluir()
                .setVisible(false);

        crudButtonBar.getBtExcluir()
                .setManaged(false);

        crudButtonBar.getBtSalvar()
                .setDisable(false);

        crudButtonBar.getBtFechar()
                .setDisable(false);
    }


    public void produtoSelecionado() {

        crudButtonBar.getBtNovo()
                .setDisable(false);

        crudButtonBar.getBtExcluir()
                .setVisible(false);

        crudButtonBar.getBtExcluir()
                .setManaged(false);

        crudButtonBar.getBtSalvar()
                .setDisable(false);

        crudButtonBar.getBtFechar()
                .setDisable(false);
    }

    public void modoEdicao() {

        crudButtonBar.getBtNovo()
                .setDisable(true);

        crudButtonBar.getBtExcluir()
                .setVisible(false);

        crudButtonBar.getBtExcluir()
                .setManaged(false);

        crudButtonBar.getBtSalvar()
                .setDisable(false);

        crudButtonBar.getBtFechar()
                .setDisable(true);
    }

}