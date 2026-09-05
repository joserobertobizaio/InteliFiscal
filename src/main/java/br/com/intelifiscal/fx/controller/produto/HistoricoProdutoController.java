package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.repository.nfeitem.NFeItemRepository;
import br.com.intelifiscal.repository.NFeRepository;
import br.com.intelifiscal.service.nfe.NFePdfService;
import javafx.stage.FileChooser;

import java.io.File;
import br.com.intelifiscal.fx.view.produto.HistoricoProdutoView;
import br.com.intelifiscal.service.produto.ProdutoVinculoService;
import br.com.intelifiscal.repository.produto.ProdutoVinculoRepository;
import br.com.intelifiscal.service.produto.ConversaoUnidadeService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoricoProdutoController {

    private final HistoricoProdutoView view;

    private final NFeItemRepository repository =
            new NFeItemRepository();

    private final NFeRepository nfeRepository =
            new NFeRepository();

    private final NFePdfService nfePdfService =
            new NFePdfService();

    private final ProdutoVinculoRepository vinculoRepository =
            new ProdutoVinculoRepository();

    private final ProdutoVinculoService vinculoService =
            new ProdutoVinculoService();

    private final ConversaoUnidadeService conversaoUnidadeService =
            new ConversaoUnidadeService();

    // ============================================================
    // CONSTRUTOR
    // ============================================================
    public HistoricoProdutoController(
            HistoricoProdutoView view) {

        this.view = view;

        configurarEventos();

        configurarPeriodoPersonalizado();

        // Carrega o histórico automaticamente ao abrir a tela
        pesquisar(false);
    }

    // ============================================================
    // EVENTOS
    // ============================================================

    private void configurarEventos() {

        // --------------------------------------------------------
        // PESQUISAR
        // --------------------------------------------------------

        view.getBtPesquisar()
                .setOnAction(
                        event -> pesquisar(true)
                );


        // --------------------------------------------------------
        // LIMPAR
        // --------------------------------------------------------

        view.getBtLimpar()
                .setOnAction(
                        event -> limpar()
                );

        // --------------------------------------------------------
        // FILTRO DE PRODUTOS VENDIDOS
        // --------------------------------------------------------

        view.getChkProdutosVendidos()
                .setOnAction(
                        event -> aplicarFiltroTipo()
                );


        // --------------------------------------------------------
        // FILTRO DE PRODUTOS COMPRADOS
        // --------------------------------------------------------

        view.getChkProdutosComprados()
                .setOnAction(
                        event -> aplicarFiltroTipo()
                );


        // --------------------------------------------------------
        // FECHAR
        // --------------------------------------------------------

        view.getBtFechar()
                .setOnAction(
                        event -> fechar()
                );


        // --------------------------------------------------------
        // ENTER NA PESQUISA
        // --------------------------------------------------------

        view.getTxtPesquisa()
                .setOnAction(
                        event -> pesquisar(true)
                );

        // --------------------------------------------------------
        // COMPARAR SELECIONADOS
        // --------------------------------------------------------

        view.getBtComparar()
                .setOnAction(
                        event -> compararSelecionados()
                );

        // --------------------------------------------------------
        // VINCULAR
        // --------------------------------------------------------

        view.getBtVincular()
                .setOnAction(
                        event -> vincular()
                );

        // --------------------------------------------------------
        // DESVINCULAR
        // --------------------------------------------------------

        view.getBtDesvincular()
                .setOnAction(
                        event -> desvincular()
                );

        // --------------------------------------------------------
        // GERAR NF-e INTEIRA EM PDF
        // --------------------------------------------------------

        view.getMenuGerarPdfNfe()
                .setOnAction(
                        event -> gerarPdfNfe()
                );

        // --------------------------------------------------------
        // ALTERAÇÃO DO PERÍODO
        // --------------------------------------------------------

        view.getCbPeriodo()
                .setOnAction(
                        event -> alterarPeriodo()
                );

        // --------------------------------------------------------
        // DATA INICIAL
        // --------------------------------------------------------

        view.getDtInicio()
                .valueProperty()
                .addListener(
                        (obs, antiga, nova) -> {

                            if ("Período personalizado".equals(
                                    view.getCbPeriodo().getValue())) {

                                if (view.getDtInicio().getValue() != null
                                        && view.getDtFim().getValue() != null) {

                                    pesquisar(false);
                                }
                            }
                        }
                );


        // --------------------------------------------------------
        // DATA FINAL
        // --------------------------------------------------------

        view.getDtFim()
                .valueProperty()
                .addListener(
                        (obs, antiga, nova) -> {

                            if ("Período personalizado".equals(
                                    view.getCbPeriodo().getValue())) {

                                if (view.getDtInicio().getValue() != null
                                        && view.getDtFim().getValue() != null) {

                                    pesquisar(false);
                                }
                            }
                        }
                );

        // --------------------------------------------------------
        // BOTÃO DIREITO DO MOUSE NA TABELA
        // --------------------------------------------------------

        view.getTabela().setOnMousePressed(event -> {

            if (event.isSecondaryButtonDown()) {

                var posicao =
                        view.getTabela()
                                .getSelectionModel()
                                .getSelectedIndex();

                if (posicao >= 0) {

                    view.getTabela()
                            .getSelectionModel()
                            .select(posicao);
                }
            }
        });
    }

        // ============================================================
        // CONFIGURA PERÍODO PERSONALIZADO
        // ============================================================

    private void configurarPeriodoPersonalizado() {

        atualizarPeriodoPersonalizado();
    }

    // ============================================================
    // ALTERAÇÃO DO PERÍODO
    // ============================================================

    private void alterarPeriodo() {

        // Primeiro mostra ou esconde os calendários
        atualizarPeriodoPersonalizado();


        // --------------------------------------------------------
        // PERÍODO PERSONALIZADO
        // --------------------------------------------------------
        //
        // Ao escolher "Período personalizado", ainda não temos
        // necessariamente as duas datas preenchidas.
        //
        // Portanto aguardamos o usuário escolher as datas.
        // --------------------------------------------------------

        if ("Período personalizado".equals(
                view.getCbPeriodo().getValue())) {

            if (view.getDtInicio().getValue() == null
                    || view.getDtFim().getValue() == null) {

                return;
            }
        }


        // --------------------------------------------------------
        // ATUALIZA A TABELA
        // --------------------------------------------------------
        //
        // false = não exige que o campo de pesquisa esteja
        // preenchido.
        //
        // Se estiver vazio, serão carregados TODOS os produtos.
        // Se houver um texto, serão carregados somente os
        // produtos correspondentes à pesquisa.
        // --------------------------------------------------------

        pesquisar(false);
    }


        // ============================================================
        // MOSTRA / OCULTA CALENDÁRIOS
        // ============================================================

    private void atualizarPeriodoPersonalizado() {

        boolean personalizado =
                "Período personalizado".equals(
                        view.getCbPeriodo().getValue()
                );

        view.getPeriodoPersonalizado()
                .setVisible(personalizado);

        view.getPeriodoPersonalizado()
                .setManaged(personalizado);
    }

        // ============================================================
        // PESQUISAR
        // ============================================================

    private void pesquisar(boolean validarPesquisa) {

        // --------------------------------------------------------
        // PEGA O TEXTO DA CAIXA QUE REALMENTE ESTÁ NA TELA
        // --------------------------------------------------------

        String pesquisa =
                view.getTxtPesquisa()
                        .getText()
                        .trim();

        if (validarPesquisa && pesquisa.isBlank()) {

            mostrarAviso(
                    "Digite um produto para pesquisar."
            );

            view.getTxtPesquisa()
                    .requestFocus();

            return;
        }


        LocalDate inicio = null;
        LocalDate fim = null;


        // ========================================================
        // PERÍODO
        // ========================================================

        String periodo =
                view.getCbPeriodo()
                        .getValue();


        /*
         * Por enquanto vamos tratar:
         *
         * Últimos 12 meses
         * Desde o início
         *
         * Os demais períodos serão ajustados na próxima etapa.
         */


        if ("Últimos 12 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(12);

        } else if ("Últimos 6 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(6);

        } else if ("Últimos 3 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(3);

        } else if ("Últimos 30 dias".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusDays(30);

        } else if ("Últimos 24 meses".equals(periodo)) {

            fim = LocalDate.now();

            inicio = fim.minusMonths(24);

        } else if ("Período personalizado".equals(periodo)) {

            inicio =
                    view.getDtInicio()
                            .getValue();

            fim =
                    view.getDtFim()
                            .getValue();

            if (inicio == null || fim == null) {

                mostrarAviso(
                        "Informe a data inicial e a data final."
                );

                return;
            }

            if (inicio.isAfter(fim)) {

                mostrarAviso(
                        "A data inicial não pode ser maior que a data final."
                );

                return;
            }

        } else {

            // Desde o início
            inicio = null;
            fim = null;
        }


        // ========================================================
        // CONSULTA
        // ========================================================

        try {

            List<ProdutoHistoricoDTO> historico;


            if (inicio == null || fim == null) {

                historico =
                        repository.listarHistoricoPorPesquisa(
                                pesquisa
                        );

            } else {

                historico =
                        repository.listarHistoricoPorPesquisa(
                                pesquisa,
                                inicio,
                                fim
                        );
            }

            // ====================================================
            // FILTRO POR TIPO
            // ====================================================

            boolean mostrarVendas =
                    view.getChkProdutosVendidos()
                            .isSelected();

            boolean mostrarCompras =
                    view.getChkProdutosComprados()
                            .isSelected();


            historico =
                    historico.stream()
                            .filter(item -> {

                                if (item == null
                                        || item.getTipo() == null) {

                                    return false;
                                }

                                if ("Venda".equalsIgnoreCase(
                                        item.getTipo())) {

                                    return mostrarVendas;
                                }

                                if ("Compra".equalsIgnoreCase(
                                        item.getTipo())) {

                                    return mostrarCompras;
                                }

                                return false;
                            })
                            .toList();


        // ====================================================
        // PREENCHE TABELA
        // ====================================================

            view.getDados()
                    .setAll(historico);

            // ====================================================
            // CONTADOR
            // ====================================================

            view.getLblContador()
                    .setText(
                            historico.size()
                                    + (
                                    historico.size() == 1
                                            ? " registro encontrado"
                                            : " registros encontrados"
                            )
                    );


            // ====================================================
            // DESCRIÇÃO
            // ====================================================

            preencherDescricao(
                    historico
            );


            // ====================================================
            // NENHUM RESULTADO
            // ====================================================

            if (historico.isEmpty()) {

                mostrarAviso(
                        "Nenhum histórico encontrado para o produto informado."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            mostrarErro(
                    "Erro ao consultar o histórico do produto.",
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // FILTRO POR TIPO
    // ============================================================

    private void aplicarFiltroTipo() {

        // Atualiza a tabela utilizando os filtros atuais.
        //
        // Se a pesquisa estiver vazia:
        //     carrega todos os produtos.
        //
        // Se houver uma pesquisa:
        //     carrega somente os produtos pesquisados.

        pesquisar(false);
    }


    // ============================================================
    // PREENCHE DESCRIÇÃO
    // ============================================================

    private void preencherDescricao(
            List<ProdutoHistoricoDTO> historico) {

        if (historico == null
                || historico.isEmpty()) {

            view.getTxtDescricao()
                    .clear();

            return;
        }


        for (ProdutoHistoricoDTO item : historico) {

            if (item.getDescricao() != null
                    && !item.getDescricao().isBlank()) {

                view.getTxtDescricao()
                        .setText(
                                item.getDescricao()
                        );

                return;
            }
        }


        view.getTxtDescricao()
                .clear();
    }


    // ============================================================
    // LIMPAR
    // ============================================================

    private void limpar() {

        // Campo visível da pesquisa
        view.getTxtPesquisa()
                .clear();


        // Campo antigo
        view.getTxtCodigoProduto()
                .clear();


        // Descrição
        view.getTxtDescricao()
                .clear();


        // Datas antigas
        view.getDtInicio()
                .setValue(null);

        view.getDtFim()
                .setValue(null);


        // Tabela
        view.getDados()
                .clear();

        view.getLblContador()
                .setText("0 registros encontrados");

        // Limpa seleções
        view.limparSelecoes();


        // Volta para o período padrão
        view.getCbPeriodo()
                .setValue("Últimos 12 meses");

        atualizarPeriodoPersonalizado();

        // Foco
        view.getTxtPesquisa()
                .requestFocus();
    }


    // ============================================================
    // FECHAR
    // ============================================================

    private void fechar() {

        NavigationManager.show(
                br.com.intelifiscal.fx.navigation.ScreenType.DASHBOARD
        );
    }

    // ============================================================
    // COMPARAR PRODUTOS SELECIONADOS
    // ============================================================

    private void compararSelecionados() {

        // --------------------------------------------------------
        // PEGA OS PRODUTOS SELECIONADOS
        // --------------------------------------------------------

        List<ProdutoHistoricoDTO> selecionados =
                view.getProdutosSelecionados();


        // --------------------------------------------------------
        // PRECISA SELECIONAR EXATAMENTE DOIS
        // --------------------------------------------------------

        if (selecionados.size() != 2) {

            mostrarAviso(
                    "Para comparar, selecione exatamente dois produtos."
            );

            return;
        }


        // --------------------------------------------------------
        // PRODUTOS
        // --------------------------------------------------------

        ProdutoHistoricoDTO produto1 =
                selecionados.get(0);

        ProdutoHistoricoDTO produto2 =
                selecionados.get(1);


        // --------------------------------------------------------
        // IDENTIFICA COMPRA E VENDA
        // --------------------------------------------------------

        ProdutoHistoricoDTO produtoCompra = null;

        ProdutoHistoricoDTO produtoVenda = null;


        if ("Compra".equalsIgnoreCase(
                produto1.getTipo())) {

            produtoCompra = produto1;

        } else if ("Venda".equalsIgnoreCase(
                produto1.getTipo())) {

            produtoVenda = produto1;
        }


        if ("Compra".equalsIgnoreCase(
                produto2.getTipo())) {

            produtoCompra = produto2;

        } else if ("Venda".equalsIgnoreCase(
                produto2.getTipo())) {

            produtoVenda = produto2;
        }


        // --------------------------------------------------------
        // PRECISA SER UMA COMPRA E UMA VENDA
        // --------------------------------------------------------

        if (produtoCompra == null
                || produtoVenda == null) {

            mostrarAviso(
                    "Para comparar, selecione:\n\n"
                            + "• 1 produto de Compra\n"
                            + "• 1 produto de Venda."
            );

            return;
        }


        // --------------------------------------------------------
        // CÓDIGOS
        // --------------------------------------------------------

        String codigoCompra =
                produtoCompra.getCodigoProduto();

        String codigoVenda =
                produtoVenda.getCodigoProduto();


        // --------------------------------------------------------
        // DESCRIÇÕES
        // --------------------------------------------------------

        String descricaoCompra =
                produtoCompra.getDescricao();

        String descricaoVenda =
                produtoVenda.getDescricao();


        // --------------------------------------------------------
        // UNIDADES
        // --------------------------------------------------------

        String unidadeCompra =
                produtoCompra.getUnidade();

        String unidadeVenda =
                produtoVenda.getUnidade();


        // ========================================================
        // BUSCA O HISTÓRICO COMPLETO DOS DOIS PRODUTOS
        // ========================================================
        //
        // IMPORTANTE:
        //
        // Não vamos usar somente o valor da linha selecionada.
        //
        // A tela "Comparar Compra e Venda" utiliza o preço médio
        // do histórico do produto.
        //
        // Para que as duas telas apresentem exatamente a mesma
        // margem, precisamos utilizar a mesma regra.
        // ========================================================

        List<ProdutoHistoricoDTO> historicoCompra =
                repository.listarHistoricoPorPesquisa(
                        codigoCompra
                );

        List<ProdutoHistoricoDTO> historicoVenda =
                repository.listarHistoricoPorPesquisa(
                        codigoVenda
                );


        // --------------------------------------------------------
        // CRIA OS RESUMOS
        // --------------------------------------------------------

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


        // --------------------------------------------------------
        // VERIFICA HISTÓRICO
        // --------------------------------------------------------

        if (!resumoCompra.temMovimento) {

            mostrarAviso(
                    "O produto de compra não possui histórico de compras."
            );

            return;
        }


        if (!resumoVenda.temMovimento) {

            mostrarAviso(
                    "O produto de venda não possui histórico de vendas."
            );

            return;
        }


        // ========================================================
        // PREÇOS MÉDIOS
        // ========================================================

        double valorCompraOriginal =
                resumoCompra.precoMedio;

        double valorVenda =
                resumoVenda.precoMedio;


        // ========================================================
        // CONVERSÃO PARA A MESMA UNIDADE
        // ========================================================
        //
        // A unidade da VENDA é utilizada como referência.
        //
        // Exemplo:
        //
        // Compra = R$ 62,7388 / MIL
        // Venda  = R$ 0,1136 / PC
        //
        // Compra convertida:
        //
        // R$ 0,0627388 / PC
        // ========================================================

        double valorCompra =
                conversaoUnidadeService.converterValorUnitario(
                        valorCompraOriginal,
                        unidadeCompra,
                        unidadeVenda
                );


        // ========================================================
        // DIFERENÇA
        // ========================================================

        double diferenca =
                valorVenda - valorCompra;


        // ========================================================
        // MARGEM SOBRE O CUSTO
        // ========================================================

        double margem = 0.0;

        if (valorCompra != 0.0) {

            margem =
                    (diferenca / valorCompra)
                            * 100.0;
        }


        // ========================================================
        // QUANTIDADES
        // ========================================================
        //
        // Mantemos as quantidades das linhas que o usuário
        // efetivamente selecionou.
        //
        // Não alteramos esse comportamento da janela.
        // ========================================================

        double quantidadeCompra =
                produtoCompra.getQuantidade() == null
                        ? 0.0
                        : produtoCompra.getQuantidade();


        double quantidadeVenda =
                produtoVenda.getQuantidade() == null
                        ? 0.0
                        : produtoVenda.getQuantidade();


        // ========================================================
        // FORMATAÇÃO
        // ========================================================
        //
        // VALOR UNITÁRIO = 3 CASAS DECIMAIS
        //
        // Exemplo:
        //
        // R$ 0,063
        // R$ 0,114
        //
        // ========================================================

        java.text.DecimalFormat formato =
                new java.text.DecimalFormat(
                        "#,##0.000",
                        java.text.DecimalFormatSymbols
                                .getInstance(
                                        java.util.Locale
                                                .forLanguageTag("pt-BR")
                                )
                );


        // --------------------------------------------------------
        // QUANTIDADE
        // --------------------------------------------------------

        java.text.DecimalFormat formatoQuantidade =
                new java.text.DecimalFormat(
                        "#,##0",
                        java.text.DecimalFormatSymbols
                                .getInstance(
                                        java.util.Locale
                                                .forLanguageTag("pt-BR")
                                )
                );


        // --------------------------------------------------------
        // DATA
        // --------------------------------------------------------

        DateTimeFormatter formatoData =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yy"
                );


        // --------------------------------------------------------
        // DATAS
        // --------------------------------------------------------
        //
        // Utilizamos a última data encontrada no histórico,
        // mantendo a mesma lógica da tela principal.
        // --------------------------------------------------------

        String dataCompra =
                resumoCompra.ultimaData == null
                        ? ""
                        : resumoCompra.ultimaData
                          .format(formatoData);


        String dataVenda =
                resumoVenda.ultimaData == null
                        ? ""
                        : resumoVenda.ultimaData
                          .format(formatoData);


        // ========================================================
        // JANELA DE COMPARAÇÃO
        // ========================================================

        Alert comparacao =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        comparacao.setTitle(
                "Comparar Compra x Venda"
        );


        comparacao.setHeaderText(
                "Comparação do Produto"
        );


        // ========================================================
        // TEXTO
        // ========================================================

        String texto =

                "══════════════════════════════════\n"
                        + "              COMPRA\n"
                        + "══════════════════════════════════\n\n"

                        + "Código: "
                        + valorSeguro(codigoCompra)
                        + "\n"

                        + "Descrição: "
                        + valorSeguro(descricaoCompra)
                        + "\n"

                        + "Quantidade: "
                        + formatoQuantidade.format(
                        quantidadeCompra
                )
                        + "\n"

                        + "Valor unitário: R$ "
                        + formato.format(
                        valorCompra
                )
                        + (dataCompra.isBlank()
                        ? ""
                        : " (última compra em "
                          + dataCompra
                          + ")")
                        + "\n\n"


                        + "══════════════════════════════════\n"
                        + "               VENDA\n"
                        + "══════════════════════════════════\n\n"

                        + "Código: "
                        + valorSeguro(codigoVenda)
                        + "\n"

                        + "Descrição: "
                        + valorSeguro(descricaoVenda)
                        + "\n"

                        + "Quantidade: "
                        + formatoQuantidade.format(
                        quantidadeVenda
                )
                        + "\n"

                        + "Valor unitário: R$ "
                        + formato.format(
                        valorVenda
                )
                        + (dataVenda.isBlank()
                        ? ""
                        : " (última venda em "
                          + dataVenda
                          + ")")
                        + "\n\n"


                        + "══════════════════════════════════\n"
                        + "             RESULTADO\n"
                        + "══════════════════════════════════\n\n"

                        + "Diferença por unidade: R$ "
                        + formato.format(
                        diferenca
                )
                        + "\n"

                        + "Margem sobre o custo: "
                        + formato.format(
                        margem
                )
                        + "%";


        // --------------------------------------------------------
        // MOSTRA
        // --------------------------------------------------------

        comparacao.setContentText(
                texto
        );


        comparacao.showAndWait();
    }

    // ============================================================
    // CRIA RESUMO DO HISTÓRICO
    // ============================================================

    private Resumo criarResumo(
            List<ProdutoHistoricoDTO> historico,
            String tipoEsperado) {

        double quantidade = 0.0;

        double somaPrecos = 0.0;

        int quantidadePrecos = 0;

        java.time.LocalDateTime ultimaData = null;


        for (ProdutoHistoricoDTO item : historico) {

            if (item == null) {
                continue;
            }


            if (!tipoEsperado.equalsIgnoreCase(
                    item.getTipo())) {

                continue;
            }


            // ----------------------------------------------------
            // QUANTIDADE
            // ----------------------------------------------------

            if (item.getQuantidade() != null) {

                quantidade +=
                        item.getQuantidade();
            }


            // ----------------------------------------------------
            // VALOR UNITÁRIO
            // ----------------------------------------------------

            if (item.getValorUnitario() != null) {

                somaPrecos +=
                        item.getValorUnitario();

                quantidadePrecos++;
            }


            // ----------------------------------------------------
            // ÚLTIMA DATA
            // ----------------------------------------------------

            if (item.getDataEmissao() != null) {

                if (ultimaData == null
                        || item.getDataEmissao()
                        .isAfter(ultimaData)) {

                    ultimaData =
                            item.getDataEmissao();
                }
            }
        }


        // --------------------------------------------------------
        // PREÇO MÉDIO
        // --------------------------------------------------------

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

    private void vincular() {

        List<ProdutoHistoricoDTO> selecionados =
                view.getProdutosSelecionados();


        // --------------------------------------------------------
        // PRECISA SELECIONAR EXATAMENTE DOIS
        // --------------------------------------------------------

        if (selecionados.size() != 2) {

            mostrarAviso(
                    "Para vincular, selecione exatamente dois produtos:\n\n"
                            + "• 1 produto de Compra\n"
                            + "• 1 produto de Venda."
            );

            return;
        }


        ProdutoHistoricoDTO produto1 =
                selecionados.get(0);

        ProdutoHistoricoDTO produto2 =
                selecionados.get(1);


        // --------------------------------------------------------
        // IDENTIFICA COMPRA E VENDA
        // --------------------------------------------------------

        ProdutoHistoricoDTO produtoCompra = null;

        ProdutoHistoricoDTO produtoVenda = null;


        if ("Compra".equalsIgnoreCase(
                produto1.getTipo())) {

            produtoCompra = produto1;

        } else if ("Venda".equalsIgnoreCase(
                produto1.getTipo())) {

            produtoVenda = produto1;
        }


        if ("Compra".equalsIgnoreCase(
                produto2.getTipo())) {

            produtoCompra = produto2;

        } else if ("Venda".equalsIgnoreCase(
                produto2.getTipo())) {

            produtoVenda = produto2;
        }


        // --------------------------------------------------------
        // VERIFICA SE TEMOS UM DE CADA
        // --------------------------------------------------------

        if (produtoCompra == null
                || produtoVenda == null) {

            mostrarAviso(
                    "O vínculo precisa ser feito entre:\n\n"
                            + "1 produto de Compra\n"
                            + "1 produto de Venda."
            );

            return;
        }


        // --------------------------------------------------------
        // CÓDIGOS
        // --------------------------------------------------------

        String codigoCompra =
                produtoCompra.getCodigoProduto();

        String codigoVenda =
                produtoVenda.getCodigoProduto();


        if (codigoCompra == null
                || codigoCompra.isBlank()
                || codigoVenda == null
                || codigoVenda.isBlank()) {

            mostrarAviso(
                    "Não foi possível identificar os códigos dos produtos."
            );

            return;
        }


        codigoCompra =
                codigoCompra.trim();

        codigoVenda =
                codigoVenda.trim();


        // --------------------------------------------------------
        // JÁ EXISTE?
        // --------------------------------------------------------

        try {

            if (vinculoService.existeVinculo(
                    codigoCompra,
                    codigoVenda)) {

                mostrarAviso(
                        "Os produtos já estão vinculados."
                );

                return;
            }


            // ----------------------------------------------------
            // CONFIRMAÇÃO
            // ----------------------------------------------------

            Alert confirmacao =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmacao.setTitle(
                    "Vincular produtos"
            );

            confirmacao.setHeaderText(
                    "Confirmar vínculo"
            );

            confirmacao.setContentText(
                    "Produto de COMPRA:\n"
                            + codigoCompra
                            + "\n\n"
                            + "Produto de VENDA:\n"
                            + codigoVenda
                            + "\n\n"
                            + "Deseja realmente vincular estes produtos?"
            );


            var resposta =
                    confirmacao.showAndWait();


            if (resposta.isEmpty()
                    || resposta.get()
                    != ButtonType.OK) {

                return;
            }


            // ----------------------------------------------------
            // GRAVA
            // ----------------------------------------------------

            vinculoService.vincular(
                    codigoCompra,
                    codigoVenda
            );

            // ----------------------------------------------------
            // SUCESSO
            // ----------------------------------------------------

            Alert sucesso =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            sucesso.setTitle(
                    "Vincular produtos"
            );

            sucesso.setHeaderText(null);

            sucesso.setContentText(
                    "Produtos vinculados com sucesso."
            );

            sucesso.showAndWait();


            // ----------------------------------------------------
            // DESMARCA
            // ----------------------------------------------------

            view.limparSelecoes();


        } catch (Exception e) {

            e.printStackTrace();

            mostrarErro(
                    "Erro ao vincular os produtos.",
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // DESVINCULAR PRODUTOS
    // ============================================================

    private void desvincular() {

        List<ProdutoHistoricoDTO> selecionados =
                view.getProdutosSelecionados();


        if (selecionados.size() != 2) {

            mostrarAviso(
                    "Selecione exatamente dois produtos: " +
                            "um produto de Compra e um produto de Venda."
            );

            return;
        }


        ProdutoHistoricoDTO produto1 =
                selecionados.get(0);

        ProdutoHistoricoDTO produto2 =
                selecionados.get(1);


        ProdutoHistoricoDTO compra = null;

        ProdutoHistoricoDTO venda = null;


        if ("Compra".equalsIgnoreCase(
                produto1.getTipo())) {

            compra = produto1;

        } else if ("Venda".equalsIgnoreCase(
                produto1.getTipo())) {

            venda = produto1;
        }


        if ("Compra".equalsIgnoreCase(
                produto2.getTipo())) {

            compra = produto2;

        } else if ("Venda".equalsIgnoreCase(
                produto2.getTipo())) {

            venda = produto2;
        }


        if (compra == null
                || venda == null) {

            mostrarAviso(
                    "Selecione uma Compra e uma Venda."
            );

            return;
        }


        String codigoCompra =
                obterCodigoProduto(compra);

        String codigoVenda =
                obterCodigoProduto(venda);


        if (codigoCompra == null
                || codigoCompra.isBlank()
                || codigoVenda == null
                || codigoVenda.isBlank()) {

            mostrarAviso(
                    "Não foi possível identificar os códigos dos produtos."
            );

            return;
        }


        try {

            if (!vinculoRepository.existeVinculo(
                    codigoCompra,
                    codigoVenda
            )) {

                mostrarAviso(
                        "Esses produtos não estão vinculados."
                );

                return;
            }


            vinculoRepository.desvincular(
                    codigoCompra,
                    codigoVenda
            );


            Alert alerta =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alerta.setTitle(
                    "Desvincular produtos"
            );

            alerta.setHeaderText(
                    "Produtos desvinculados com sucesso!"
            );

            alerta.setContentText(
                    "Compra: "
                            + codigoCompra
                            + "\n"
                            + "Venda: "
                            + codigoVenda
            );

            alerta.showAndWait();


        } catch (Exception e) {

            e.printStackTrace();

            mostrarErro(
                    "Erro ao desvincular os produtos.",
                    e.getMessage()
            );
        }
    }


        // ============================================================
        // OBTÉM CÓDIGO DO PRODUTO
        // ============================================================

    private String obterCodigoProduto(
            ProdutoHistoricoDTO produto) {

        if (produto == null) {
            return null;
        }


        if (produto.getCodigoProduto() != null
                && !produto.getCodigoProduto().isBlank()) {

            return produto.getCodigoProduto().trim();
        }


        /*
         * Na versão atual do histórico, o código pode não
         * estar preenchido diretamente no DTO.
         *
         * Nesse caso, usamos a pesquisa atual somente como
         * fallback.
         */

        return null;
    }

    // ============================================================
    // GERAR NF-e INTEIRA EM PDF
    // LOCALIZA A NF-e NO BANCO
    // ============================================================

    private void gerarPdfNfe() {

        // --------------------------------------------------------
        // PEGA A LINHA SELECIONADA PELO BOTÃO DIREITO
        // --------------------------------------------------------

        ProdutoHistoricoDTO registro =
                view.getTabela()
                        .getSelectionModel()
                        .getSelectedItem();


        if (registro == null) {

            mostrarAviso(
                    "Selecione uma linha da NF-e."
            );

            return;
        }


        // --------------------------------------------------------
        // DADOS DA NF-e
        // --------------------------------------------------------

        String numero =
                registro.getNumeroNfe();

        String serie =
                registro.getSerie();

        String emitente =
                registro.getEmitente();


        // --------------------------------------------------------
        // VALIDAÇÃO
        // --------------------------------------------------------

        if (numero == null || numero.isBlank()) {

            mostrarAviso(
                    "Não foi possível identificar o número da NF-e."
            );

            return;
        }

        if (serie == null || serie.isBlank()) {

            mostrarAviso(
                    "Não foi possível identificar a série da NF-e."
            );

            return;
        }

        if (emitente == null || emitente.isBlank()) {

            mostrarAviso(
                    "Não foi possível identificar o emitente da NF-e."
            );

            return;
        }


        // --------------------------------------------------------
        // BUSCA A NF-e NO BANCO
        // --------------------------------------------------------

        try {

            var nfe =
                    nfeRepository.buscarPorNumeroSerieEmitente(
                            numero.trim(),
                            serie.trim(),
                            emitente.trim()
                    );


            // ----------------------------------------------------
            // NF-e NÃO ENCONTRADA
            // ----------------------------------------------------

            if (nfe == null) {

                mostrarAviso(
                        "A NF-e não foi encontrada no banco de dados.\n\n"
                                + "Número: "
                                + numero.trim()
                                + "\n"
                                + "Série: "
                                + serie.trim()
                                + "\n"
                                + "Emitente: "
                                + emitente.trim()
                );

                return;
            }


            // ----------------------------------------------------
            // VERIFICA ID
            // ----------------------------------------------------

            if (nfe.getId() == null) {

                mostrarAviso(
                        "A NF-e foi encontrada, mas não possui ID interno."
                );

                return;
            }


            // ----------------------------------------------------
            // BUSCA TODOS OS ITENS
            // ----------------------------------------------------

            List<br.com.intelifiscal.dto.nfeitem.NFeItemDTO> itens =
                    repository.listarPorIdNfe(
                            nfe.getId().intValue()
                    );


            // ----------------------------------------------------
            // VERIFICA ITENS
            // ----------------------------------------------------

            if (itens.isEmpty()) {

                mostrarAviso(
                        "A NF-e foi encontrada, mas nenhum item foi localizado.\n\n"
                                + "NF-e: "
                                + nfe.getNumero()
                                + "\n"
                                + "Série: "
                                + nfe.getSerie()
                );

                return;
            }


            // ====================================================
            // ESCOLHE ONDE SALVAR O PDF
            // ====================================================

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Salvar NF-e em PDF"
            );


            fileChooser.setInitialFileName(
                    "NFe_"
                            + nfe.getNumero()
                            + "_Serie_"
                            + nfe.getSerie()
                            + ".pdf"
            );


            fileChooser.getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Arquivo PDF (*.pdf)",
                                    "*.pdf"
                            )
                    );


            File arquivo =
                    fileChooser.showSaveDialog(
                            view.getScene().getWindow()
                    );


            // ----------------------------------------------------
            // USUÁRIO CANCELou
            // ----------------------------------------------------

            if (arquivo == null) {

                return;
            }


            // ====================================================
            // GARANTE EXTENSÃO .PDF
            // ====================================================

            String caminho =
                    arquivo.getAbsolutePath();

            if (!caminho.toLowerCase()
                    .endsWith(".pdf")) {

                arquivo =
                        new File(
                                caminho + ".pdf"
                        );
            }


            // ====================================================
            // GERA O PDF
            // ====================================================

            nfePdfService.gerar(
                    nfe,
                    itens,
                    arquivo
            );


            // ====================================================
            // SUCESSO
            // ====================================================

            Alert sucesso =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            sucesso.setTitle(
                    "NF-e em PDF"
            );

            sucesso.setHeaderText(
                    "PDF gerado com sucesso!"
            );

            sucesso.setContentText(
                    "NF-e: "
                            + nfe.getNumero()
                            + "\n"
                            + "Série: "
                            + nfe.getSerie()
                            + "\n"
                            + "Itens: "
                            + itens.size()
                            + "\n\n"
                            + "Arquivo salvo em:\n"
                            + arquivo.getAbsolutePath()
            );

            sucesso.showAndWait();


        } catch (Exception e) {

            e.printStackTrace();

            mostrarErro(
                    "Erro ao gerar o PDF da NF-e.",
                    e.getMessage()
            );
        }
    }


    // ============================================================
    // BOTÃO DIREITO - COPIAR CÓDIGO
    // ============================================================

    private void copiarCodigo() {

        List<ProdutoHistoricoDTO> selecionados =
                view.getProdutosSelecionados();

        if (selecionados.size() != 1) {

            mostrarAviso(
                    "Selecione exatamente um registro."
            );

            return;
        }

        String codigo =
                selecionados.get(0).getCodigoProduto();

        copiarParaAreaTransferencia(codigo);
    }


    // ============================================================
    // BOTÃO DIREITO - COPIAR DESCRIÇÃO
    // ============================================================

    private void copiarDescricao() {

        List<ProdutoHistoricoDTO> selecionados =
                view.getProdutosSelecionados();

        if (selecionados.size() != 1) {

            mostrarAviso(
                    "Selecione exatamente um registro."
            );

            return;
        }

        String descricao =
                selecionados.get(0).getDescricao();

        copiarParaAreaTransferencia(descricao);
    }


    // ============================================================
    // BOTÃO DIREITO - COPIAR NF-e
    // ============================================================

    private void copiarNfe() {

        List<ProdutoHistoricoDTO> selecionados =
                view.getProdutosSelecionados();

        if (selecionados.size() != 1) {

            mostrarAviso(
                    "Selecione exatamente um registro."
            );

            return;
        }

        String numero =
                selecionados.get(0).getNumeroNfe();

        copiarParaAreaTransferencia(numero);
    }

    // ============================================================
    // COPIAR PARA ÁREA DE TRANSFERÊNCIA
    // ============================================================

    private void copiarParaAreaTransferencia(
            String texto) {

        if (texto == null) {
            texto = "";
        }

        Clipboard clipboard =
                Clipboard.getSystemClipboard();

        ClipboardContent content =
                new ClipboardContent();

        content.putString(texto);

        clipboard.setContent(content);
    }

    // ============================================================
    // VALOR SEGURO
    // ============================================================

    private String valorSeguro(String valor) {

        return valor == null
                ? ""
                : valor.trim();
    }

    // ============================================================
    // ALERTA
    // ============================================================

    private void mostrarAviso(
            String mensagem) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alerta.setTitle(
                "Histórico do Produto"
        );

        alerta.setHeaderText(null);

        alerta.setContentText(
                mensagem
        );

        alerta.showAndWait();
    }


    // ============================================================
    // ERRO
    // ============================================================

    private void mostrarErro(
            String titulo,
            String mensagem) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Histórico do Produto"
        );

        alerta.setHeaderText(
                titulo
        );

        alerta.setContentText(
                mensagem == null
                        ? "Erro desconhecido."
                        : mensagem
        );

        alerta.showAndWait();
    }

    // ============================================================
    // CLASSE RESUMO
    // ============================================================

    private static class Resumo {

        private final double quantidade;

        private final double precoMedio;

        private final boolean temMovimento;

        private final java.time.LocalDateTime ultimaData;


        private Resumo(
                double quantidade,
                double precoMedio,
                boolean temMovimento,
                java.time.LocalDateTime ultimaData) {

            this.quantidade = quantidade;

            this.precoMedio = precoMedio;

            this.temMovimento = temMovimento;

            this.ultimaData = ultimaData;
        }
    }
}