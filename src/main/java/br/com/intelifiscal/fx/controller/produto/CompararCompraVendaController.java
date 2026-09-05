package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.view.produto.CompararCompraVendaView;
import br.com.intelifiscal.service.produto.ProdutoService;
import br.com.intelifiscal.service.produto.ConversaoUnidadeService;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class CompararCompraVendaController {

    private final CompararCompraVendaView view;

    private final ProdutoService service =
            new ProdutoService();

    private final ConversaoUnidadeService conversaoUnidadeService =
            new ConversaoUnidadeService();

    //  aqui faremos uma correção futura
    private final DecimalFormat formatoQuantidade =
            new DecimalFormat(
                    "#,##0.####",
                    DecimalFormatSymbols.getInstance(
                            Locale.forLanguageTag("pt-BR")
                    )
            );

    private final DecimalFormat formatoValor =
            new DecimalFormat(
                    "#,##0.0000",
                    DecimalFormatSymbols.getInstance(
                            new Locale("pt", "BR")
                    )
            );

    public CompararCompraVendaController(
            CompararCompraVendaView view) {

        this.view = view;

        configurarEventos();
    }

    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtPesquisar()
                .setOnAction(
                        e -> compararProdutos()
                );

        view.getBtVincular()
                .setOnAction(
                        e -> vincularProdutos()
                );

        view.getBtFechar()
                .setOnAction(
                        e -> fechar()
                );
    }

    //==================================================
    // COMPARA PRODUTOS
    //==================================================

    private void compararProdutos() {

        String codigoCompra =
                view.getTxtCodigoCompra()
                        .getText()
                        .trim();

        String codigoVenda =
                view.getTxtCodigoVenda()
                        .getText()
                        .trim();

        limparResultado();

        if (codigoCompra.isBlank()
                || codigoVenda.isBlank()) {

            view.getLblResultado().setText(
                    "Informe os dois códigos para realizar a comparação."
            );

            return;
        }

        //==================================================
        // BUSCA PRODUTO DE COMPRA
        //==================================================

        ProdutoDTO produtoCompra =
                service.buscarPorCodigoProduto(
                        codigoCompra
                );

        //==================================================
        // BUSCA PRODUTO DE VENDA
        //==================================================

        ProdutoDTO produtoVenda =
                service.buscarPorCodigoProduto(
                        codigoVenda
                );

        //==================================================
        // MOSTRA DADOS DA COMPRA
        //==================================================

        if (produtoCompra == null) {

            limparCompra();

        } else {

            preencherCompra(
                    produtoCompra
            );
        }

        //==================================================
        // MOSTRA DADOS DA VENDA
        //==================================================

        if (produtoVenda == null) {

            limparVenda();

        } else {

            preencherVenda(
                    produtoVenda
            );
        }

        //==================================================
        // HISTÓRICO
        //==================================================

        List<ProdutoHistoricoDTO> historicoCompra =
                produtoCompra == null
                        ? List.of()
                        : service.listarHistoricoPorCodigoProduto(
                        codigoCompra
                );

        List<ProdutoHistoricoDTO> historicoVenda =
                produtoVenda == null
                        ? List.of()
                        : service.listarHistoricoPorCodigoProduto(
                        codigoVenda
                );

        //==================================================
        // RESUMOS
        //==================================================

        Resumo resumoCompra =
                criarResumo(
                        historicoCompra,
                        "Compra"
                );

        Resumo resumoVenda =
                criarResumo(
                        historicoVenda,
                        "Venda"
                );

        //==================================================
        // QUANTIDADES
        //==================================================

        if (produtoCompra != null) {

            view.getLblCompraQuantidade()
                    .setText(
                            formatoQuantidade.format(
                                    resumoCompra.quantidade
                            )
                                    + " "
                                    + produtoCompra.getUnidade()
                    );
        }

        if (produtoVenda != null) {

            view.getLblVendaQuantidade()
                    .setText(
                            formatoQuantidade.format(
                                    resumoVenda.quantidade
                            )
                                    + " "
                                    + produtoVenda.getUnidade()
                    );
        }

        //==================================================
        // PREÇOS
        //==================================================

        if (resumoCompra.temMovimento) {

            view.getLblCompraPreco()
                    .setText(
                            "R$ "
                                    + formatoValor.format(
                                    resumoCompra.precoMedio
                            )
                    );
        }

        if (resumoVenda.temMovimento) {

            view.getLblVendaPreco()
                    .setText(
                            "R$ "
                                    + formatoValor.format(
                                    resumoVenda.precoMedio
                            )
                    );
        }

        // ==================================================
        // DATAS
        // ==================================================

        java.time.format.DateTimeFormatter formatoData =
                java.time.format.DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        if (resumoCompra.ultimaData != null) {

            view.getLblCompraData()
                    .setText(
                            resumoCompra.ultimaData
                                    .format(formatoData)
                    );
        }

        if (resumoVenda.ultimaData != null) {

            view.getLblVendaData()
                    .setText(
                            resumoVenda.ultimaData
                                    .format(formatoData)
                    );
        }

        //==================================================
        // RESULTADO
        //==================================================

        gerarResultado(
                produtoCompra,
                produtoVenda,
                resumoCompra,
                resumoVenda
        );
    }

    //==================================================
    // PREENCHER COMPRA
    //==================================================

    private void preencherCompra(
            ProdutoDTO produto) {

        view.getLblCompraCodigo()
                .setText(
                        produto.getCodigoProduto()
                );

        view.getLblCompraDescricao()
                .setText(
                        produto.getDescricao()
                );

        view.getLblCompraUnidade()
                .setText(
                        produto.getUnidade()
                );
    }

    //==================================================
    // PREENCHER VENDA
    //==================================================

    private void preencherVenda(
            ProdutoDTO produto) {

        view.getLblVendaCodigo()
                .setText(
                        produto.getCodigoProduto()
                );

        view.getLblVendaDescricao()
                .setText(
                        produto.getDescricao()
                );

        view.getLblVendaUnidade()
                .setText(
                        produto.getUnidade()
                );
    }

    //==================================================
    // CRIA RESUMO DO ÚLTIMO MOVIMENTO
    //==================================================

    private Resumo criarResumo(
            List<ProdutoHistoricoDTO> historico,
            String tipoEsperado) {

        double quantidade = 0.0;

        double somaPrecos = 0.0;

        int quantidadePrecos = 0;

        LocalDateTime ultimaData = null;

        for (ProdutoHistoricoDTO item : historico) {

            if (item == null) {
                continue;
            }

            if (!tipoEsperado.equalsIgnoreCase(
                    item.getTipo()
            )) {
                continue;
            }

            if (item.getQuantidade() != null) {

                quantidade +=
                        item.getQuantidade();
            }

            if (item.getValorUnitario() != null) {

                somaPrecos +=
                        item.getValorUnitario();

                quantidadePrecos++;
            }

            // ======================================================
            // ÚLTIMA DATA DO MOVIMENTO
            // ======================================================

            if (item.getDataEmissao() != null) {

                if (ultimaData == null
                        || item.getDataEmissao()
                        .isAfter(ultimaData)) {

                    ultimaData =
                            item.getDataEmissao();
                }
            }
        }

        double precoMedio = 0.0;

        if (quantidadePrecos > 0) {

            precoMedio =
                    somaPrecos
                            / quantidadePrecos;
        }

        return new Resumo(
                quantidade,
                precoMedio,
                quantidadePrecos > 0,
                ultimaData
        );
    }

    //==================================================
    // RESULTADO DA COMPARAÇÃO
    //==================================================

    private void gerarResultado(
            ProdutoDTO produtoCompra,
            ProdutoDTO produtoVenda,
            Resumo resumoCompra,
            Resumo resumoVenda) {

        if (produtoCompra == null
                && produtoVenda == null) {

            view.getLblResultado().setText(
                    "Nenhum dos dois produtos foi encontrado."
            );

            return;
        }

        if (produtoCompra == null) {

            view.getLblResultado().setText(
                    "Produto de compra não encontrado."
            );

            return;
        }

        if (produtoVenda == null) {

            view.getLblResultado().setText(
                    "Produto de venda não encontrado."
            );

            return;
        }

        if (!resumoCompra.temMovimento) {

            view.getLblResultado().setText(
                    "O produto de compra foi encontrado, " +
                            "mas não possui histórico de compras."
            );

            return;
        }

        if (!resumoVenda.temMovimento) {

            view.getLblResultado().setText(
                    "O produto de venda foi encontrado, " +
                            "mas não possui histórico de vendas."
            );

            return;
        }

        double precoCompra =
                resumoCompra.precoMedio;

        double precoVenda =
                resumoVenda.precoMedio;

        //==================================================
        // CONVERSÃO MIL -> PC
        //==================================================

        double precoCompraConvertido =
                conversaoUnidadeService.converterValorUnitario(
                        precoCompra,
                        produtoCompra.getUnidade(),
                        produtoVenda.getUnidade()
                );

        double diferenca =
                precoVenda
                        - precoCompraConvertido;

        double margem =
                0.0;

        if (precoCompraConvertido != 0) {

            margem =
                    (diferenca / precoCompraConvertido)
                            * 100.0;
        }

        String texto =
                "Compra: "
                        + produtoCompra.getCodigoProduto()
                        + " ("
                        + produtoCompra.getUnidade()
                        + ")"
                        + " → Venda: "
                        + produtoVenda.getCodigoProduto()
                        + " ("
                        + produtoVenda.getUnidade()
                        + ")"
                        + "\n"
                        + "Preço de compra equivalente: R$ "
                        + formatoValor.format(
                        precoCompraConvertido
                )
                        + " por "
                        + produtoVenda.getUnidade()
                        + "\n"
                        + "Preço médio de venda: R$ "
                        + formatoValor.format(
                        precoVenda
                )
                        + " por "
                        + produtoVenda.getUnidade()
                        + "\n"
                        + "Diferença: R$ "
                        + formatoValor.format(
                        diferenca
                )
                        + "\n"
                        + "Margem sobre a custo: "
                        + formatoValor.format(
                        margem
                )
                        + "%";

        view.getLblResultado()
                .setText(texto);
    }

    //==================================================
    // CONVERSÃO DE UNIDADE
    //==================================================

    private double converterPreco(
            double preco,
            String unidadeCompra,
            String unidadeVenda) {

        if (unidadeCompra == null
                || unidadeVenda == null) {

            return preco;
        }

        String compra =
                unidadeCompra
                        .trim()
                        .toUpperCase();

        String venda =
                unidadeVenda
                        .trim()
                        .toUpperCase();

        // MIL -> PC
        if ("MIL".equals(compra)
                && "PC".equals(venda)) {

            return preco / 1000.0;
        }

        // Caso as unidades sejam iguais
        if (compra.equals(venda)) {

            return preco;
        }

        // Por enquanto, se não conhecemos
        // a conversão, mantém o preço original.
        return preco;
    }

    //==================================================
    // LIMPA COMPRA
    //==================================================

    private void limparCompra() {

        view.getLblCompraCodigo()
                .setText("-");

        view.getLblCompraDescricao()
                .setText("Produto não encontrado");

        view.getLblCompraUnidade()
                .setText("-");

        view.getLblCompraQuantidade()
                .setText("-");

        view.getLblCompraPreco()
                .setText("-");
    }

    //==================================================
    // LIMPA VENDA
    //==================================================

    private void limparVenda() {

        view.getLblVendaCodigo()
                .setText("-");

        view.getLblVendaDescricao()
                .setText("Produto não encontrado");

        view.getLblVendaUnidade()
                .setText("-");

        view.getLblVendaQuantidade()
                .setText("-");

        view.getLblVendaPreco()
                .setText("-");
    }

    //==================================================
    // LIMPA RESULTADO
    //==================================================

    private void limparResultado() {

        view.getLblCompraQuantidade()
                .setText("-");

        view.getLblCompraPreco()
                .setText("-");

        view.getLblVendaQuantidade()
                .setText("-");

        view.getLblVendaPreco()
                .setText("-");

        view.getLblResultado()
                .setText(
                        "Informe os códigos para realizar a comparação."
                );
    }

    //==================================================
    // FECHAR
    //==================================================

    private void fechar() {

        NavigationManager.show(
                br.com.intelifiscal.fx.navigation.ScreenType.DASHBOARD
        );
    }

    //==================================================
    // VINCULAR PRODUTOS
    //==================================================

    private void vincularProdutos() {

        String codigoCompra =
                view.getTxtCodigoCompra()
                        .getText()
                        .trim();

        String codigoVenda =
                view.getTxtCodigoVenda()
                        .getText()
                        .trim();


        //==================================================
        // VALIDA CÓDIGOS
        //==================================================

        if (codigoCompra.isBlank()
                || codigoVenda.isBlank()) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle(
                    "Vincular produtos"
            );

            alerta.setHeaderText(
                    "Códigos não informados"
            );

            alerta.setContentText(
                    "Informe o código do produto de compra " +
                            "e o código do produto de venda."
            );

            alerta.showAndWait();

            return;
        }


        //==================================================
        // BUSCA PRODUTO DE COMPRA
        //==================================================

        ProdutoDTO produtoCompra =
                service.buscarPorCodigoProduto(
                        codigoCompra
                );


        if (produtoCompra == null) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle(
                    "Vincular produtos"
            );

            alerta.setHeaderText(
                    "Produto de compra não encontrado"
            );

            alerta.setContentText(
                    "O código "
                            + codigoCompra
                            + " não está cadastrado."
            );

            alerta.showAndWait();

            return;
        }


        //==================================================
        // BUSCA PRODUTO DE VENDA
        //==================================================

        ProdutoDTO produtoVenda =
                service.buscarPorCodigoProduto(
                        codigoVenda
                );


        if (produtoVenda == null) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle(
                    "Vincular produtos"
            );

            alerta.setHeaderText(
                    "Produto de venda não encontrado"
            );

            alerta.setContentText(
                    "O código "
                            + codigoVenda
                            + " não está cadastrado."
            );

            alerta.showAndWait();

            return;
        }


        //==================================================
        // VERIFICA SE JÁ EXISTE
        //==================================================

        if (service.existeVinculo(
                codigoCompra,
                codigoVenda
        )) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.INFORMATION
                    );

            alerta.setTitle(
                    "Vincular produtos"
            );

            alerta.setHeaderText(
                    "Produtos já estão vinculados"
            );

            alerta.setContentText(
                    "O produto de compra "
                            + codigoCompra
                            + " já está vinculado ao produto de venda "
                            + codigoVenda
                            + "."
            );

            alerta.showAndWait();

            return;
        }


        //==================================================
        // GRAVA VÍNCULO
        //==================================================

        try {

            service.vincularProdutos(
                    codigoCompra,
                    codigoVenda
            );


            //==================================================
            // CONFIRMAÇÃO
            //==================================================

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.INFORMATION
                    );

            alerta.setTitle(
                    "Vincular produtos"
            );

            alerta.setHeaderText(
                    "Produtos vinculados com sucesso!"
            );

            alerta.setContentText(
                    "Compra: "
                            + produtoCompra.getCodigoProduto()
                            + " - "
                            + produtoCompra.getDescricao()
                            + "\n\n"
                            + "Venda: "
                            + produtoVenda.getCodigoProduto()
                            + " - "
                            + produtoVenda.getDescricao()
            );

            alerta.showAndWait();


        } catch (Exception e) {

            e.printStackTrace();

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR
                    );

            alerta.setTitle(
                    "Erro"
            );

            alerta.setHeaderText(
                    "Não foi possível vincular os produtos."
            );

            alerta.setContentText(
                    e.getMessage()
            );

            alerta.showAndWait();
        }
    }

    //==================================================
    // CLASSE RESUMO
    //==================================================
    private static class Resumo {

        private final double quantidade;

        private final double precoMedio;

        private final boolean temMovimento;

        private final LocalDateTime ultimaData;

        private Resumo(
                double quantidade,
                double precoMedio,
                boolean temMovimento,
                LocalDateTime ultimaData) {

            this.quantidade = quantidade;

            this.precoMedio = precoMedio;

            this.temMovimento = temMovimento;

            this.ultimaData = ultimaData;
        }
    }
}