package br.com.intelifiscal.fx.view.nfe;

import br.com.intelifiscal.entity.NFe;
import br.com.intelifiscal.fx.components.common.Card;
import br.com.intelifiscal.fx.components.common.CrudButtonBar;
import br.com.intelifiscal.fx.components.common.SectionTitle;
import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.util.FormatadorNumero;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.ComboBox;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GerenciarNfeView extends BaseView {

    //==================================================
    // PESQUISA
    //==================================================

    private final TextField txtPesquisa =
            new TextField();


    //==================================================
    // TABELA
    //==================================================

    private final TableView<NFe> tabelaNfe =
            new TableView<>();


    //==================================================
    // DETALHES
    //==================================================

    private final TextField txtNumero =
            criarCampoConsulta();

    private final TextField txtModelo =
            criarCampoConsulta();

    private final ComboBox<String> cbTipo =
            new ComboBox<>();

    private final TextField txtDataEmissao =
            criarCampoConsulta();

    private final TextField txtEmitente =
            criarCampoConsulta();

    private final TextField txtCnpjEmitente =
            criarCampoConsulta();

    private final TextField txtMunicipioEmitente =
            criarCampoConsulta();

    private final TextField txtUfEmitente =
            criarCampoConsulta();

    private final TextField txtDestinatario =
            criarCampoConsulta();

    private final TextField txtCnpjDestinatario =
            criarCampoConsulta();

    private final TextField txtMunicipioDestinatario =
            criarCampoConsulta();

    private final TextField txtUfDestinatario =
            criarCampoConsulta();

    private final TextField txtValorTotal =
            criarCampoConsulta();

    private final TextField txtSituacao =
            criarCampoConsulta();

    private final TextField txtChave =
            criarCampoConsulta();


    //==================================================
    // BOTÕES
    //==================================================

    private final CrudButtonBar crudButtonBar =
            new CrudButtonBar();


    //==================================================
    // SCROLL
    //==================================================

    private ScrollPane scrollPane;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public GerenciarNfeView() {

        super(
                "Gerenciar NF-e",
                "Consulta e gerenciamento das Notas Fiscais Eletrônicas"
        );

        initialize();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void initialize() {

        //---------------------------------------------
        // PESQUISA
        //---------------------------------------------

        txtPesquisa.setPromptText(
                "Pesquisar por NF, chave, emitente, destinatário ou CNPJ (ex. 00123654000199)"
        );

        txtPesquisa.setPrefWidth(650);

        cbTipo.getItems().setAll(
                "Compra",
                "Venda"
        );

        cbTipo.setPrefHeight(34);

        cbTipo.setMaxWidth(Double.MAX_VALUE);

        cbTipo.setDisable(true);

        HBox barraPesquisa =
                new HBox(
                        12,
                        txtPesquisa
                );

        barraPesquisa.setAlignment(
                Pos.CENTER_LEFT
        );


        //---------------------------------------------
        // TÍTULO
        //---------------------------------------------

        SectionTitle secNfe =
                new SectionTitle(
                        "Notas Fiscais Eletrônicas"
                );


        //---------------------------------------------
        // TABELA
        //---------------------------------------------

        configurarTabelaNfe();


        //---------------------------------------------
        // DETALHES
        //---------------------------------------------

        SectionTitle secDetalhes =
                new SectionTitle(
                        "Dados da NF-e selecionada"
                );

        Label lblAvisoEdicao =
                new Label(
                        "Atenção: nesta tela, ao selecionar uma NF-e somente o campo [Tipo] pode ser alterado. "
                                + "Para remover a NF-e, utilize o botão Excluir."
                );

        lblAvisoEdicao.setWrapText(true);

        lblAvisoEdicao.setStyle(
                "-fx-text-fill: #1E5EFF; " +
                        "-fx-font-weight: bold;"
        );

        GridPane gridDetalhes =
                criarGridDetalhes();

        VBox painelDetalhes =
                new VBox(
                        8,
                        secDetalhes,
                        lblAvisoEdicao,
                        gridDetalhes
                );


        //---------------------------------------------
        // BOTÕES
        //---------------------------------------------

        crudButtonBar.getBtSalvar().setDisable(true);
        crudButtonBar.getBtExcluir().setDisable(true);
        crudButtonBar.getBtNovo().setDisable(true);


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

                secNfe,

                tabelaNfe,

                painelDetalhes,

                crudButtonBar
        );


        //---------------------------------------------
        // CARD
        //---------------------------------------------

        Card card =
                new Card(conteudo);

        card.setWidthPercentage(0.92);

        card.setMaxContentWidth(1200);


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
    // TABELA DE NF-e
    //==================================================

    private void configurarTabelaNfe() {

        tabelaNfe.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabelaNfe.setPlaceholder(
                new Label(
                        "Nenhuma NF-e encontrada."
                )
        );


        //---------------------------------------------
        // NF
        //---------------------------------------------

        TableColumn<NFe, String> colNumero =
                new TableColumn<>("NF");

        colNumero.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getNumero()
                        )
        );

        colNumero.setComparator((a, b) -> {

            try {

                return Integer.compare(
                        Integer.parseInt(a),
                        Integer.parseInt(b)
                );

            } catch (NumberFormatException e) {

                return a.compareToIgnoreCase(b);
            }
        });


        //---------------------------------------------
        // MODELO
        //---------------------------------------------

        TableColumn<NFe, String> colModelo =
                new TableColumn<>("Modelo");

        colModelo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getModelo()
                        )
        );


        //---------------------------------------------
        // TIPO
        //---------------------------------------------

        TableColumn<NFe, String> colTipo =
                new TableColumn<>("Tipo");

        colTipo.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getTipo()
                        )
        );


        //---------------------------------------------
        // EMISSÃO
        //---------------------------------------------

        TableColumn<NFe, String> colData =
                new TableColumn<>("Emissão");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colData.setCellValueFactory(
                data -> {

                    LocalDate dataEmissao =
                            data.getValue().getDataEmissao();

                    String texto =
                            dataEmissao == null
                                    ? ""
                                    : dataEmissao.format(formatter);

                    return new SimpleStringProperty(
                            texto
                    );
                }
        );

        colData.setComparator((a, b) -> {

            if (a == null || a.isBlank()) {

                return (b == null || b.isBlank())
                        ? 0
                        : -1;
            }

            if (b == null || b.isBlank()) {

                return 1;
            }

            return LocalDate.parse(
                    a,
                    formatter
            ).compareTo(
                    LocalDate.parse(
                            b,
                            formatter
                    )
            );
        });


        //---------------------------------------------
        // EMITENTE
        //---------------------------------------------

        TableColumn<NFe, String> colEmitente =
                new TableColumn<>("Emitente");

        colEmitente.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getEmitente()
                        )
        );

        colEmitente.setComparator(
                String.CASE_INSENSITIVE_ORDER
        );


        //---------------------------------------------
        // DESTINATÁRIO
        //---------------------------------------------

        TableColumn<NFe, String> colDestinatario =
                new TableColumn<>("Destinatário");

        colDestinatario.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getDestinatario()
                        )
        );

        colDestinatario.setComparator(
                String.CASE_INSENSITIVE_ORDER
        );


        //---------------------------------------------
        // VALOR
        //---------------------------------------------

        TableColumn<NFe, Number> colValor =
                new TableColumn<>("Valor");

        colValor.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorTotal()
                        )
        );

        FormatadorNumero.aplicar(
                colValor
        );


        //---------------------------------------------
        // SITUAÇÃO
        //---------------------------------------------

        TableColumn<NFe, String> colSituacao =
                new TableColumn<>("Situação");

        colSituacao.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getSituacao()
                        )
        );


        //---------------------------------------------
        // ADICIONA COLUNAS
        //---------------------------------------------

        tabelaNfe.getColumns().setAll(

                colNumero,

                colModelo,

                colTipo,

                colData,

                colEmitente,

                colDestinatario,

                colValor,

                colSituacao
        );


        //---------------------------------------------
        // LARGURAS PREFERENCIAIS
        //---------------------------------------------

        colNumero.setPrefWidth(70);

        colModelo.setPrefWidth(80);

        colTipo.setPrefWidth(90);

        colData.setPrefWidth(100);

        colEmitente.setPrefWidth(220);

        colDestinatario.setPrefWidth(220);

        colValor.setPrefWidth(120);

        colSituacao.setPrefWidth(110);


        //---------------------------------------------
        // TAMANHO
        //---------------------------------------------

        tabelaNfe.setPrefHeight(450);
    }


    //==================================================
    // GRID DE DETALHES
    //==================================================

    private GridPane criarGridDetalhes() {

        GridPane grid =
                new GridPane();

        grid.setHgap(12);

        grid.setVgap(10);

        grid.setPadding(
                new Insets(5, 0, 5, 0)
        );


        //---------------------------------------------
        // LINHA 1
        //---------------------------------------------

        grid.add(
                criarRotulo("Número"),
                0,
                0
        );

        grid.add(
                txtNumero,
                1,
                0
        );

        grid.add(
                criarRotulo("Modelo"),
                2,
                0
        );

        grid.add(
                txtModelo,
                3,
                0
        );

        grid.add(
                criarRotulo("Tipo"),
                4,
                0
        );

        grid.add(
                cbTipo,
                5,
                0
        );


        //---------------------------------------------
        // LINHA 2
        //---------------------------------------------

        grid.add(
                criarRotulo("Data de emissão"),
                0,
                1
        );

        grid.add(
                txtDataEmissao,
                1,
                1
        );

        grid.add(
                criarRotulo("Valor total"),
                2,
                1
        );

        grid.add(
                txtValorTotal,
                3,
                1
        );

        grid.add(
                criarRotulo("Situação"),
                4,
                1
        );

        grid.add(
                txtSituacao,
                5,
                1
        );


        //---------------------------------------------
        // LINHA 3
        //---------------------------------------------

        grid.add(
                criarRotulo("Emitente"),
                0,
                2
        );

        grid.add(
                txtEmitente,
                1,
                2,
                3,
                1
        );

        grid.add(
                criarRotulo("CNPJ"),
                4,
                2
        );

        grid.add(
                txtCnpjEmitente,
                5,
                2
        );


        //---------------------------------------------
        // LINHA 4
        //---------------------------------------------

        grid.add(
                criarRotulo("Município"),
                0,
                3
        );

        grid.add(
                txtMunicipioEmitente,
                1,
                3
        );

        grid.add(
                criarRotulo("UF"),
                2,
                3
        );

        grid.add(
                txtUfEmitente,
                3,
                3
        );


        //---------------------------------------------
        // LINHA 5
        //---------------------------------------------

        grid.add(
                criarRotulo("Destinatário"),
                0,
                4
        );

        grid.add(
                txtDestinatario,
                1,
                4,
                3,
                1
        );

        grid.add(
                criarRotulo("CNPJ"),
                4,
                4
        );

        grid.add(
                txtCnpjDestinatario,
                5,
                4
        );


        //---------------------------------------------
        // LINHA 6
        //---------------------------------------------

        grid.add(
                criarRotulo("Município"),
                0,
                5
        );

        grid.add(
                txtMunicipioDestinatario,
                1,
                5
        );

        grid.add(
                criarRotulo("UF"),
                2,
                5
        );

        grid.add(
                txtUfDestinatario,
                3,
                5
        );


        //---------------------------------------------
        // LINHA 7
        //---------------------------------------------

        grid.add(
                criarRotulo("Chave de acesso"),
                0,
                6
        );

        grid.add(
                txtChave,
                1,
                6,
                5,
                1
        );


        //---------------------------------------------
        // LARGURAS
        //---------------------------------------------

        grid.getColumnConstraints().clear();


        return grid;
    }


    //==================================================
    // COMPONENTES AUXILIARES
    //==================================================

    private static TextField criarCampoConsulta() {

        TextField campo =
                new TextField();

        campo.setEditable(false);

        campo.setPrefHeight(34);

        campo.setMaxWidth(Double.MAX_VALUE);

        return campo;
    }


    private static Label criarRotulo(String texto) {

        Label label =
                new Label(texto);

        label.setMinWidth(100);

        label.setAlignment(
                Pos.CENTER_RIGHT
        );

        return label;
    }


    //==================================================
    // GETTERS
    //==================================================

    public TextField getTxtPesquisa() {

        return txtPesquisa;
    }


    public TableView<NFe> getTabelaNfe() {

        return tabelaNfe;
    }


    public ScrollPane getScrollPane() {

        return scrollPane;
    }


    public TextField getTxtNumero() {

        return txtNumero;
    }


    public TextField getTxtModelo() {

        return txtModelo;
    }


    public ComboBox<String> getCbTipo() {

        return cbTipo;
    }


    public TextField getTxtDataEmissao() {

        return txtDataEmissao;
    }


    public TextField getTxtEmitente() {

        return txtEmitente;
    }


    public TextField getTxtCnpjEmitente() {

        return txtCnpjEmitente;
    }


    public TextField getTxtMunicipioEmitente() {

        return txtMunicipioEmitente;
    }


    public TextField getTxtUfEmitente() {

        return txtUfEmitente;
    }


    public TextField getTxtDestinatario() {

        return txtDestinatario;
    }


    public TextField getTxtCnpjDestinatario() {

        return txtCnpjDestinatario;
    }


    public TextField getTxtMunicipioDestinatario() {

        return txtMunicipioDestinatario;
    }


    public TextField getTxtUfDestinatario() {

        return txtUfDestinatario;
    }


    public TextField getTxtValorTotal() {

        return txtValorTotal;
    }


    public TextField getTxtSituacao() {

        return txtSituacao;
    }


    public TextField getTxtChave() {

        return txtChave;
    }


    public CrudButtonBar getCrudButtonBar() {

        return crudButtonBar;
    }

}