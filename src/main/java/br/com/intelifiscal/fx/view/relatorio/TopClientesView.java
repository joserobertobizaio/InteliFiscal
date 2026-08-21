package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.service.relatorio.TopClientesService;
import br.com.intelifiscal.util.FormatadorNumero;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.List;

public class TopClientesView extends BorderPane {

    private final TopClientesService service;

    private final ComboBox<Integer> cbLimite;

    private final TableView<ClienteVendaDTO> tabela;

    private final TableColumn<ClienteVendaDTO, Integer> colPosicao;
    private final TableColumn<ClienteVendaDTO, String> colCliente;
    private final TableColumn<ClienteVendaDTO, String> colCnpj;
    private final TableColumn<ClienteVendaDTO, Integer> colNotas;
    private final TableColumn<ClienteVendaDTO, Integer> colItens;
    private final TableColumn<ClienteVendaDTO, Number> colQuantidade;
    private final TableColumn<ClienteVendaDTO, BigDecimal> colValorTotal;

    private final Button btConsultar;

    public TopClientesView() {

        service = new TopClientesService();

        setPadding(new Insets(20));

        // ============================================================
        // TÍTULO
        // ============================================================

        Label titulo =
                new Label("Maiores clientes");

        titulo.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitulo =
                new Label(
                        "Ranking dos clientes que mais compraram nos últimos 12 meses."
                );

        VBox cabecalho =
                new VBox(5);

        cabecalho.getChildren().addAll(
                titulo,
                subtitulo
        );

        // ============================================================
        // FILTRO
        // ============================================================

        Label lblTop =
                new Label("Quantidade de clientes:");

        cbLimite =
                new ComboBox<>();

        cbLimite.getItems().addAll(
                5,
                10,
                20,
                50
        );

        cbLimite.setValue(10);
        cbLimite.setPrefWidth(100);

        btConsultar =
                new Button("Consultar");

        HBox filtros =
                new HBox(
                        10,
                        lblTop,
                        cbLimite,
                        btConsultar
                );

        filtros.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox topo =
                new VBox(
                        15,
                        cabecalho,
                        filtros
                );

        topo.setPadding(
                new Insets(0, 0, 15, 0)
        );

        setTop(topo);

        // ============================================================
        // TABELA
        // ============================================================

        tabela =
                new TableView<>();

        colPosicao =
                new TableColumn<>("Posição");

        colCliente =
                new TableColumn<>("Cliente");

        colCnpj =
                new TableColumn<>("CNPJ");

        colNotas =
                new TableColumn<>("Notas");

        colItens =
                new TableColumn<>("Itens");

        colQuantidade =
                new TableColumn<>("Quantidade");

        colValorTotal =
                new TableColumn<>("Valor Total");

        // ============================================================
        // POSIÇÃO
        // ============================================================

        colPosicao.setCellFactory(
                coluna ->
                        new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    Integer item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                if (empty) {
                                    setText(null);
                                } else {
                                    setText(
                                            String.valueOf(
                                                    getIndex() + 1
                                            )
                                    );
                                }
                            }
                        }
        );

        // ============================================================
        // CLIENTE
        // ============================================================

        colCliente.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue()
                                        .getCliente()
                        )
        );

        // ============================================================
        // CNPJ
        // ============================================================

        colCnpj.setCellValueFactory(
                cell ->
                        new SimpleStringProperty(
                                cell.getValue()
                                        .getCnpj()
                        )
        );

        // ============================================================
        // NOTAS
        // ============================================================

        colNotas.setCellValueFactory(
                cell ->
                        new SimpleObjectProperty<>(
                                cell.getValue()
                                        .getNotas()
                        )
        );

        // ============================================================
        // ITENS
        // ============================================================

        colItens.setCellValueFactory(
                cell ->
                        new SimpleObjectProperty<>(
                                cell.getValue()
                                        .getItens()
                        )
        );

        // ============================================================
        // QUANTIDADE
        // ============================================================

        colQuantidade.setCellValueFactory(
                cell ->
                        new SimpleObjectProperty<>(
                                cell.getValue()
                                        .getQuantidade()
                        )
        );

        colQuantidade.setCellFactory(
                coluna ->
                        new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    Number item,
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
                                            FormatadorNumero
                                                    .formatarQuantidade(
                                                            item.doubleValue()
                                                    )
                                    );
                                }
                            }
                        }
        );

        // ============================================================
        // VALOR TOTAL
        // ============================================================

        colValorTotal.setCellValueFactory(
                cell ->
                        new SimpleObjectProperty<>(
                                cell.getValue()
                                        .getValorTotal()
                        )
        );

        colValorTotal.setCellFactory(
                coluna ->
                        new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    BigDecimal item,
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
                                            "R$ "
                                                    + FormatadorNumero
                                                    .formatar(item)
                                    );
                                }
                            }
                        }
        );

        // ============================================================
        // TAMANHOS
        // ============================================================

        colPosicao.setPrefWidth(70);
        colCliente.setPrefWidth(350);
        colCnpj.setPrefWidth(150);
        colNotas.setPrefWidth(80);
        colItens.setPrefWidth(80);
        colQuantidade.setPrefWidth(130);
        colValorTotal.setPrefWidth(160);

        // ============================================================
        // ORDENAÇÃO
        // ============================================================

        tabela.getSortOrder().add(
                colValorTotal
        );

        colValorTotal.setSortType(
                TableColumn.SortType.DESCENDING
        );

        tabela.getColumns().addAll(
                colPosicao,
                colCliente,
                colCnpj,
                colNotas,
                colItens,
                colQuantidade,
                colValorTotal
        );

        tabela.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        setCenter(tabela);
    }

    public ComboBox<Integer> getCbLimite() {
        return cbLimite;
    }

    public Button getBtConsultar() {
        return btConsultar;
    }

    public TableView<ClienteVendaDTO> getTabela() {
        return tabela;
    }

    public void exibirDados(
            List<ClienteVendaDTO> lista
    ) {

        tabela.setItems(
                FXCollections.observableArrayList(
                        lista
                )
        );

        tabela.getSortOrder().clear();

        tabela.getSortOrder().add(
                colValorTotal
        );

        colValorTotal.setSortType(
                TableColumn.SortType.DESCENDING
        );

        tabela.sort();
    }

    public void exibirErro(
            String mensagem
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle("Erro");

        alerta.setHeaderText(
                "Erro ao consultar maiores clientes"
        );

        alerta.setContentText(
                mensagem
        );

        alerta.showAndWait();
    }
}