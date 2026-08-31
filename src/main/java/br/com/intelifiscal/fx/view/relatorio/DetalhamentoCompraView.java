package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class DetalhamentoCompraView extends BorderPane {

    private final DatePicker dtInicial;
    private final DatePicker dtFinal;

    private final Button btPesquisar;
    private final Button btLimpar;

    private final TableView<DetalhamentoCompraDTO> dados;

    private final Label lbTotal;

    public DetalhamentoCompraView() {

        setPadding(new Insets(15));

        /*
         * =========================================================
         * TÍTULO
         * =========================================================
         */

        Label titulo = new Label("Detalhamento das Compras");
        titulo.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitulo = new Label(
                "Detalhamento dos itens das notas fiscais de compra."
        );

        VBox cabecalho = new VBox(
                5,
                titulo,
                subtitulo
        );

        cabecalho.setPadding(
                new Insets(0, 0, 15, 0)
        );

        /*
         * =========================================================
         * FILTROS
         * =========================================================
         */

        Label lblInicial =
                new Label("Data inicial:");

        Label lblFinal =
                new Label("Data final:");

        dtInicial =
                new DatePicker();

        dtFinal =
                new DatePicker();

        btPesquisar =
                new Button("Pesquisar");

        btLimpar =
                new Button("Limpar");

        HBox filtros =
                new HBox(
                        10,
                        lblInicial,
                        dtInicial,
                        lblFinal,
                        dtFinal,
                        btPesquisar,
                        btLimpar
                );

        filtros.setAlignment(Pos.CENTER_LEFT);

        /*
         * =========================================================
         * TABELA
         * =========================================================
         */

        dados =
                new TableView<>();

        dados.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        TableColumn<DetalhamentoCompraDTO, String> colCnpj =
                new TableColumn<>("CNPJ");

        colCnpj.setCellValueFactory(
                new PropertyValueFactory<>("cnpj")
        );

        TableColumn<DetalhamentoCompraDTO, String> colFornecedor =
                new TableColumn<>("Fornecedor");

        colFornecedor.setCellValueFactory(
                new PropertyValueFactory<>("fornecedor")
        );

        TableColumn<DetalhamentoCompraDTO, String> colNumero =
                new TableColumn<>("Nota");

        colNumero.setCellValueFactory(
                new PropertyValueFactory<>("numeroNota")
        );

        TableColumn<DetalhamentoCompraDTO, LocalDate> colData =
                new TableColumn<>("Data");

        colData.setCellValueFactory(
                new PropertyValueFactory<>("dataCompra")
        );

        TableColumn<DetalhamentoCompraDTO, String> colProduto =
                new TableColumn<>("Produto");

        colProduto.setCellValueFactory(
                new PropertyValueFactory<>("produto")
        );

        TableColumn<DetalhamentoCompraDTO, String> colCodigo =
                new TableColumn<>("Código");

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("codigoProduto")
        );

        TableColumn<DetalhamentoCompraDTO, Double> colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                new PropertyValueFactory<>("quantidade")
        );

        TableColumn<DetalhamentoCompraDTO, java.math.BigDecimal> colValorUnitario =
                new TableColumn<>("Valor Unitário");

        colValorUnitario.setCellValueFactory(
                new PropertyValueFactory<>("valorUnitario")
        );

        TableColumn<DetalhamentoCompraDTO, java.math.BigDecimal> colValorTotal =
                new TableColumn<>("Valor Total");

        colValorTotal.setCellValueFactory(
                new PropertyValueFactory<>("valorTotal")
        );

        dados.getColumns().addAll(
                colCnpj,
                colFornecedor,
                colNumero,
                colData,
                colProduto,
                colCodigo,
                colQuantidade,
                colValorUnitario,
                colValorTotal
        );

        /*
         * =========================================================
         * TOTAL
         * =========================================================
         */

        lbTotal =
                new Label("Total: R$ 0,00");

        lbTotal.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        /*
         * =========================================================
         * LAYOUT
         * =========================================================
         */

        VBox superior =
                new VBox(
                        10,
                        cabecalho,
                        filtros
                );

        setTop(superior);

        setCenter(dados);

        VBox inferior =
                new VBox(lbTotal);

        inferior.setPadding(
                new Insets(10, 0, 0, 0)
        );

        inferior.setAlignment(Pos.CENTER_RIGHT);

        setBottom(inferior);
    }

    /*
     * =============================================================
     * GETTERS USADOS PELO CONTROLLER
     * =============================================================
     */

    public Button getBtPesquisar() {
        return btPesquisar;
    }

    public Button getBtLimpar() {
        return btLimpar;
    }

    public DatePicker getDtInicial() {
        return dtInicial;
    }

    public DatePicker getDtFinal() {
        return dtFinal;
    }

    public TableView<DetalhamentoCompraDTO> getDados() {
        return dados;
    }

    public Label getLbTotal() {
        return lbTotal;
    }
}