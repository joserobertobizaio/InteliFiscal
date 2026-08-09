package br.com.intelifiscal.fx.controller.venda;

import br.com.intelifiscal.dto.venda.VendaDTO;
import br.com.intelifiscal.fx.view.venda.VendaView;
import br.com.intelifiscal.service.venda.VendaService;

import javafx.collections.FXCollections;

public class VendaController {

    private final VendaView view;

    private final VendaService service =
            new VendaService();


    public VendaController(
            VendaView view
    ) {

        this.view = view;

        inicializar();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarVendas();

        configurarSelecao();

        configurarPesquisa();
    }


    //==================================================
    // CARREGA VENDAS
    //==================================================

    private void carregarVendas() {

        view.getTabelaVendas()
                .setItems(
                        FXCollections.observableArrayList(
                                service.listarTodas()
                        )
                );
    }


    //==================================================
    // SELEÇÃO DA VENDA
    //==================================================

    private void configurarSelecao() {

        view.getTabelaVendas()
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
            VendaDTO venda
    ) {

        if (venda == null) {

            view.getTabelaItens()
                    .getItems()
                    .clear();

            return;
        }

        view.getTabelaItens()
                .setItems(
                        FXCollections.observableArrayList(
                                service.listarItensPorNfe(
                                        venda.getId()
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

            carregarVendas();

            return;
        }

        var resultado =
                service.listarTodas()
                        .stream()
                        .filter(
                                venda -> {

                                    String numero =
                                            venda.getNumero() == null
                                                    ? ""
                                                    : venda.getNumero();

                                    String cliente =
                                            venda.getDestinatario() == null
                                                    ? ""
                                                    : venda.getDestinatario();

                                    String cnpj =
                                            venda.getCnpjDestinatario() == null
                                                    ? ""
                                                    : venda.getCnpjDestinatario();

                                    return numero
                                            .toLowerCase()
                                            .contains(filtro)

                                            || cliente
                                            .toLowerCase()
                                            .contains(filtro)

                                            || cnpj
                                            .toLowerCase()
                                            .contains(filtro);
                                }
                        )
                        .toList();

        view.getTabelaVendas()
                .setItems(
                        FXCollections.observableArrayList(
                                resultado
                        )
                );
    }
}