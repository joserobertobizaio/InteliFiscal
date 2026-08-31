package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;

import javafx.collections.FXCollections;
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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.scene.control.TableCell;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;


public class ResumoVendasView extends BorderPane {

    //==================================================
    // COMPONENTES DO RESUMO
    //==================================================

    private final Label lblNotas =
            new Label("0");

    private final Label lblItens =
            new Label("0");

    private final Label lblQuantidade =
            new Label("0");

    private final Label lblValorTotal =
            new Label("R$ 0,00");

    private final Label lblTicketMedio =
            new Label("R$ 0,00");

    //==================================================
    // TABELA DE VENDAS POR CLIENTE
    //==================================================

    private final TableView<ClienteVendaDTO> tabelaClientes =
            new TableView<>();


    //==================================================
    // FILTRO DE PERÍODO
    //==================================================

    private final ComboBox<String> cbPeriodo =
            new ComboBox<>();

    private final Label lblDe =
            new Label("De:");

    private final Label lblAte =
            new Label("Até:");

    private final DatePicker dtInicio =
            new DatePicker();

    private final DatePicker dtFim =
            new DatePicker();

    private final Button btConsultar =
            new Button("🔎 Consultar");

    private final Button btExcel =
            new Button("📊 Exportar Excel");


    //==================================================
    // BOTÃO FECHAR
    //==================================================

    private final Button btFechar =
            new Button("✖ Fechar");


    //==================================================
    // FORMATAÇÃO
    //==================================================

    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(
                    new Locale("pt", "BR")
            );

    private final NumberFormat numero =
            NumberFormat.getNumberInstance(
                    new Locale("pt", "BR")
            );


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoVendasView() {

        configurarView();

        criarCabecalho();

        criarFiltroPeriodo();

        criarResumo();

        criarRodape();

        atualizarControlesPeriodo();
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
                new Label("Resumo de Vendas");

        titulo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #123b70;"
        );

        Label subtitulo =
                new Label(
                        "Análise gerencial das vendas realizadas."
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
    // FILTRO DE PERÍODO
    //==================================================

    private void criarFiltroPeriodo() {

        HBox filtro =
                new HBox(8);

        filtro.setAlignment(Pos.CENTER_LEFT);

        filtro.setPadding(
                new Insets(10, 0, 0, 0)
        );

        Label lblPeriodo =
                new Label("Período:");


        //==================================================
        // COMBOBOX
        //==================================================

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

        cbPeriodo.setPrefWidth(175);


        //==================================================
        // DATAS
        //==================================================

        dtInicio.setPrefWidth(120);
        dtFim.setPrefWidth(120);

        // Inicialmente os campos de data ficam bloqueados
        dtInicio.setDisable(true);
        dtFim.setDisable(true);

        //==================================================
        // DATA PADRÃO
        //==================================================

        LocalDate hoje =
                LocalDate.now();

        dtInicio.setValue(
                hoje.minusMonths(12)
        );

        dtFim.setValue(
                hoje
        );


        //==================================================
        // BOTÃO CONSULTAR
        //==================================================

        btConsultar.setPrefWidth(110);

        // Só aparece para período personalizado
        btConsultar.setVisible(false);
        btConsultar.setManaged(false);


        //==================================================
        // MONTAGEM
        //==================================================

        filtro.getChildren().addAll(
                lblPeriodo,
                cbPeriodo,
                lblDe,
                dtInicio,
                lblAte,
                dtFim,
                btConsultar
        );


        //==================================================
        // COLOCA O FILTRO ABAIXO DO CABEÇALHO
        //==================================================

        VBox topo =
                new VBox(5);

        topo.getChildren().addAll(
                getTop(),
                filtro
        );

        setTop(topo);

        atualizarControlesPeriodo();
    }


        //==================================================
        // ATUALIZAR DATAS CONFORME O PERÍODO
        //==================================================

    private void atualizarDatasPeriodo() {

        LocalDate hoje =
                LocalDate.now();

        String periodo =
                cbPeriodo.getValue();


        if (periodo == null) {
            return;
        }


        switch (periodo) {

            case "Últimos 30 dias":

                dtInicio.setValue(
                        hoje.minusDays(30)
                );

                dtFim.setValue(
                        hoje
                );

                break;


            case "Últimos 3 meses":

                dtInicio.setValue(
                        hoje.minusMonths(3)
                );

                dtFim.setValue(
                        hoje
                );

                break;


            case "Últimos 6 meses":

                dtInicio.setValue(
                        hoje.minusMonths(6)
                );

                dtFim.setValue(
                        hoje
                );

                break;


            case "Últimos 12 meses":

                dtInicio.setValue(
                        hoje.minusMonths(12)
                );

                dtFim.setValue(
                        hoje
                );

                break;


            case "Últimos 24 meses":

                dtInicio.setValue(
                        hoje.minusMonths(24)
                );

                dtFim.setValue(
                        hoje
                );

                break;


            case "Desde o início":

                dtInicio.setValue(
                        null
                );

                dtFim.setValue(
                        hoje
                );

                break;


            case "Período personalizado":

                // Mantém as datas escolhidas pelo usuário.

                break;
        }
    }


    //==================================================
    // RESUMO
    //==================================================

    private void criarResumo() {

        HBox grid = new HBox(15);

        grid.setAlignment(Pos.CENTER_LEFT);

        grid.setPadding(
                new Insets(25, 0, 20, 0)
        );


        grid.getChildren().addAll(
                criarCard(
                        "🧾 Notas fiscais",
                        lblNotas
                ),

                criarCard(
                        "📦 Itens",
                        lblItens
                ),

                criarCard(
                        "🔢 Quantidade",
                        lblQuantidade
                ),

                criarCard(
                        "💰 Valor total",
                        lblValorTotal
                ),

                criarCard(
                        "🎯 Ticket médio",
                        lblTicketMedio
                )
        );

        VBox areaCentral = new VBox(10);

        areaCentral.getChildren().addAll(
                grid,
                criarTabelaClientes()
        );

        setCenter(areaCentral);

    }

    //==================================================
    // TABELA DE VENDAS POR CLIENTE
    //==================================================

    private TableView<ClienteVendaDTO> criarTabelaClientes() {

        //==================================================
        // CLIENTE
        //==================================================

        TableColumn<ClienteVendaDTO, String> colunaCliente =
                new TableColumn<>("Cliente");

        colunaCliente.setCellValueFactory(
                new PropertyValueFactory<>("cliente")
        );

        colunaCliente.setPrefWidth(300);


        //==================================================
        // NOTAS
        //==================================================

        TableColumn<ClienteVendaDTO, Integer> colunaNotas =
                new TableColumn<>("Notas");

        colunaNotas.setCellValueFactory(
                new PropertyValueFactory<>("notas")
        );

        colunaNotas.setPrefWidth(90);


        //==================================================
        //  DATA DA ÚLTIMA VENDA
        //==================================================

        TableColumn<ClienteVendaDTO, LocalDate> colunaDataUltimaVenda =
                new TableColumn<>("Data da última venda");

        colunaDataUltimaVenda.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getDataUltimaVenda()
                        )
        );

        colunaDataUltimaVenda.setCellFactory(
                coluna -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            LocalDate data,
                            boolean empty
                    ) {
                        super.updateItem(data, empty);

                        if (empty || data == null) {
                            setText(null);
                        } else {
                            setText(
                                    data.format(
                                            java.time.format.DateTimeFormatter
                                                    .ofPattern("dd/MM/yyyy")
                                    )
                            );
                        }
                    }
                }
        );

        colunaDataUltimaVenda.setPrefWidth(150);


        //==================================================
        // QUANTIDADE
        //==================================================

        TableColumn<ClienteVendaDTO, Double> colunaQuantidade =
                new TableColumn<>("Quantidade");

        colunaQuantidade.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getQuantidade()
                        )
        );

        colunaQuantidade.setCellFactory(
                coluna -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Double quantidade,
                            boolean empty
                    ) {

                        super.updateItem(quantidade, empty);

                        if (empty || quantidade == null) {

                            setText(null);

                        } else {

                            NumberFormat formato =
                                    NumberFormat.getNumberInstance(
                                            new Locale("pt", "BR")
                                    );

                            formato.setMinimumFractionDigits(0);
                            formato.setMaximumFractionDigits(0);

                            setText(
                                    formato.format(quantidade)
                            );
                        }
                    }
                }
        );

        colunaQuantidade.setPrefWidth(130);


        //==================================================
        // VALOR TOTAL
        //==================================================

        TableColumn<ClienteVendaDTO, BigDecimal> colunaValorTotal =
                new TableColumn<>("Valor Total");

        colunaValorTotal.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorTotal()
                        )
        );

        colunaValorTotal.setCellFactory(
                coluna -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            BigDecimal valor,
                            boolean empty
                    ) {

                        super.updateItem(valor, empty);

                        if (empty || valor == null) {

                            setText(null);

                        } else {

                            setText(
                                    formatarValorBrasil(valor)
                            );
                        }
                    }
                }
        );

        colunaValorTotal.setPrefWidth(150);


        //==================================================
        // ADICIONA AS COLUNAS
        //==================================================

        tabelaClientes.getColumns().setAll(
                colunaCliente,
                colunaNotas,
                colunaDataUltimaVenda,
                colunaQuantidade,
                colunaValorTotal
        );


        tabelaClientes.setPrefHeight(250);

        tabelaClientes.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );


        return tabelaClientes;
    }


    //==================================================
    // CARD
    //==================================================

    private VBox criarCard(
            String titulo,
            Label valor
    ) {

        Label lblTitulo =
                new Label(titulo);

        lblTitulo.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #444444;"
        );


        valor.setStyle(
                "-fx-font-size: 19px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #123b70;"
        );


        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(15)
        );

        card.setPrefWidth(210);

        card.setPrefHeight(85);

        card.setAlignment(
                Pos.CENTER_LEFT
        );

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 10;"
        );


        card.getChildren().addAll(
                lblTitulo,
                valor
        );


        return card;
    }


    //==================================================
    // RODAPÉ
    //==================================================

    private void criarRodape() {

        HBox rodape =
                new HBox(10);

        rodape.setAlignment(
                Pos.CENTER_RIGHT
        );

        rodape.setPadding(
                new Insets(15, 0, 0, 0)
        );


        //==================================================
        // BOTÃO EXCEL
        //==================================================

        btExcel.setPrefWidth(150);


        //==================================================
        // BOTÃO FECHAR
        //==================================================

        btFechar.setPrefWidth(100);


        //==================================================
        // ADICIONAR BOTÕES
        //==================================================

        rodape.getChildren().addAll(
                btExcel,
                btFechar
        );


        setBottom(rodape);
    }


    //==================================================
    // ATUALIZAR RESUMO
    //==================================================

    public void atualizarResumo(
            ResumoVendasDTO dto
    ) {

        lblNotas.setText(
                String.valueOf(
                        dto.getNotas()
                )
        );


        lblItens.setText(
                String.valueOf(
                        dto.getItens()
                )
        );


        lblQuantidade.setText(
                formatarNumero(
                        dto.getQuantidade()
                )
        );


        lblValorTotal.setText(
                moeda.format(
                        dto.getValorTotal()
                )
        );


        lblTicketMedio.setText(
                moeda.format(
                        dto.getTicketMedio()
                )
        );
    }

    //==================================================
    // ATUALIZAR TABELA DE CLIENTES
    //==================================================

    public void atualizarClientes(
            java.util.List<ClienteVendaDTO> lista
    ) {

        tabelaClientes.setItems(
                FXCollections.observableArrayList(lista)
        );
    }


    //==================================================
    // GETTERS - FILTRO
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


    public Button getBtConsultar() {

        return btConsultar;
    }

    //==================================================
    // CONTROLE DOS FILTROS CONFORME O PERÍODO
    //==================================================

    public void atualizarControlesPeriodo() {

        String periodo = cbPeriodo.getValue();

        if (periodo == null) {
            return;
        }

        boolean personalizado =
                "Período personalizado".equals(periodo);

        boolean desdeInicio =
                "Desde o início".equals(periodo);


        //==================================================
        // PERÍODO PERSONALIZADO
        //==================================================

        if (personalizado) {

            lblDe.setVisible(true);
            lblDe.setManaged(true);

            lblAte.setVisible(true);
            lblAte.setManaged(true);

            dtInicio.setVisible(true);
            dtInicio.setManaged(true);
            dtInicio.setDisable(false);

            dtFim.setVisible(true);
            dtFim.setManaged(true);
            dtFim.setDisable(false);

            btConsultar.setVisible(true);
            btConsultar.setManaged(true);

            return;
        }


        //==================================================
        // DESDE O INÍCIO
        //==================================================

        if (desdeInicio) {

            lblDe.setVisible(false);
            lblDe.setManaged(false);

            lblAte.setVisible(false);
            lblAte.setManaged(false);

            dtInicio.setVisible(false);
            dtInicio.setManaged(false);
            dtInicio.setDisable(true);

            dtFim.setVisible(false);
            dtFim.setManaged(false);
            dtFim.setDisable(true);

            btConsultar.setVisible(false);
            btConsultar.setManaged(false);

            return;
        }


        //==================================================
        // PERÍODOS AUTOMÁTICOS
        //==================================================

        lblDe.setVisible(true);
        lblDe.setManaged(true);

        lblAte.setVisible(true);
        lblAte.setManaged(true);

        dtInicio.setVisible(true);
        dtInicio.setManaged(true);
        dtInicio.setDisable(true);

        dtFim.setVisible(true);
        dtFim.setManaged(true);
        dtFim.setDisable(true);

        btConsultar.setVisible(false);
        btConsultar.setManaged(false);
    }

    //==================================================
    // GETTER - EXCEL
    //==================================================

    public Button getBtExcel() {

        return btExcel;
    }


    //==================================================
    // GETTER - FECHAR
    //==================================================

    public Button getBtFechar() {

        return btFechar;
    }


    //==================================================
    // FORMATAÇÃO
    //==================================================

    private String formatarNumero(
            double valor
    ) {

        return numero.format(valor);
    }

    private String formatarValorBrasil(
            BigDecimal valor
    ) {

        NumberFormat formato =
                NumberFormat.getNumberInstance(
                        new Locale("pt", "BR")
                );

        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(2);

        return formato.format(valor);
    }

}