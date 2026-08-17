package br.com.intelifiscal.fx.view.relatorio;

import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.util.Locale;

public class ResumoVendasView extends BorderPane {

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


    public ResumoVendasView() {

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

        setCenter(grid);
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
    // GETTERS
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
}