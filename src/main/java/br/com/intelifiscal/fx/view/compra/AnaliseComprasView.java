package br.com.intelifiscal.fx.view.compra;

import br.com.intelifiscal.dto.relatorio.AnaliseComprasDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Tela de Análise Detalhada de Compras.
 */
public class AnaliseComprasView extends BorderPane {

    //==================================================
    // FILTROS
    //==================================================

    private final ComboBox<String> cbPeriodo =
            new ComboBox<>();

    private final DatePicker dtInicio =
            new DatePicker();

    private final DatePicker dtFim =
            new DatePicker();

    private final ComboBox<String> cbFornecedor =
            new ComboBox<>();

    private final ComboBox<String> cbProduto =
            new ComboBox<>();

    private final Button btConsultar =
            new Button("🔎 Consultar");

    private final Button btFechar =
            new Button("✖ Fechar");


    //==================================================
    // INDICADORES
    //==================================================

    private final Label lblTotalComprado =
            new Label("R$ 0,00");

    private final Label lblNotas =
            new Label("0");

    private final Label lblFornecedores =
            new Label("0");

    private final Label lblProdutos =
            new Label("0");

    private final Label lblTicketMedio =
            new Label("R$ 0,00");


    //==================================================
    // TABELA
    //==================================================

    private final TableView<AnaliseComprasDTO> tabela =
            new TableView<>();


    //==================================================
    // FORMATAÇÃO
    //==================================================

    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(
                    new Locale("pt", "BR")
            );


    //==================================================
    // CONSTRUTOR
    //==================================================

    public AnaliseComprasView() {

        configurarView();

        criarCabecalho();

        criarFiltros();

        criarIndicadores();

        criarTabela();

        criarRodape();
    }


    //==================================================
    // CONFIGURAÇÃO DA VIEW
    //==================================================

    private void configurarView() {

        setPadding(new Insets(20));

        setStyle(
                "-fx-background-color: #f4f7fb;"
        );
    }


    //==================================================
    // CABEÇALHO
    //==================================================

    private void criarCabecalho() {

        VBox cabecalho =
                new VBox(5);

        Label titulo =
                new Label("Análise de Compras");

        titulo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #123b70;"
        );

        Label subtitulo =
                new Label(
                        "Análise detalhada das compras realizadas."
                );

        subtitulo.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #333333;"
        );

        cabecalho.getChildren().addAll(
                titulo,
                subtitulo
        );

        setTop(cabecalho);
    }


    //==================================================
    // FILTROS
    //==================================================

    private void criarFiltros() {

        GridPane filtros =
                new GridPane();

        filtros.setHgap(10);

        filtros.setVgap(8);

        filtros.setPadding(
                new Insets(15, 0, 15, 0)
        );


        //==================================================
        // PERÍODO
        //==================================================

        Label lblPeriodo =
                new Label("Período:");


        cbPeriodo.getItems().clear();

        cbPeriodo.getItems().addAll(
                "Últimos 30 dias",
                "Últimos 3 meses",
                "Últimos 6 meses",
                "Últimos 12 meses",
                "Personalizado"
        );


        cbPeriodo.setValue(
                "Últimos 12 meses"
        );


        cbPeriodo.setPrefWidth(170);


        //==================================================
        // DATA INICIAL
        //==================================================

        Label lblDe =
                new Label("De:");


        dtInicio.setPrefWidth(125);


        //==================================================
        // DATA FINAL
        //==================================================

        Label lblAte =
                new Label("Até:");


        dtFim.setPrefWidth(125);


        //==================================================
        // POSICIONAMENTO
        //==================================================

        filtros.add(
                lblPeriodo,
                0,
                0
        );


        filtros.add(
                cbPeriodo,
                1,
                0
        );


        filtros.add(
                lblDe,
                2,
                0
        );


        filtros.add(
                dtInicio,
                3,
                0
        );


        filtros.add(
                lblAte,
                4,
                0
        );


        filtros.add(
                dtFim,
                5,
                0
        );


        //==================================================
        // ÁREA CENTRAL
        //==================================================

        VBox centro =
                new VBox();


        centro.getChildren().addAll(
                filtros,
                criarTituloTabela(),
                tabela
        );


        VBox.setMargin(
                tabela,
                new Insets(5, 0, 0, 0)
        );


        setCenter(centro);
    }

    //==================================================
    // INDICADORES
    //==================================================

    private void criarIndicadores() {

        // Os indicadores são criados em criarAreaIndicadores().
    }


    private VBox criarAreaIndicadores() {

        HBox linha1 =
                new HBox(15);

        linha1.setAlignment(
                Pos.CENTER_LEFT
        );


        linha1.getChildren().addAll(

                criarCard(
                        "Valor total comprado",
                        lblTotalComprado
                ),

                criarCard(
                        "Notas fiscais",
                        lblNotas
                ),

                criarCard(
                        "Fornecedores",
                        lblFornecedores
                ),

                criarCard(
                        "Produtos",
                        lblProdutos
                ),

                criarCard(
                        "Ticket médio",
                        lblTicketMedio
                )
        );


        VBox area =
                new VBox();

        area.setPadding(
                new Insets(5, 0, 15, 0)
        );

        area.getChildren().add(
                linha1
        );

        return area;
    }


    //==================================================
    // CARD DE INDICADOR
    //==================================================

    private VBox criarCard(
            String titulo,
            Label valor
    ) {

        Label lblTitulo =
                new Label(titulo);

        lblTitulo.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #555555;"
        );


        valor.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #123b70;"
        );


        VBox card =
                new VBox(6);

        card.setPadding(
                new Insets(12)
        );

        if (titulo.equals("Valor total comprado")) {
            card.setPrefWidth(220);
        } else {
            card.setPrefWidth(170);
        }

        card.setMinHeight(75);

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 8;"
        );

        card.getChildren().addAll(
                lblTitulo,
                valor
        );

        return card;
    }


    //==================================================
    // TÍTULO DA TABELA
    //==================================================

    private HBox criarTituloTabela() {

        Label titulo =
                new Label(
                        "Compras detalhadas"
                );

        titulo.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );


        Label subtitulo =
                new Label(
                        "Detalhamento das compras por fornecedor e produto."
                );

        subtitulo.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #555555;"
        );


        VBox textos =
                new VBox(3);

        textos.getChildren().addAll(
                titulo,
                subtitulo
        );


        HBox box =
                new HBox();

        box.getChildren().add(
                textos
        );

        return box;
    }


    //==================================================
    // TABELA
    //==================================================


    private void criarTabela() {

        //==================================================
        // FORNECEDOR
        //==================================================

        TableColumn<AnaliseComprasDTO, String> colFornecedor =
                new TableColumn<>("Fornecedor");

        colFornecedor.setPrefWidth(300);

        colFornecedor.setCellValueFactory(
                new PropertyValueFactory<>("fornecedor")
        );

        colFornecedor.setSortable(true);


        //==================================================
        // NOTAS
        //==================================================

        TableColumn<AnaliseComprasDTO, Integer> colNotas =
                new TableColumn<>("Notas");

        colNotas.setPrefWidth(90);

        colNotas.setCellValueFactory(
                new PropertyValueFactory<>("notas")
        );

        colNotas.setSortable(true);


        //==================================================
        //  DATA DA ÚLTIMA COMPRA
        //==================================================

        TableColumn<AnaliseComprasDTO, LocalDate> colDataUltimaCompra =
                new TableColumn<>("Data da Última Compra");

        colDataUltimaCompra.setPrefWidth(150);

        colDataUltimaCompra.setSortable(true);

        colDataUltimaCompra.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleObjectProperty<>(
                                cellData.getValue().getDataUltimaCompra()
                        )
        );

        colDataUltimaCompra.setCellFactory(
                coluna -> new javafx.scene.control.TableCell<
                        AnaliseComprasDTO,
                        LocalDate>() {

                    private final java.time.format.DateTimeFormatter formato =
                            java.time.format.DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy"
                            );

                    @Override
                    protected void updateItem(
                            LocalDate item,
                            boolean empty) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(formato.format(item));
                        }
                    }
                }
        );


        //==================================================
        // QUANTIDADE
        //==================================================

        TableColumn<AnaliseComprasDTO, Double> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setPrefWidth(130);

        colQuantidade.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleObjectProperty<>(
                                cellData.getValue().getQuantidade()
                        )
        );

        colQuantidade.setSortable(true);


        //==================================================
        // FORMATAÇÃO DA QUANTIDADE
        // Sem casas decimais
        // Com separador de milhar
        //==================================================

        colQuantidade.setCellFactory(
                coluna -> new javafx.scene.control.TableCell<
                        AnaliseComprasDTO,
                        Double>() {

                    @Override
                    protected void updateItem(
                            Double item,
                            boolean empty) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    String.format(
                                            new Locale("pt", "BR"),
                                            "%,.0f",
                                            item
                                    )
                            );
                        }
                    }
                }
        );


        //==================================================
        // VALOR TOTAL
        //==================================================

        TableColumn<
                AnaliseComprasDTO,
                java.math.BigDecimal
                > colValor =
                new TableColumn<>("Valor Total");

        colValor.setPrefWidth(150);

        colValor.setSortable(true);

        colValor.setCellValueFactory(
                cellData -> {

                    java.math.BigDecimal valor =
                            cellData.getValue().getValorTotal();

                    if (valor == null) {
                        valor = java.math.BigDecimal.ZERO;
                    }

                    return new javafx.beans.property.SimpleObjectProperty<>(
                            valor
                    );
                }
        );


        //==================================================
        // FORMATAÇÃO DO VALOR TOTAL
        //==================================================

        colValor.setCellFactory(
                coluna -> new javafx.scene.control.TableCell<
                        AnaliseComprasDTO,
                        java.math.BigDecimal>() {

                    @Override
                    protected void updateItem(
                            java.math.BigDecimal item,
                            boolean empty) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    moeda.format(item)
                            );
                        }
                    }
                }
        );


        //==================================================
        // PARTICIPAÇÃO
        //==================================================

        TableColumn<
                AnaliseComprasDTO,
                java.math.BigDecimal
                > colParticipacao =
                new TableColumn<>("Participação");

        colParticipacao.setPrefWidth(120);

        colParticipacao.setSortable(true);

        colParticipacao.setCellValueFactory(
                cellData -> {

                    java.math.BigDecimal participacao =
                            cellData.getValue().getParticipacao();

                    if (participacao == null) {
                        participacao = java.math.BigDecimal.ZERO;
                    }

                    return new javafx.beans.property.SimpleObjectProperty<>(
                            participacao
                    );
                }
        );


        //==================================================
        // FORMATAÇÃO DA PARTICIPAÇÃO
        //==================================================

        colParticipacao.setCellFactory(
                coluna -> new javafx.scene.control.TableCell<
                        AnaliseComprasDTO,
                        java.math.BigDecimal>() {

                    private final NumberFormat percentual =
                            NumberFormat.getNumberInstance(
                                    new Locale("pt", "BR")
                            );

                    {
                        percentual.setMinimumFractionDigits(2);
                        percentual.setMaximumFractionDigits(2);
                    }

                    @Override
                    protected void updateItem(
                            java.math.BigDecimal item,
                            boolean empty) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    percentual.format(item) + "%"
                            );
                        }
                    }
                }
        );


        //==================================================
        // ADICIONA AS COLUNAS
        //==================================================

        tabela.getColumns().addAll(
                colFornecedor,
                colNotas,
                colDataUltimaCompra,
                colQuantidade,
                colValor,
                colParticipacao
        );


        //==================================================
        // PLACEHOLDER
        //==================================================

        tabela.setPlaceholder(
                new Label(
                        "Nenhum dado encontrado."
                )
        );


        //==================================================
        // TAMANHO
        //==================================================

        tabela.setPrefHeight(350);


        //==================================================
        // REDIMENSIONAMENTO
        //==================================================

        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
    }


    //==================================================
    // RODAPÉ
    //==================================================

    private void criarRodape() {

        HBox rodape =
                new HBox();

        rodape.setAlignment(
                Pos.CENTER_RIGHT
        );

        rodape.setPadding(
                new Insets(15, 0, 0, 0)
        );

        rodape.getChildren().add(
                btFechar
        );

        setBottom(rodape);
    }


    //==================================================
    // GETTERS
    //==================================================

    public ComboBox<String> getCbPeriodo() {
        return cbPeriodo;
    }


    public DatePicker getDtInicio() {
        return dtInicio;
    }


    public DatePicker getDtFim() {
        return dtFim;
    }


    public ComboBox<String> getCbFornecedor() {
        return cbFornecedor;
    }


    public ComboBox<String> getCbProduto() {
        return cbProduto;
    }


    public Button getBtConsultar() {
        return btConsultar;
    }


    public Button getBtFechar() {
        return btFechar;
    }


    public TableView<AnaliseComprasDTO> getTabela() {
        return tabela;
    }


    public Label getLblTotalComprado() {
        return lblTotalComprado;
    }


    public Label getLblNotas() {
        return lblNotas;
    }


    public Label getLblFornecedores() {
        return lblFornecedores;
    }


    public Label getLblProdutos() {
        return lblProdutos;
    }


    public Label getLblTicketMedio() {
        return lblTicketMedio;
    }


    //==================================================
    // CONVERTER MOEDA
    //==================================================

    private double converterMoedaParaDouble(String valor) {

        if (valor == null || valor.isBlank()) {
            return 0.0;
        }

        try {

            String numero = valor
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            return Double.parseDouble(numero);

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }


    //==================================================
    // CONVERTER PERCENTUAL
    //==================================================

    private double converterPercentualParaDouble(String valor) {

        if (valor == null || valor.isBlank()) {
            return 0.0;
        }

        try {

            String numero = valor
                    .replace("%", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            return Double.parseDouble(numero);

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }
}