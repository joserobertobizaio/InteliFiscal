package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.produto.ProdutoView;
import br.com.intelifiscal.service.produto.ProdutoService;

import br.com.intelifiscal.fx.controller.produto.CompararCompraVendaController;
import br.com.intelifiscal.fx.view.produto.CompararCompraVendaView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import br.com.intelifiscal.dto.produto.ProdutoHistoricoResumoDTO;
import java.util.List;

import java.time.LocalDate;

public class ProdutoController {

    private final ProdutoView view;

    private final ProdutoService service =
            new ProdutoService();

    private final ObservableList<ProdutoDTO> produtos =
            FXCollections.observableArrayList();

    private ProdutoDTO produtoSelecionado;

    private boolean modoEdicao = false;


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ProdutoController(ProdutoView view) {

        this.view = view;

        configurarTabela();

        configurarEventos();

        configurarPeriodoInicial();

        carregarProdutos();

        bloquearFormulario();

        atualizarEstadoBotoes();
    }


    //==================================================
    // PERÍODO INICIAL
    //==================================================

    private void configurarPeriodoInicial() {

        LocalDate hoje =
                LocalDate.now();

        view.getCmbPeriodo()
                .setValue(
                        "Últimos 12 meses"
                );

        view.getDtInicio()
                .setValue(
                        hoje.minusMonths(12)
                );

        view.getDtFim()
                .setValue(
                        hoje
                );

        view.atualizarControlesPeriodo();
    }


    //==================================================
    // CONFIGURA TABELA
    //==================================================

    private void configurarTabela() {

        TableColumn<ProdutoDTO, String> colCodigo =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(0);

        TableColumn<ProdutoDTO, String> colCodigoBarras =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(1);

        TableColumn<ProdutoDTO, String> colDescricao =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(2);

        TableColumn<ProdutoDTO, String> colNcm =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(3);

        TableColumn<ProdutoDTO, String> colCest =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(4);

        TableColumn<ProdutoDTO, String> colUnidade =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(5);

        TableColumn<ProdutoDTO, String> colAtivo =
                (TableColumn<ProdutoDTO, String>)
                        view.getTabelaProdutos()
                                .getColumns()
                                .get(6);


        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>(
                        "codigoProduto"
                )
        );

        colCodigoBarras.setCellValueFactory(
                new PropertyValueFactory<>(
                        "codigoBarras"
                )
        );

        colDescricao.setCellValueFactory(
                new PropertyValueFactory<>(
                        "descricao"
                )
        );

        colNcm.setCellValueFactory(
                new PropertyValueFactory<>(
                        "ncm"
                )
        );

        colCest.setCellValueFactory(
                new PropertyValueFactory<>(
                        "cest"
                )
        );

        colUnidade.setCellValueFactory(
                new PropertyValueFactory<>(
                        "unidade"
                )
        );


        //---------------------------------------------
        // ATIVO
        //---------------------------------------------

        colAtivo.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                cellData.getValue().isAtivo()
                                        ? "Sim"
                                        : "Não"
                        )
        );


        view.getTabelaProdutos()
                .setItems(produtos);
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        //---------------------------------------------
        // SELEÇÃO DO PRODUTO
        //---------------------------------------------

        view.getTabelaProdutos()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, antigo, novo) ->
                                selecionarProduto(novo)
                );


        //---------------------------------------------
        // PESQUISA
        //---------------------------------------------

        view.getTxtPesquisa()
                .textProperty()
                .addListener(
                        (obs, antigo, novo) ->
                                pesquisar(novo)
                );


        //---------------------------------------------
        // BOTÕES
        //---------------------------------------------

        view.getCrudButtonBar()
                .getBtNovo()
                .setOnAction(
                        event -> editar()
                );


        view.getCrudButtonBar()
                .getBtSalvar()
                .setOnAction(
                        event -> salvar()
                );


        view.getCrudButtonBar()
                .getBtFechar()
                .setOnAction(
                        event -> fechar()
                );


        //---------------------------------------------
        // PERÍODO
        //---------------------------------------------

        view.getCmbPeriodo()
                .setOnAction(
                        event -> alterarPeriodo()
                );


        //---------------------------------------------
        // CONSULTAR HISTÓRICO
        //---------------------------------------------

        view.getBtConsultarHistorico()
                .setOnAction(
                        event -> consultarHistorico()
                );

        view.getBtCompararCompraVenda()
                .setOnAction(event -> abrirComparacao());
    }


    private void abrirComparacao() {

        CompararCompraVendaView viewComparacao =
                new CompararCompraVendaView();

        new CompararCompraVendaController(
                viewComparacao
        );

        NavigationManager.show(
                viewComparacao
        );
    }


    //==================================================
    // ALTERA PERÍODO
    //==================================================

    private void alterarPeriodo() {

        String periodo =
                view.getCmbPeriodo()
                        .getValue();


        if (periodo == null) {

            return;
        }


        //---------------------------------------------
        // DESDE O INÍCIO
        //---------------------------------------------

        if ("Desde o início".equals(periodo)) {

            view.atualizarControlesPeriodo();

            consultarHistorico();

            return;
        }


        //---------------------------------------------
        // PERSONALIZADO
        //---------------------------------------------

        if ("Período personalizado".equals(periodo)) {

            view.atualizarControlesPeriodo();

            return;
        }


        //---------------------------------------------
        // PERÍODOS PRÉ-DEFINIDOS
        //---------------------------------------------

        LocalDate hoje =
                LocalDate.now();


        switch (periodo) {

            case "Últimos 30 dias" -> {

                view.getDtInicio()
                        .setValue(
                                hoje.minusDays(30)
                        );
            }


            case "Últimos 90 dias" -> {

                view.getDtInicio()
                        .setValue(
                                hoje.minusDays(90)
                        );
            }


            case "Últimos 6 meses" -> {

                view.getDtInicio()
                        .setValue(
                                hoje.minusMonths(6)
                        );
            }


            case "Últimos 12 meses" -> {

                view.getDtInicio()
                        .setValue(
                                hoje.minusMonths(12)
                        );
            }


            case "Últimos 24 meses" -> {

                view.getDtInicio()
                        .setValue(
                                hoje.minusMonths(24)
                        );
            }
        }


        view.getDtFim()
                .setValue(hoje);


        view.atualizarControlesPeriodo();


        //---------------------------------------------
        // CONSULTA AUTOMÁTICA
        //---------------------------------------------

        consultarHistorico();
    }


    //==================================================
    // CONSULTA HISTÓRICO
    //==================================================

    private void consultarHistorico() {

        if (produtoSelecionado == null) {

            return;
        }


        String periodo =
                view.getCmbPeriodo()
                        .getValue();


        //---------------------------------------------
        // DESDE O INÍCIO
        //---------------------------------------------

        if ("Desde o início".equals(periodo)) {

            carregarHistorico(
                    produtoSelecionado,
                    null,
                    null
            );

            return;
        }


        //---------------------------------------------
        // PERÍODO PERSONALIZADO
        //---------------------------------------------

        LocalDate inicio =
                view.getDtInicio()
                        .getValue();

        LocalDate fim =
                view.getDtFim()
                        .getValue();


        if (inicio == null || fim == null) {

            if ("Período personalizado".equals(periodo)) {

                Alert alerta =
                        new Alert(
                                Alert.AlertType.WARNING
                        );

                alerta.setTitle(
                        "Histórico do produto"
                );

                alerta.setHeaderText(
                        "Período não informado"
                );

                alerta.setContentText(
                        "Informe a data inicial e a data final."
                );

                alerta.showAndWait();
            }

            return;
        }


        //---------------------------------------------
        // VALIDA PERÍODO
        //---------------------------------------------

        if (inicio.isAfter(fim)) {

            Alert alerta =
                    new Alert(
                            Alert.AlertType.WARNING
                    );

            alerta.setTitle(
                    "Histórico do produto"
            );

            alerta.setHeaderText(
                    "Período inválido"
            );

            alerta.setContentText(
                    "A data inicial não pode ser maior que a data final."
            );

            alerta.showAndWait();

            return;
        }


        //---------------------------------------------
        // CONSULTA
        //---------------------------------------------

        carregarHistorico(
                produtoSelecionado,
                inicio,
                fim
        );
    }

    //==================================================
    // CARREGA HISTÓRICO
    //==================================================

    private void carregarHistorico(
            ProdutoDTO produto,
            LocalDate inicio,
            LocalDate fim) {

        if (produto == null
                || produto.getCodigoProduto() == null
                || produto.getCodigoProduto().isBlank()) {

            view.getTabelaHistorico()
                    .getItems()
                    .clear();

            return;
        }


        List<ProdutoHistoricoDTO> historico;


        //---------------------------------------------
        // DESDE O INÍCIO
        //---------------------------------------------

        if (inicio == null && fim == null) {

            historico =
                    service.listarHistoricoPorCodigoProduto(
                            produto.getCodigoProduto()
                    );

        } else {

            //---------------------------------------------
            // PERÍODO COM DATAS
            //---------------------------------------------

            historico =
                    service.listarHistoricoPorCodigoProduto(
                            produto.getCodigoProduto(),
                            inicio,
                            fim
                    );
        }


        view.getTabelaHistorico()
                .getItems()
                .setAll(historico);

        ProdutoHistoricoResumoDTO resumo =
                calcularResumoHistorico(historico);

        //==================================================
// ATUALIZA RESUMO NA TELA
//==================================================

        view.getLblQtdCompras().setText(
                formatarQuantidade(resumo.getQuantidadeComprada())
                        + " "
                        + obterUnidade(historico, "Compra")
        );

        view.getLblValorCompras().setText(
                formatarValor(resumo.getValorTotalComprado())
        );

        view.getLblMenorPrecoCompra().setText(
                formatarValor(resumo.getMenorPrecoCompra())
        );

        view.getLblMaiorPrecoCompra().setText(
                formatarValor(resumo.getMaiorPrecoCompra())
        );

        view.getLblPrecoMedioCompra().setText(
                formatarValor(resumo.getPrecoMedioCompra())
        );

        view.getLblQtdVendas().setText(
                formatarQuantidade(resumo.getQuantidadeVendida())
                        + " "
                        + obterUnidade(historico, "Venda")
        );


        view.getLblValorVendas().setText(
                formatarValor(resumo.getValorTotalVendido())
        );

        view.getLblMenorPrecoVenda().setText(
                formatarValor(resumo.getMenorPrecoVenda())
        );

        view.getLblMaiorPrecoVenda().setText(
                formatarValor(resumo.getMaiorPrecoVenda())
        );

        view.getLblPrecoMedioVenda().setText(
                formatarValor(resumo.getPrecoMedioVenda())
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println("       RESUMO DO PRODUTO");
        System.out.println("========================================");

        System.out.println("COMPRAS");
        System.out.println("Quantidade: "
                + resumo.getQuantidadeComprada());

        System.out.println("Valor total: "
                + resumo.getValorTotalComprado());

        System.out.println("Menor preço: "
                + resumo.getMenorPrecoCompra());

        System.out.println("Maior preço: "
                + resumo.getMaiorPrecoCompra());

        System.out.println("Preço médio: "
                + resumo.getPrecoMedioCompra());

        System.out.println();

        System.out.println("VENDAS");
        System.out.println("Quantidade: "
                + resumo.getQuantidadeVendida());

        System.out.println("Valor total: "
                + resumo.getValorTotalVendido());

        System.out.println("Menor preço: "
                + resumo.getMenorPrecoVenda());

        System.out.println("Maior preço: "
                + resumo.getMaiorPrecoVenda());

        System.out.println("Preço médio: "
                + resumo.getPrecoMedioVenda());

        System.out.println("========================================");

        for (ProdutoHistoricoDTO item : historico) {

            System.out.println(
                    "TIPO = " + item.getTipo()
                            + " | QTD = " + item.getQuantidade()
                            + " | VALOR = " + item.getValorUnitario()
            );
        }

    }


    //==================================================
    // SELECIONA PRODUTO
    //==================================================

    private void selecionarProduto(
            ProdutoDTO produto) {

        produtoSelecionado = produto;


        if (produto == null) {

            limparFormulario();

            bloquearFormulario();

            view.getTabelaHistorico()
                    .getItems()
                    .clear();

            atualizarEstadoBotoes();

            return;
        }


        carregarFormulario(produto);


        //---------------------------------------------
        // CARREGA HISTÓRICO DO PERÍODO ATUAL
        //---------------------------------------------

        consultarHistorico();

        bloquearFormulario();

        modoEdicao = false;

        atualizarEstadoBotoes();
    }


    //==================================================
    // EDITAR
    //==================================================

    private void editar() {

        if (produtoSelecionado == null) {

            return;
        }


        modoEdicao = true;

        atualizarEstadoBotoes();

        desbloquearFormulario();


        Platform.runLater(() -> {

            view.getTxtDescricao()
                    .requestFocus();

            posicionarDescricao();
        });
    }


    //==================================================
    // POSICIONA DESCRIÇÃO NO SCROLL
    //==================================================

    private void posicionarDescricao() {

        if (view.getScrollPane() == null) {

            return;
        }


        Platform.runLater(() -> {

            ScrollPane scrollPane =
                    view.getScrollPane();

            javafx.scene.Node campo =
                    view.getTxtDescricao();

            javafx.scene.Node conteudo =
                    scrollPane.getContent();


            if (campo == null
                    || conteudo == null) {

                return;
            }


            javafx.geometry.Bounds campoBounds =
                    campo.localToScene(
                            campo.getBoundsInLocal()
                    );

            javafx.geometry.Bounds conteudoBounds =
                    conteudo.localToScene(
                            conteudo.getBoundsInLocal()
                    );


            double campoY =
                    campoBounds.getMinY()
                            - conteudoBounds.getMinY();


            double viewportHeight =
                    scrollPane.getViewportBounds()
                            .getHeight();


            double conteudoHeight =
                    conteudoBounds.getHeight();


            double maxScroll =
                    conteudoHeight
                            - viewportHeight;


            if (maxScroll <= 0) {

                return;
            }


            double novoVvalue =
                    campoY / maxScroll;


            novoVvalue =
                    Math.max(
                            0,
                            Math.min(
                                    1,
                                    novoVvalue
                            )
                    );


            scrollPane.setVvalue(
                    novoVvalue
            );
        });
    }


    //==================================================
    // CARREGA PRODUTOS
    //==================================================

    private void carregarProdutos() {

        List<ProdutoDTO> lista =
                service.listarTodos();

        produtos.setAll(lista);
    }


    //==================================================
    // SALVAR
    //==================================================

    private void salvar() {

        if (!modoEdicao
                || produtoSelecionado == null) {

            return;
        }


        try {

            produtoSelecionado.setCodigoProduto(
                    view.getTxtCodigoProduto()
                            .getText()
                            .trim()
            );

            produtoSelecionado.setCodigoBarras(
                    view.getTxtCodigoBarras()
                            .getText()
                            .trim()
            );

            produtoSelecionado.setDescricao(
                    view.getTxtDescricao()
                            .getText()
                            .trim()
            );

            produtoSelecionado.setNcm(
                    view.getTxtNcm()
                            .getText()
                            .trim()
            );

            produtoSelecionado.setCest(
                    view.getTxtCest()
                            .getText()
                            .trim()
            );

            produtoSelecionado.setUnidade(
                    view.getTxtUnidade()
                            .getText()
                            .trim()
            );

            produtoSelecionado.setAtivo(
                    view.getChkAtivo()
                            .isSelected()
            );


            service.atualizar(
                    produtoSelecionado
            );


            carregarProdutos();


            modoEdicao = false;

            bloquearFormulario();

            atualizarEstadoBotoes();


            Alert alerta =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alerta.setTitle(
                    "Produto"
            );

            alerta.setHeaderText(
                    "Produto salvo com sucesso!"
            );

            alerta.setContentText(
                    "As alterações foram gravadas no banco de dados."
            );

            alerta.showAndWait();


        } catch (Exception e) {

            e.printStackTrace();


            Alert alerta =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alerta.setTitle(
                    "Erro"
            );

            alerta.setHeaderText(
                    "Não foi possível salvar o produto."
            );

            alerta.setContentText(
                    e.getMessage()
            );

            alerta.showAndWait();
        }
    }


    //==================================================
    // EXCLUIR
    //==================================================

    private void excluir() {

        if (produtoSelecionado == null) {

            return;
        }


        try {

            service.excluir(
                    produtoSelecionado.getId()
            );


            produtos.remove(
                    produtoSelecionado
            );


            produtoSelecionado = null;


            view.getTabelaHistorico()
                    .getItems()
                    .clear();


            limparFormulario();

            bloquearFormulario();

            atualizarEstadoBotoes();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    //==================================================
    // CARREGA FORMULÁRIO
    //==================================================

    private void carregarFormulario(
            ProdutoDTO produto) {

        view.getTxtCodigoProduto()
                .setText(
                        valor(
                                produto.getCodigoProduto()
                        )
                );


        view.getTxtCodigoBarras()
                .setText(
                        valor(
                                produto.getCodigoBarras()
                        )
                );


        view.getTxtDescricao()
                .setText(
                        valor(
                                produto.getDescricao()
                        )
                );


        view.getTxtNcm()
                .setText(
                        valor(
                                produto.getNcm()
                        )
                );


        view.getTxtCest()
                .setText(
                        valor(
                                produto.getCest()
                        )
                );


        view.getTxtUnidade()
                .setText(
                        valor(
                                produto.getUnidade()
                        )
                );


        view.getChkAtivo()
                .setSelected(
                        produto.isAtivo()
                );
    }


    //==================================================
    // LIMPA FORMULÁRIO
    //==================================================

    private void limparFormulario() {

        view.getTxtCodigoProduto()
                .clear();

        view.getTxtCodigoBarras()
                .clear();

        view.getTxtDescricao()
                .clear();

        view.getTxtNcm()
                .clear();

        view.getTxtCest()
                .clear();

        view.getTxtUnidade()
                .clear();

        view.getChkAtivo()
                .setSelected(true);

        modoEdicao = false;
    }


    //==================================================
    // BLOQUEIA FORMULÁRIO
    //==================================================

    private void bloquearFormulario() {

        view.getTxtCodigoProduto()
                .setDisable(true);

        view.getTxtCodigoBarras()
                .setDisable(true);

        view.getTxtDescricao()
                .setDisable(true);

        view.getTxtNcm()
                .setDisable(true);

        view.getTxtCest()
                .setDisable(true);

        view.getTxtUnidade()
                .setDisable(true);

        view.getChkAtivo()
                .setDisable(true);
    }


    //==================================================
    // DESBLOQUEIA FORMULÁRIO
    //==================================================

    private void desbloquearFormulario() {

        view.getTxtCodigoProduto()
                .setDisable(false);

        view.getTxtCodigoBarras()
                .setDisable(false);

        view.getTxtDescricao()
                .setDisable(false);

        view.getTxtNcm()
                .setDisable(false);

        view.getTxtCest()
                .setDisable(false);

        view.getTxtUnidade()
                .setDisable(false);

        view.getChkAtivo()
                .setDisable(false);
    }


    //==================================================
    // ESTADO DOS BOTÕES
    //==================================================

    private void atualizarEstadoBotoes() {

        boolean selecionado =
                produtoSelecionado != null;

        boolean editando =
                modoEdicao;


        view.getCrudButtonBar()
                .getBtNovo()
                .setDisable(
                        !selecionado || editando
                );


        view.getCrudButtonBar()
                .getBtExcluir()
                .setDisable(
                        !selecionado || editando
                );


        view.getCrudButtonBar()
                .getBtFechar()
                .setDisable(
                        editando
                );


        view.getCrudButtonBar()
                .getBtSalvar()
                .setDisable(
                        !editando
                );
    }


    //==================================================
    // VALOR
    //==================================================

    private String valor(String valor) {

        return valor == null
                ? ""
                : valor;
    }


    //==================================================
    // PESQUISA
    //==================================================

    private void pesquisar(String texto) {

        if (texto == null
                || texto.isBlank()) {

            carregarProdutos();

            return;
        }


        String filtro =
                texto.trim().toLowerCase();


        List<ProdutoDTO> lista =
                service.listarTodos()
                        .stream()
                        .filter(produto ->
                                contem(
                                        produto.getCodigoProduto(),
                                        filtro
                                )
                                        ||
                                        contem(
                                                produto.getCodigoBarras(),
                                                filtro
                                        )
                                        ||
                                        contem(
                                                produto.getDescricao(),
                                                filtro
                                        )
                        )
                        .toList();


        produtos.setAll(lista);

        produtoSelecionado = null;

        limparFormulario();

        bloquearFormulario();

        atualizarEstadoBotoes();
    }


    //==================================================
    // CONTÉM
    //==================================================

    private boolean contem(
            String valor,
            String filtro) {

        return valor != null
                && valor.toLowerCase()
                .contains(filtro);
    }


    //==================================================
    // FECHAR
    //==================================================

    private void fechar() {

        NavigationManager.show(
                ScreenType.DASHBOARD
        );
    }

    private ProdutoHistoricoResumoDTO calcularResumoHistorico(
            List<ProdutoHistoricoDTO> historico
    ) {

        ProdutoHistoricoResumoDTO resumo =
                new ProdutoHistoricoResumoDTO();

        double somaPrecosCompra = 0;
        double somaPrecosVenda = 0;

        int quantidadeRegistrosCompra = 0;
        int quantidadeRegistrosVenda = 0;

        double menorCompra = Double.MAX_VALUE;
        double maiorCompra = Double.MIN_VALUE;

        double menorVenda = Double.MAX_VALUE;
        double maiorVenda = Double.MIN_VALUE;


        for (ProdutoHistoricoDTO item : historico) {

            if (item.getTipo() == null) {
                continue;
            }


            //==================================================
            // COMPRAS
            //==================================================

            if (item.getTipo().equalsIgnoreCase("Compra")) {

                resumo.setQuantidadeComprada(
                        resumo.getQuantidadeComprada()
                                + item.getQuantidade()
                );

                resumo.setValorTotalComprado(
                        resumo.getValorTotalComprado()
                                + item.getValorTotal()
                );


                double preco = item.getValorUnitario();


                if (preco < menorCompra) {
                    menorCompra = preco;
                }

                if (preco > maiorCompra) {
                    maiorCompra = preco;
                }


                somaPrecosCompra += preco;
                quantidadeRegistrosCompra++;
            }


            //==================================================
            // VENDAS
            //==================================================

            else if (item.getTipo().equalsIgnoreCase("Venda")) {

                resumo.setQuantidadeVendida(
                        resumo.getQuantidadeVendida()
                                + item.getQuantidade()
                );

                resumo.setValorTotalVendido(
                        resumo.getValorTotalVendido()
                                + item.getValorTotal()
                );


                double preco = item.getValorUnitario();


                if (preco < menorVenda) {
                    menorVenda = preco;
                }

                if (preco > maiorVenda) {
                    maiorVenda = preco;
                }


                somaPrecosVenda += preco;
                quantidadeRegistrosVenda++;
            }
        }


        //==================================================
        // RESULTADOS DAS COMPRAS
        //==================================================

        if (quantidadeRegistrosCompra > 0) {

            resumo.setMenorPrecoCompra(menorCompra);

            resumo.setMaiorPrecoCompra(maiorCompra);

            resumo.setPrecoMedioCompra(
                    somaPrecosCompra /
                            quantidadeRegistrosCompra
            );
        }


        //==================================================
        // RESULTADOS DAS VENDAS
        //==================================================

        if (quantidadeRegistrosVenda > 0) {

            resumo.setMenorPrecoVenda(menorVenda);

            resumo.setMaiorPrecoVenda(maiorVenda);

            resumo.setPrecoMedioVenda(
                    somaPrecosVenda /
                            quantidadeRegistrosVenda
            );
        }
        return resumo;
    }

    //==================================================
    // OBTÉM UNIDADE DO HISTÓRICO
    //==================================================

    private String obterUnidade(
            List<ProdutoHistoricoDTO> historico,
            String tipo
    ) {

        if (historico == null) {
            return "";
        }

        for (ProdutoHistoricoDTO item : historico) {

            if (item.getTipo() == null) {
                continue;
            }

            if (item.getTipo().equalsIgnoreCase(tipo)) {

                if (item.getUnidade() != null
                        && !item.getUnidade().isBlank()) {

                    return item.getUnidade();
                }
            }
        }

        return "";
    }

    //==================================================
    // FORMATA QUANTIDADE
    //==================================================

    private String formatarQuantidade(double valor) {

        return String.format(
                java.util.Locale.forLanguageTag("pt-BR"),
                "%,.0f",
                valor
        );
    }


//==================================================
// FORMATA VALOR
//==================================================

    private String formatarValor(double valor) {

        return String.format(
                java.util.Locale.forLanguageTag("pt-BR"),
                "R$ %,.2f",
                valor
        );
    }

}