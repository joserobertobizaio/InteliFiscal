package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.view.produto.CompararCompraVendaView;
import br.com.intelifiscal.service.produto.ProdutoService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class CompararCompraVendaController {

    private final CompararCompraVendaView view;

    private final ProdutoService service =
            new ProdutoService();

    private final DecimalFormat formatoQuantidade =
            new DecimalFormat(
                    "#,##0.##",
                    DecimalFormatSymbols.getInstance(
                            new Locale("pt", "BR")
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
    // CRIA RESUMO
    //==================================================

    private Resumo criarResumo(
            List<ProdutoHistoricoDTO> historico,
            String tipoEsperado) {

        double quantidade = 0.0;

        double somaPrecos = 0.0;

        int quantidadePrecos = 0;

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
                quantidadePrecos > 0
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
                converterPreco(
                        precoCompra,
                        produtoCompra.getUnidade(),
                        produtoVenda.getUnidade()
                );

        double diferenca =
                precoVenda
                        - precoCompraConvertido;

        double margem =
                0.0;

        if (precoVenda != 0) {

            margem =
                    (diferenca / precoVenda)
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
                        + "Margem sobre a venda: "
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

        view.setVisible(false);
    }

    //==================================================
    // CLASSE RESUMO
    //==================================================

    private static class Resumo {

        private final double quantidade;

        private final double precoMedio;

        private final boolean temMovimento;

        private Resumo(
                double quantidade,
                double precoMedio,
                boolean temMovimento) {

            this.quantidade = quantidade;

            this.precoMedio = precoMedio;

            this.temMovimento = temMovimento;
        }
    }
}