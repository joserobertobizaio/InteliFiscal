package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableCell;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ResumoComprasView extends BorderPane {

    private final Label lblNotas = new Label("0");
    private final Label lblItens = new Label("0");
    private final Label lblQuantidade = new Label("0");
    private final Label lblValorTotal = new Label("R$ 0,00");
    private final Label lblTicketMedio = new Label("R$ 0,00");

    private final TableView<FornecedorCompraDTO> tabelaFornecedores =
            new TableView<>();

    private final Button btFechar =
            new Button("✖ Fechar");

    private final NumberFormat moeda =
            NumberFormat.getCurrencyInstance(
                    new Locale("pt", "BR")
            );

    private final NumberFormat numero =
            NumberFormat.getNumberInstance(
                    new Locale("pt", "BR")
            );

    public ResumoComprasView() {

        configurarView();

        criarCabecalho();

        criarResumo();

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
                new Label("Resumo de Compras");

        titulo.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #123b70;"
        );

        Label subtitulo =
                new Label(
                        "Análise gerencial das compras realizadas."
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
    // RESUMO
    //==================================================

    private void criarResumo() {

        GridPane grid =
                new GridPane();

        grid.setHgap(20);
        grid.setVgap(15);

        grid.setPadding(
                new Insets(25, 0, 20, 0)
        );

        grid.add(
                criarCard(
                        "🧾 Notas fiscais",
                        lblNotas
                ),
                0, 0
        );

        grid.add(
                criarCard(
                        "📦 Itens",
                        lblItens
                ),
                1, 0
        );

        grid.add(
                criarCard(
                        "🔢 Quantidade",
                        lblQuantidade
                ),
                2, 0
        );

        grid.add(
                criarCard(
                        "💰 Valor total",
                        lblValorTotal
                ),
                0, 1
        );

        grid.add(
                criarCard(
                        "🎯 Ticket médio",
                        lblTicketMedio
                ),
                1, 1
        );

        setCenter(
                new VBox(grid, criarAreaTabela())
        );
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
    // ÁREA DA TABELA
    //==================================================

    private VBox criarAreaTabela() {

        VBox area =
                new VBox(10);

        Label titulo =
                new Label("Compras por fornecedor");

        titulo.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;"
        );

        Label subtitulo =
                new Label(
                        "Fornecedores ordenados pelo maior valor comprado."
                );

        subtitulo.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #555555;"
        );

        criarColunas();

        tabelaFornecedores.setPrefHeight(300);

        tabelaFornecedores.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        area.getChildren().addAll(
                titulo,
                subtitulo,
                tabelaFornecedores
        );

        return area;
    }


    //==================================================
    // COLUNAS
    //==================================================

    private void criarColunas() {

        TableColumn<FornecedorCompraDTO, String>
                colFornecedor =
                new TableColumn<>("Fornecedor");

        colFornecedor.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().getFornecedor()
                        )
        );


        TableColumn<FornecedorCompraDTO, String>
                colNotas =
                new TableColumn<>("Notas");

        colNotas.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        data.getValue().getNotas()
                                )
                        )
        );


        TableColumn<FornecedorCompraDTO, String>
                colItens =
                new TableColumn<>("Itens");

        colItens.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        data.getValue().getItens()
                                )
                        )
        );


        TableColumn<FornecedorCompraDTO, String>
                colQuantidade =
                new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                formatarNumero(
                                        data.getValue().getQuantidade()
                                )
                        )
        );


        TableColumn<FornecedorCompraDTO, BigDecimal>
                colValor =
                new TableColumn<>("Valor total");

        colValor.setCellValueFactory(
                data ->
                        new SimpleObjectProperty<>(
                                data.getValue().getValorTotal()
                        )
        );

        colValor.setCellFactory(
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
                                    moeda.format(valor)
                            );
                        }
                    }
                }
        );


        tabelaFornecedores.getColumns().addAll(
                colFornecedor,
                colNotas,
                colItens,
                colQuantidade,
                colValor
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

        btFechar.setPrefWidth(100);

        rodape.getChildren().add(
                btFechar
        );

        setBottom(rodape);
    }


    //==================================================
    // ATUALIZAR RESUMO
    //==================================================

    public void atualizarResumo(
            ResumoComprasDTO dto
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
    // ATUALIZAR FORNECEDORES
    //==================================================

    public void atualizarFornecedores(
            List<FornecedorCompraDTO> lista
    ) {

        tabelaFornecedores
                .getItems()
                .setAll(lista);
    }


    //==================================================
    // GETTERS
    //==================================================

    public Button getBtFechar() {

        return btFechar;
    }


    public TableView<FornecedorCompraDTO>
    getTabelaFornecedores() {

        return tabelaFornecedores;
    }


    //==================================================
    // FORMATAÇÃO
    //==================================================

    private String formatarNumero(
            double valor
    ) {

        return numero.format(valor);
    }
}