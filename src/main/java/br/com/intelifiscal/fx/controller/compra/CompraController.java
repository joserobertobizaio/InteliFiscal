package br.com.intelifiscal.fx.controller.compra;

import br.com.intelifiscal.dto.compra.CompraDTO;
import br.com.intelifiscal.fx.view.compra.CompraView;
import br.com.intelifiscal.service.compra.CompraService;

import javafx.collections.FXCollections;

public class CompraController {

    private final CompraView view;

    private final CompraService service =
            new CompraService();


    public CompraController(
            CompraView view
    ) {

        this.view = view;

        inicializar();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarCompras();

        configurarSelecao();

        configurarPesquisa();
    }


    //==================================================
    // CARREGA COMPRAS
    //==================================================

    private void carregarCompras() {

        view.getTabelaCompras()
                .setItems(
                        FXCollections.observableArrayList(
                                service.listarTodas()
                        )
                );
    }


    //==================================================
    // SELEÇÃO DA COMPRA
    //==================================================

    private void configurarSelecao() {

        view.getTabelaCompras()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, anterior, selecionada) -> {

                            carregarItens(
                                    selecionada
                            );
                        }
                );
    }


    //==================================================
    // CARREGA ITENS
    //==================================================

    private void carregarItens(
            CompraDTO compra
    ) {

        if (compra == null) {

            view.getTabelaItens()
                    .getItems()
                    .clear();

            return;
        }

        view.getTabelaItens()
                .setItems(
                        FXCollections.observableArrayList(
                                service.listarItensPorNfe(
                                        compra.getId()
                                )
                        )
                );
    }


    //==================================================
    // PESQUISA
    //==================================================

    private void configurarPesquisa() {

        view.getTxtPesquisa()
                .textProperty()
                .addListener(
                        (obs, antigo, novo) -> {

                            pesquisar(novo);
                        }
                );
    }


    //==================================================
    // FILTRO
    //==================================================

    private void pesquisar(
            String texto
    ) {

        String filtro =
                texto == null
                        ? ""
                        : texto.trim()
                          .toLowerCase();

        if (filtro.isBlank()) {

            carregarCompras();

            return;
        }

        var resultado =
                service.listarTodas()
                        .stream()
                        .filter(
                                compra -> {

                                    String numero =
                                            compra.getNumero() == null
                                                    ? ""
                                                    : compra.getNumero();

                                    String fornecedor =
                                            compra.getEmitente() == null
                                                    ? ""
                                                    : compra.getEmitente();

                                    String cnpj =
                                            compra.getCnpjEmitente() == null
                                                    ? ""
                                                    : compra.getCnpjEmitente();

                                    return numero
                                            .toLowerCase()
                                            .contains(filtro)

                                            || fornecedor
                                            .toLowerCase()
                                            .contains(filtro)

                                            || cnpj
                                            .toLowerCase()
                                            .contains(filtro);
                                }
                        )
                        .toList();

        view.getTabelaCompras()
                .setItems(
                        FXCollections.observableArrayList(
                                resultado
                        )
                );
    }

}