package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.produto.ProdutoView;
import br.com.intelifiscal.service.produto.ProdutoService;
import javafx.scene.control.ScrollPane;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;

import java.util.List;

public class ProdutoController {

    private final ProdutoView view;

    private final ProdutoService service =
            new ProdutoService();

    private final ObservableList<ProdutoDTO> produtos =
            FXCollections.observableArrayList();

    private ProdutoDTO produtoSelecionado;

    private boolean modoEdicao = false;

    public ProdutoController(ProdutoView view) {

        this.view = view;

        configurarTabela();

        configurarEventos();

        carregarProdutos();

        bloquearFormulario();

        atualizarEstadoBotoes();
    }


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
                new PropertyValueFactory<>("codigoProduto")
        );

        colCodigoBarras.setCellValueFactory(
                new PropertyValueFactory<>("codigoBarras")
        );

        colDescricao.setCellValueFactory(
                new PropertyValueFactory<>("descricao")
        );

        colNcm.setCellValueFactory(
                new PropertyValueFactory<>("ncm")
        );

        colCest.setCellValueFactory(
                new PropertyValueFactory<>("cest")
        );

        colUnidade.setCellValueFactory(
                new PropertyValueFactory<>("unidade")
        );


        //---------------------------------------------
        // ATIVO → SIM / NÃO
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


    private void configurarEventos() {

        view.getTabelaProdutos()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, antigo, novo) ->
                                selecionarProduto(novo)
                );


        view.getTxtPesquisa()
                .textProperty()
                .addListener(
                        (obs, antigo, novo) ->
                                pesquisar(novo)
                );


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
                .getBtExcluir()
                .setOnAction(
                        event -> excluir()
                );


        view.getCrudButtonBar()
                .getBtFechar()
                .setOnAction(
                        event -> fechar()
                );
    }

    private void carregarProdutos() {

        List<ProdutoDTO> lista =
                service.listarTodos();

        produtos.setAll(lista);
    }

    //==================================================
    // CARREGA HISTÓRICO DO PRODUTO
    //==================================================

    private void carregarHistorico(
            ProdutoDTO produto) {

        if (produto == null
                || produto.getCodigoProduto() == null
                || produto.getCodigoProduto().isBlank()) {

            view.getTabelaHistorico()
                    .getItems()
                    .clear();

            return;
        }

        List<ProdutoHistoricoDTO> historico =
                service.listarHistoricoPorCodigoProduto(
                        produto.getCodigoProduto()
                );

        view.getTabelaHistorico()
                .getItems()
                .setAll(historico);
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

        carregarHistorico(produto);

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

        //---------------------------------------------
        // FOCO NA DESCRIÇÃO
        //---------------------------------------------

        Platform.runLater(() -> {

            view.getTxtDescricao().requestFocus();

            posicionarDescricao();
        });
    }


    //==================================================
    // POSICIONA DESCRIÇÃO NO SCROLL
    //==================================================

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

            if (campo == null || conteudo == null) {
                return;
            }

            //---------------------------------------------
            // POSIÇÃO DO CAMPO DENTRO DO CONTEÚDO
            //---------------------------------------------

            javafx.geometry.Bounds campoBounds =
                    campo.localToScene(
                            campo.getBoundsInLocal()
                    );

            javafx.geometry.Bounds conteudoBounds =
                    conteudo.localToScene(
                            conteudo.getBoundsInLocal()
                    );

            //---------------------------------------------
            // DISTÂNCIA DO CAMPO ATÉ O TOPO DO CONTEÚDO
            //---------------------------------------------

            double campoY =
                    campoBounds.getMinY()
                            - conteudoBounds.getMinY();

            //---------------------------------------------
            // ALTURA VISÍVEL DO SCROLL
            //---------------------------------------------

            double viewportHeight =
                    scrollPane.getViewportBounds()
                            .getHeight();

            //---------------------------------------------
            // ALTURA TOTAL DO CONTEÚDO
            //---------------------------------------------

            double conteudoHeight =
                    conteudoBounds.getHeight();

            //---------------------------------------------
            // QUANTO PODE ROLAR
            //---------------------------------------------

            double maxScroll =
                    conteudoHeight
                            - viewportHeight;

            if (maxScroll <= 0) {
                return;
            }

            //---------------------------------------------
            // POSIÇÃO DESEJADA
            //---------------------------------------------

            double novoVvalue =
                    campoY / maxScroll;

            //---------------------------------------------
            // LIMITA ENTRE 0 E 1
            //---------------------------------------------

            novoVvalue =
                    Math.max(
                            0,
                            Math.min(
                                    1,
                                    novoVvalue
                            )
                    );

            //---------------------------------------------
            // APLICA O SCROLL
            //---------------------------------------------

            scrollPane.setVvalue(
                    novoVvalue
            );
        });
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


            //-----------------------------------------
            // ATUALIZA BANCO
            //-----------------------------------------

            service.atualizar(
                    produtoSelecionado
            );


            //-----------------------------------------
            // ATUALIZA TABELA
            //-----------------------------------------

            carregarProdutos();

            modoEdicao = false;

            bloquearFormulario();

            atualizarEstadoBotoes();

            //-----------------------------------------
            // MENSAGEM
            //-----------------------------------------

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


            //-----------------------------------------
            // ERRO
            //-----------------------------------------

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
                        valor(produto.getCodigoProduto())
                );

        view.getTxtCodigoBarras()
                .setText(
                        valor(produto.getCodigoBarras())
                );

        view.getTxtDescricao()
                .setText(
                        valor(produto.getDescricao())
                );

        view.getTxtNcm()
                .setText(
                        valor(produto.getNcm())
                );

        view.getTxtCest()
                .setText(
                        valor(produto.getCest())
                );

        view.getTxtUnidade()
                .setText(
                        valor(produto.getUnidade())
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
    // CONTROLE DOS BOTÕES
    //==================================================

    private void atualizarEstadoBotoes() {

        boolean produtoSelecionado =
                this.produtoSelecionado != null;

        boolean editando =
                this.modoEdicao;


        // EDITAR
        view.getCrudButtonBar()
                .getBtNovo()
                .setDisable(
                        !produtoSelecionado
                                || editando
                );


        // EXCLUIR
        view.getCrudButtonBar()
                .getBtExcluir()
                .setDisable(
                        !produtoSelecionado
                );


        // FECHAR
        view.getCrudButtonBar()
                .getBtFechar()
                .setDisable(
                        editando
                );


        // SALVAR
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
}