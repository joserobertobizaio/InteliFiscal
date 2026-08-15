package br.com.intelifiscal.fx.view.periodo;

import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import br.com.intelifiscal.fx.controller.periodo.ResumoPeriodoController;
import br.com.intelifiscal.fx.view.base.BaseView;
import br.com.intelifiscal.util.FormatadorNumero;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.List;

public class ResumoPeriodoView extends BaseView {

    //==================================================
    // LABELS DOS INDICADORES
    //==================================================

    private final Label lblPeriodo =
            new Label("Últimos 12 meses");

    private final Label lblNotasCompras =
            new Label("0");

    private final Label lblItensCompras =
            new Label("0");

    private final Label lblQuantidadeCompras =
            new Label("0");

    private final Label lblValorCompras =
            new Label("R$ 0,00");

    private final Label lblTicketCompras =
            new Label("R$ 0,00");


    private final Label lblNotasVendas =
            new Label("0");

    private final Label lblItensVendas =
            new Label("0");

    private final Label lblQuantidadeVendas =
            new Label("0");

    private final Label lblValorVendas =
            new Label("R$ 0,00");

    private final Label lblTicketVendas =
            new Label("R$ 0,00");

    //==================================================
    // GRÁFICO MENSAL
    //==================================================

    private BarChart<String, Number> graficoMensal;

    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoPeriodoView() {

        super(
                "Resumo dos últimos 12 meses",
                "Análise gerencial de compras e vendas no período."
        );

        initialize();
    }


    //==================================================
    // INITIALIZE
    //==================================================

    private void initialize() {

        //==================================================
        // PERÍODO
        //==================================================

        lblPeriodo.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
        );

        Label lblDescricaoPeriodo =
                new Label(
                        "Período considerado com base na última data de emissão registrada."
                );

        lblDescricaoPeriodo.setStyle(
                "-fx-font-size: 12px;"
        );


        VBox periodoBox =
                new VBox(4);

        periodoBox.setAlignment(
                Pos.CENTER_LEFT
        );

        periodoBox.getChildren().addAll(
                lblPeriodo,
                lblDescricaoPeriodo
        );


        //==================================================
        // COMPRAS
        //==================================================

        VBox cardCompras =
                criarCard(
                        "🛒 Compras",
                        lblNotasCompras,
                        lblItensCompras,
                        lblQuantidadeCompras,
                        lblValorCompras,
                        lblTicketCompras
                );


        //==================================================
        // VENDAS
        //==================================================

        VBox cardVendas =
                criarCard(
                        "💰 Vendas",
                        lblNotasVendas,
                        lblItensVendas,
                        lblQuantidadeVendas,
                        lblValorVendas,
                        lblTicketVendas
                );


        //==================================================
        // COMPARATIVO
        //==================================================

        HBox comparativo =
                new HBox(
                        25,
                        cardCompras,
                        cardVendas
                );

        comparativo.setAlignment(
                Pos.CENTER
        );

        //==================================================
        // GRÁFICO COMPRAS X VENDAS
        //==================================================

        CategoryAxis eixoX =
                new CategoryAxis();

        NumberAxis eixoY =
                new NumberAxis();

        eixoX.setLabel("Mês");
        eixoY.setLabel("Valor (R$)");

        graficoMensal =
                new BarChart<>(
                        eixoX,
                        eixoY
                );

        graficoMensal.setTitle(
                "Compras x Vendas por mês"
        );

        graficoMensal.setLegendVisible(true);

        graficoMensal.setAnimated(false);

        graficoMensal.setPrefHeight(270);

        graficoMensal.setMinHeight(250);


        //==================================================
        // CONTEÚDO
        //==================================================

        VBox conteudo =
                new VBox(20);

        conteudo.setPadding(
                new Insets(20)
        );

        conteudo.setAlignment(
                Pos.TOP_CENTER
        );

        conteudo.getChildren().addAll(
                periodoBox,
                comparativo,
                graficoMensal
        );

        //==================================================
        // LAYOUT
        //==================================================

        BorderPane painel =
                new BorderPane();

        painel.setCenter(
                conteudo
        );

        setContent(painel);


        //==================================================
        // CONTROLLER
        //==================================================

        new ResumoPeriodoController(this);
    }


    //==================================================
    // CARD
    //==================================================

    private VBox criarCard(
            String titulo,
            Label lblNotas,
            Label lblItens,
            Label lblQuantidade,
            Label lblValor,
            Label lblTicket
    ) {

        Label lblTitulo =
                new Label(titulo);

        lblTitulo.setStyle(
                "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
        );


        GridPane grid =
                new GridPane();

        grid.setHgap(25);
        grid.setVgap(7);


        adicionarLinha(
                grid,
                0,
                "Notas fiscais",
                lblNotas
        );

        adicionarLinha(
                grid,
                1,
                "Itens",
                lblItens
        );

        adicionarLinha(
                grid,
                2,
                "Quantidade",
                lblQuantidade
        );

        adicionarLinha(
                grid,
                3,
                "Valor total",
                lblValor
        );

        adicionarLinha(
                grid,
                4,
                "Ticket médio",
                lblTicket
        );


        VBox card =
                new VBox(10);

        card.setPadding(
                new Insets(14)
        );

        card.setPrefWidth(380);

        card.setMaxWidth(380);

        card.getChildren().addAll(
                lblTitulo,
                grid
        );


        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 10;"
                        + "-fx-border-color: #e0e0e0;"
                        + "-fx-border-radius: 10;"
                        + "-fx-border-width: 1;"
        );


        return card;
    }


    //==================================================
    // LINHA DO CARD
    //==================================================

    private void adicionarLinha(
            GridPane grid,
            int linha,
            String descricao,
            Label valor
    ) {

        Label lblDescricao =
                new Label(descricao);

        lblDescricao.setStyle(
                "-fx-font-size: 13px;"
        );


        valor.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
        );


        grid.add(
                lblDescricao,
                0,
                linha
        );

        grid.add(
                valor,
                1,
                linha
        );
    }


    //==================================================
    // ATUALIZA OS DADOS
    //==================================================

    public void atualizar(
            List<ResumoPeriodoDTO> lista
    ) {

        if (lista == null) {
            return;
        }


        for (ResumoPeriodoDTO dto : lista) {

            if (dto == null) {
                continue;
            }


            if ("COMPRA".equalsIgnoreCase(
                    dto.getOperacao()
            )) {

                preencherCompras(dto);

            } else if ("VENDA".equalsIgnoreCase(
                    dto.getOperacao()
            )) {

                preencherVendas(dto);
            }
        }
    }


    //==================================================
    // COMPRAS
    //==================================================

    private void preencherCompras(
            ResumoPeriodoDTO dto
    ) {

        lblNotasCompras.setText(
                String.valueOf(
                        dto.getNotas()
                )
        );

        lblItensCompras.setText(
                String.valueOf(
                        dto.getItens()
                )
        );

        lblQuantidadeCompras.setText(
                FormatadorNumero.formatarQuantidade(
                        dto.getQuantidade()
                )
        );

        lblValorCompras.setText(
                "R$ "
                        + FormatadorNumero.formatar(
                        dto.getValorTotal()
                )
        );

        lblTicketCompras.setText(
                "R$ "
                        + FormatadorNumero.formatar(
                        dto.getTicketMedio()
                )
        );
    }


    //==================================================
    // VENDAS
    //==================================================

    private void preencherVendas(
            ResumoPeriodoDTO dto
    ) {

        lblNotasVendas.setText(
                String.valueOf(
                        dto.getNotas()
                )
        );

        lblItensVendas.setText(
                String.valueOf(
                        dto.getItens()
                )
        );

        lblQuantidadeVendas.setText(
                FormatadorNumero.formatarQuantidade(
                        dto.getQuantidade()
                )
        );

        lblValorVendas.setText(
                "R$ "
                        + FormatadorNumero.formatar(
                        dto.getValorTotal()
                )
        );

        lblTicketVendas.setText(
                "R$ "
                        + FormatadorNumero.formatar(
                        dto.getTicketMedio()
                )
        );
    }

    //==================================================
// ATUALIZA O GRÁFICO MENSAL
//==================================================

    public void atualizarGrafico(
            List<ResumoMensalDTO> lista
    ) {

        if (lista == null) {
            return;
        }

        graficoMensal.getData().clear();


        XYChart.Series<String, Number> serieCompras =
                new XYChart.Series<>();

        serieCompras.setName("Compras");


        XYChart.Series<String, Number> serieVendas =
                new XYChart.Series<>();

        serieVendas.setName("Vendas");


        for (ResumoMensalDTO dto : lista) {

            if (dto == null) {
                continue;
            }


            String mes = dto.getMes();

            if (mes == null) {
                continue;
            }


            // Converte YYYY-MM para MM/YYYY
            String mesFormatado = mes;

            if (mes.length() == 7) {

                String ano =
                        mes.substring(0, 4);

                String numeroMes =
                        mes.substring(5, 7);

                mesFormatado =
                        numeroMes + "/" + ano;
            }


            BigDecimal valor =
                    dto.getValorTotal();

            if (valor == null) {
                valor = BigDecimal.ZERO;
            }


            XYChart.Data<String, Number> ponto =
                    new XYChart.Data<>(
                            mesFormatado,
                            valor.doubleValue()
                    );


            if ("COMPRA".equalsIgnoreCase(
                    dto.getOperacao()
            )) {

                serieCompras.getData().add(ponto);


            } else if ("VENDA".equalsIgnoreCase(
                    dto.getOperacao()
            )) {

                serieVendas.getData().add(ponto);
            }
        }


        graficoMensal.getData().addAll(
                serieCompras,
                serieVendas
        );
    }
}