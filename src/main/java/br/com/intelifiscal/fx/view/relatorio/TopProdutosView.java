package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.dto.relatorio.TopProdutosDTO;
import br.com.intelifiscal.service.relatorio.TopProdutosService;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TopProdutosView extends BorderPane {

    private final TopProdutosService service;

    private final ComboBox<Integer> cbLimite;

    private final TableView<TopProdutosDTO> tabela;

    private final TableColumn<TopProdutosDTO, Integer> colPosicao;
    private final TableColumn<TopProdutosDTO, String> colCodigo;
    private final TableColumn<TopProdutosDTO, String> colDescricao;
    private final TableColumn<TopProdutosDTO, BigDecimal> colQuantidade;
    private final TableColumn<TopProdutosDTO, BigDecimal> colValorTotal;

    private final Button btConsultar;

    public TopProdutosView() {

        service = new TopProdutosService();

        setPadding(new Insets(20));

        // ============================================================
        // TÍTULO
        // ============================================================

        Label titulo = new Label("Produtos mais vendidos");
        titulo.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitulo = new Label(
                "Ranking dos produtos vendidos nos últimos 12 meses."
        );

        VBox cabecalho = new VBox(5);
        cabecalho.getChildren().addAll(titulo, subtitulo);

        // ============================================================
        // FILTRO
        // ============================================================

        Label lblTop = new Label("Quantidade de produtos:");

        cbLimite = new ComboBox<>();
        cbLimite.getItems().addAll(5, 10, 20, 50);
        cbLimite.setValue(10);
        cbLimite.setPrefWidth(100);

        btConsultar = new Button("Consultar");

        HBox filtros = new HBox(
                10,
                lblTop,
                cbLimite,
                btConsultar
        );

        filtros.setAlignment(Pos.CENTER_LEFT);

        VBox topo = new VBox(
                15,
                cabecalho,
                filtros
        );

        topo.setPadding(new Insets(0, 0, 15, 0));

        setTop(topo);

        // ============================================================
        // TABELA
        // ============================================================

        tabela = new TableView<>();

        colPosicao = new TableColumn<>("Posição");
        colCodigo = new TableColumn<>("Código");
        colDescricao = new TableColumn<>("Produto");
        colQuantidade = new TableColumn<>("Quantidade");
        colValorTotal = new TableColumn<>("Valor Total");

        colPosicao.setCellValueFactory(
                cell -> new SimpleObjectProperty<>(
                        cell.getValue().getPosicao()
                )
        );

        colCodigo.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getCodigoProduto()
                )
        );

        colDescricao.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        cell.getValue().getDescricao()
                )
        );

        colQuantidade.setCellValueFactory(
                cell -> new SimpleObjectProperty<>(
                        cell.getValue().getQuantidadeVendida()
                )
        );

        colValorTotal.setCellValueFactory(
                cell -> new SimpleObjectProperty<>(
                        cell.getValue().getValorVendido()
                )
        );

        // ============================================================
// FORMATAÇÃO DAS COLUNAS
// ============================================================

        NumberFormat formatoQuantidade = NumberFormat.getNumberInstance(
                new Locale("pt", "BR")
        );

        formatoQuantidade.setGroupingUsed(true);
        formatoQuantidade.setMinimumFractionDigits(0);
        formatoQuantidade.setMaximumFractionDigits(0);

        colQuantidade.setCellFactory(coluna ->
                new TableCell<TopProdutosDTO, BigDecimal>() {

                    @Override
                    protected void updateItem(BigDecimal valor, boolean empty) {

                        super.updateItem(valor, empty);

                        if (empty || valor == null) {
                            setText(null);
                        } else {
                            setText(formatoQuantidade.format(valor));
                        }
                    }
                }
        );

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(
                new Locale("pt", "BR")
        );

        colValorTotal.setCellFactory(coluna ->
                new TableCell<TopProdutosDTO, BigDecimal>() {

                    @Override
                    protected void updateItem(BigDecimal valor, boolean empty) {

                        super.updateItem(valor, empty);

                        if (empty || valor == null) {
                            setText(null);
                        } else {
                            setText(formatoMoeda.format(valor));
                        }
                    }
                }
        );

        colPosicao.setPrefWidth(80);
        colCodigo.setPrefWidth(130);
        colDescricao.setPrefWidth(350);
        colQuantidade.setPrefWidth(130);
        colValorTotal.setPrefWidth(150);

        // Permite ordenação nas colunas.
        tabela.getSortOrder().add(colQuantidade);
        colQuantidade.setSortType(
                TableColumn.SortType.DESCENDING
        );

        tabela.getColumns().addAll(
                colPosicao,
                colCodigo,
                colDescricao,
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

    public TableView<TopProdutosDTO> getTabela() {
        return tabela;
    }

    public TableColumn<TopProdutosDTO, BigDecimal> getColQuantidade() {
        return colQuantidade;
    }

    public void exibirDados(List<TopProdutosDTO> lista) {

        tabela.setItems(
                FXCollections.observableArrayList(lista)
        );

        tabela.getSortOrder().clear();
        tabela.getSortOrder().add(colQuantidade);

        colQuantidade.setSortType(
                TableColumn.SortType.DESCENDING
        );

        tabela.sort();
    }

    public void exibirErro(String mensagem) {

        Alert alerta = new Alert(
                Alert.AlertType.ERROR
        );

        alerta.setTitle("Erro");
        alerta.setHeaderText(
                "Erro ao consultar produtos mais vendidos"
        );
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }

}