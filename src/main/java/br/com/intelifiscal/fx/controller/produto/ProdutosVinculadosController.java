package br.com.intelifiscal.fx.controller.produto;

import br.com.intelifiscal.dto.produto.ProdutoVinculoDTO;
import br.com.intelifiscal.fx.view.produto.ProdutosVinculadosView;
import br.com.intelifiscal.service.produto.ProdutoVinculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class ProdutosVinculadosController {

    private final ProdutosVinculadosView view;
    private final ProdutoVinculoService service =
            new ProdutoVinculoService();

    private final ObservableList<ProdutoVinculoDTO> todosOsVinculos =
            FXCollections.observableArrayList();


    // ============================================================
    // CONSTRUTOR
    // ============================================================

    public ProdutosVinculadosController(
            ProdutosVinculadosView view) {

        this.view = view;

        configurarEventos();

        carregarVinculos();
    }


    // ============================================================
    // EVENTOS
    // ============================================================

    private void configurarEventos() {

        view.getTxtPesquisa().textProperty().addListener(
                (observable, antigo, novo) -> pesquisar()
        );

        view.getBtLimpar().setOnAction(
                event -> limparPesquisa()
        );

        view.getBtDesvincular().setOnAction(
                event -> desvincular()
        );

        view.getMiDesvincular().setOnAction(
                event -> desvincular()
        );

        view.getMenuDetalharVinculo().setOnAction(
                event -> detalharVinculo()
        );

        view.getBtFechar().setOnAction(
                event -> fechar()
        );

    }

    // ============================================================
    // CARREGAR VÍNCULOS
    // ============================================================

    private void carregarVinculos() {

        try {

            List<ProdutoVinculoDTO> lista =
                    service.listarTodos();

            todosOsVinculos.setAll(lista);

            view.getDados().setAll(lista);

            atualizarContador();

        } catch (Exception e) {

            mostrarErro(
                    "Erro ao carregar os produtos vinculados.",
                    e
            );
        }
    }


    // ============================================================
    // PESQUISA
    // ============================================================

    private void pesquisar() {

        String texto =
                view.getTxtPesquisa()
                        .getText();

        if (texto == null
                || texto.isBlank()) {

            view.getDados().setAll(
                    todosOsVinculos
            );

            atualizarContador();

            return;
        }

        texto = texto
                .trim()
                .toLowerCase();


        ObservableList<ProdutoVinculoDTO> resultado =
                FXCollections.observableArrayList();


        for (ProdutoVinculoDTO vinculo :
                todosOsVinculos) {

            if (contem(
                    vinculo.getCodigoCompra(),
                    texto
            )
                    || contem(
                    vinculo.getDescricaoCompra(),
                    texto
            )
                    || contem(
                    vinculo.getCodigoVenda(),
                    texto
            )
                    || contem(
                    vinculo.getDescricaoVenda(),
                    texto
            )) {

                resultado.add(vinculo);
            }
        }


        view.getDados().setAll(
                resultado
        );

        atualizarContador();
    }


    // ============================================================
    // VERIFICA TEXTO
    // ============================================================

    private boolean contem(
            String valor,
            String pesquisa) {

        return valor != null
                && valor.toLowerCase()
                .contains(pesquisa);
    }


    // ============================================================
    // LIMPAR PESQUISA
    // ============================================================

    private void limparPesquisa() {

        view.getTxtPesquisa()
                .clear();

        view.getDados().setAll(
                todosOsVinculos
        );

        atualizarContador();
    }

    // ============================================================
    // DETALHAR VÍNCULO
    // ============================================================

    private void detalharVinculo() {

        ProdutoVinculoDTO vinculo =
                view.getTabela()
                        .getSelectionModel()
                        .getSelectedItem();

        if (vinculo == null) {

            mostrarAviso(
                    "Selecione um vínculo na tabela para detalhar."
            );

            return;
        }


        // --------------------------------------------------------
        // CRIA A TELA DE COMPARAÇÃO
        // --------------------------------------------------------

        br.com.intelifiscal.fx.view.produto.CompararCompraVendaView compararView =
                new br.com.intelifiscal.fx.view.produto.CompararCompraVendaView();


        // --------------------------------------------------------
        // CRIA A JANELA
        // --------------------------------------------------------

        Stage janela =
                new Stage();

        janela.setTitle(
                "InteliFiscal - Comparar Compra × Venda"
        );

        janela.initOwner(
                view.getScene().getWindow()
        );


        // --------------------------------------------------------
        // CONFIGURA O CONTROLLER
        // --------------------------------------------------------

        new CompararCompraVendaController(
                compararView
        );


        // --------------------------------------------------------
        // PREENCHE OS CÓDIGOS DO VÍNCULO
        // --------------------------------------------------------

        compararView.getTxtCodigoCompra().setText(
                vinculo.getCodigoCompra()
        );

        compararView.getTxtCodigoVenda().setText(
                vinculo.getCodigoVenda()
        );


        // --------------------------------------------------------
        // EXECUTA A PESQUISA AUTOMATICAMENTE
        // --------------------------------------------------------

        compararView.getBtPesquisar().fire();


        // --------------------------------------------------------
        // FECHAR A JANELA POP-UP
        // --------------------------------------------------------

        compararView.getBtFechar().setOnAction(
                event -> janela.close()
        );


        // --------------------------------------------------------
        // MOSTRA A JANELA
        // --------------------------------------------------------

        janela.setScene(
                new Scene(
                        compararView,
                        1100,
                        700
                )
        );

        janela.setMinWidth(950);
        janela.setMinHeight(600);

        janela.show();
    }


    // ============================================================
    // DESVINCULAR
    // ============================================================

    private void desvincular() {

        ProdutoVinculoDTO vinculo =
                view.getTabela()
                        .getSelectionModel()
                        .getSelectedItem();


        if (vinculo == null) {

            mostrarAviso(
                    "Selecione um vínculo na tabela para desvincular."
            );

            return;
        }


        Alert confirmacao =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacao.setTitle(
                "Desvincular produtos"
        );

        confirmacao.setHeaderText(
                "Deseja realmente desvincular estes produtos?"
        );

        confirmacao.setContentText(
                "Compra: "
                        + vinculo.getCodigoCompra()
                        + " - "
                        + vinculo.getDescricaoCompra()
                        + "\n\n"
                        + "Venda: "
                        + vinculo.getCodigoVenda()
                        + " - "
                        + vinculo.getDescricaoVenda()
        );


        confirmacao.showAndWait()
                .ifPresent(resposta -> {

                    if (resposta == ButtonType.OK) {

                        executarDesvinculamento(
                                vinculo
                        );
                    }
                });
    }


    // ============================================================
    // EXECUTA DESVINCULAMENTO
    // ============================================================

    private void executarDesvinculamento(
            ProdutoVinculoDTO vinculo) {

        try {

            service.desvincular(
                    vinculo.getCodigoCompra(),
                    vinculo.getCodigoVenda()
            );


            todosOsVinculos.remove(
                    vinculo
            );


            view.getDados().remove(
                    vinculo
            );


            atualizarContador();


            mostrarInformacao(
                    "Os produtos foram desvinculados com sucesso."
            );


        } catch (Exception e) {

            mostrarErro(
                    "Erro ao desvincular os produtos.",
                    e
            );
        }
    }


    // ============================================================
    // CONTADOR
    // ============================================================

    private void atualizarContador() {

        int quantidade =
                view.getDados().size();

        view.getLblContador().setText(
                quantidade
                        + (
                        quantidade == 1
                                ? " vínculo encontrado"
                                : " vínculos encontrados"
                )
        );
    }


    // ============================================================
    // FECHAR
    // ============================================================

    private void fechar() {

        view.getScene()
                .getWindow()
                .hide();
    }


    // ============================================================
    // ALERTAS
    // ============================================================

    private void mostrarAviso(
            String mensagem) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(
                "Atenção"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                mensagem
        );

        alert.showAndWait();
    }


    private void mostrarInformacao(
            String mensagem) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Produtos vinculados"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                mensagem
        );

        alert.showAndWait();
    }


    private void mostrarErro(
            String mensagem,
            Exception e) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Erro"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                mensagem
                        + "\n\n"
                        + (
                        e.getMessage() == null
                                ? ""
                                : e.getMessage()
                )
        );

        alert.showAndWait();
    }
}