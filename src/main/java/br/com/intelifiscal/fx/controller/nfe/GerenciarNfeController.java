package br.com.intelifiscal.fx.controller.nfe;

import br.com.intelifiscal.entity.NFe;
import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.nfe.GerenciarNfeView;
import br.com.intelifiscal.service.NFeService;
import br.com.intelifiscal.util.Mensagem;

import javafx.collections.FXCollections;

import java.time.format.DateTimeFormatter;

public class GerenciarNfeController {

    private final GerenciarNfeView view;

    private final NFeService service =
            new NFeService();

    private static final DateTimeFormatter FORMATTER_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    //==================================================
    // CONSTRUTOR
    //==================================================

    public GerenciarNfeController(
            GerenciarNfeView view) {

        this.view = view;

        inicializar();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarNfe();

        configurarSelecao();

        configurarPesquisa();

        configurarBotoes();

        configurarFechar();

        limparDetalhes();

        bloquearEdicao();

        atualizarEstadoBotoes(false);
    }


    //==================================================
    // CARREGAR NF-e
    //==================================================

    private void carregarNfe() {

        view.getTabelaNfe().setItems(
                FXCollections.observableArrayList(
                        service.pesquisar("")
                )
        );
    }


    //==================================================
    // SELEÇÃO
    //==================================================

    private void configurarSelecao() {

        view.getTabelaNfe()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, anterior, selecionada) -> {

                            if (selecionada == null) {

                                limparDetalhes();

                                bloquearEdicao();

                                atualizarEstadoBotoes(false);

                                return;
                            }

                            preencherDetalhes(selecionada);

                            bloquearEdicao();

                            atualizarEstadoBotoes(true);
                        }
                );
    }


    //==================================================
    // PREENCHER DETALHES
    //==================================================

    private void preencherDetalhes(NFe nfe) {

        view.getTxtNumero().setText(
                valorTexto(nfe.getNumero())
        );

        view.getTxtModelo().setText(
                valorTexto(nfe.getModelo())
        );

        view.getCbTipo().setValue(
                valorTexto(nfe.getTipo())
        );

        view.getTxtDataEmissao().setText(
                nfe.getDataEmissao() == null
                        ? ""
                        : nfe.getDataEmissao()
                          .format(FORMATTER_DATA)
        );

        view.getTxtEmitente().setText(
                valorTexto(nfe.getEmitente())
        );

        view.getTxtCnpjEmitente().setText(
                valorTexto(nfe.getCnpjEmitente())
        );

        view.getTxtMunicipioEmitente().setText(
                valorTexto(nfe.getMunicipioEmitente())
        );

        view.getTxtUfEmitente().setText(
                valorTexto(nfe.getUfEmitente())
        );

        view.getTxtDestinatario().setText(
                valorTexto(nfe.getDestinatario())
        );

        view.getTxtCnpjDestinatario().setText(
                valorTexto(nfe.getCnpjDestinatario())
        );

        view.getTxtMunicipioDestinatario().setText(
                valorTexto(nfe.getMunicipioDestinatario())
        );

        view.getTxtUfDestinatario().setText(
                valorTexto(nfe.getUfDestinatario())
        );

        view.getTxtValorTotal().setText(
                nfe.getValorTotal() == null
                        ? ""
                        : nfe.getValorTotal().toString()
        );

        view.getTxtSituacao().setText(
                valorTexto(nfe.getSituacao())
        );

        view.getTxtChave().setText(
                valorTexto(nfe.getChave())
        );
    }


    //==================================================
    // LIMPAR DETALHES
    //==================================================

    private void limparDetalhes() {

        view.getTxtNumero().clear();

        view.getTxtModelo().clear();

        view.getCbTipo().setValue(null);

        view.getTxtDataEmissao().clear();

        view.getTxtEmitente().clear();

        view.getTxtCnpjEmitente().clear();

        view.getTxtMunicipioEmitente().clear();

        view.getTxtUfEmitente().clear();

        view.getTxtDestinatario().clear();

        view.getTxtCnpjDestinatario().clear();

        view.getTxtMunicipioDestinatario().clear();

        view.getTxtUfDestinatario().clear();

        view.getTxtValorTotal().clear();

        view.getTxtSituacao().clear();

        view.getTxtChave().clear();
    }


    //==================================================
    // PESQUISA
    //==================================================

    private void configurarPesquisa() {

        view.getTxtPesquisa()
                .textProperty()
                .addListener(
                        (obs, antigo, novo) ->
                                pesquisar(novo)
                );
    }


    private void pesquisar(String texto) {

        String filtro =
                texto == null
                        ? ""
                        : texto.trim();

        view.getTabelaNfe().setItems(
                FXCollections.observableArrayList(
                        service.pesquisar(filtro)
                )
        );

        limparDetalhes();

        bloquearEdicao();

        atualizarEstadoBotoes(false);
    }


    //==================================================
    // BOTÕES
    //==================================================

    private void configurarBotoes() {

        view.getCrudButtonBar()
                .getBtNovo()
                .setOnAction(
                        event ->
                                editar()
                );

        view.getCrudButtonBar()
                .getBtSalvar()
                .setOnAction(
                        event ->
                                salvar()
                );

        view.getCrudButtonBar()
                .getBtExcluir()
                .setOnAction(
                        event ->
                                excluir()
                );
    }


    //==================================================
    // EDITAR
    //==================================================

    private void editar() {

        NFe selecionada =
                view.getTabelaNfe()
                        .getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            Mensagem.aviso(
                    "Selecione uma NF-e."
            );

            return;
        }

        if ("CANCELADA".equalsIgnoreCase(
                selecionada.getSituacao())) {

            Mensagem.aviso(
                    "NF-e cancelada não pode ter sua classificação alterada."
            );

            return;
        }

        view.getCbTipo().setDisable(false);

        view.getCbTipo().requestFocus();

        view.getCrudButtonBar()
                .getBtNovo()
                .setDisable(true);

        view.getCrudButtonBar()
                .getBtSalvar()
                .setDisable(false);

        view.getCrudButtonBar()
                .getBtExcluir()
                .setDisable(true);
    }


    //==================================================
    // SALVAR
    //==================================================

    private void salvar() {

        NFe selecionada =
                view.getTabelaNfe()
                        .getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            Mensagem.aviso(
                    "Selecione uma NF-e."
            );

            return;
        }

        if ("CANCELADA".equalsIgnoreCase(
                selecionada.getSituacao())) {

            Mensagem.aviso(
                    "NF-e cancelada não pode ter sua classificação alterada."
            );

            bloquearEdicao();

            atualizarEstadoBotoes(true);

            return;
        }

        String tipo =
                view.getCbTipo()
                        .getValue();

        if (tipo.isBlank()) {

            Mensagem.aviso(
                    "Informe o tipo da NF-e."
            );

            view.getCbTipo().requestFocus();

            return;
        }

        try {

            service.atualizarTipo(
                    selecionada.getId(),
                    tipo
            );

            selecionada.setTipo(tipo);

            view.getTabelaNfe()
                    .refresh();

            preencherDetalhes(selecionada);

            bloquearEdicao();

            atualizarEstadoBotoes(true);

            Mensagem.sucesso(
                    "Classificação da NF-e atualizada com sucesso."
            );

        } catch (RuntimeException e) {

            Mensagem.erro(
                    "Não foi possível atualizar a classificação da NF-e."
            );
        }
    }


    //==================================================
    // EXCLUIR
    //==================================================

    private void excluir() {

        NFe selecionada =
                view.getTabelaNfe()
                        .getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            Mensagem.aviso(
                    "Selecione uma NF-e."
            );

            return;
        }

        String numero =
                valorTexto(selecionada.getNumero());

        String situacao =
                valorTexto(selecionada.getSituacao());

        boolean confirmar =
                Mensagem.confirmar(
                        "Deseja realmente excluir a NF-e nº "
                                + numero
                                + "?\n\n"
                                + "Esta operação é permanente e removerá "
                                + "a NF-e e seus dados relacionados.\n\n"
                                + "Situação atual: "
                                + situacao
                                + "."
                );

        if (!confirmar) {

            return;
        }

        try {

            service.excluir(
                    selecionada.getId()
            );

            String filtro =
                    view.getTxtPesquisa()
                            .getText();

            pesquisar(filtro);

            Mensagem.sucesso(
                    "NF-e nº "
                            + numero
                            + " excluída com sucesso."
            );

        } catch (RuntimeException e) {

            Mensagem.erro(
                    "Não foi possível excluir a NF-e nº "
                            + numero
                            + "."
            );
        }
    }


    //==================================================
    // FECHAR
    //==================================================

    private void configurarFechar() {

        view.getCrudButtonBar()
                .getBtFechar()
                .setOnAction(
                        event ->
                                fechar()
                );
    }


    private void fechar() {

        NavigationManager.show(
                ScreenType.DASHBOARD
        );
    }


    //==================================================
    // BLOQUEAR EDIÇÃO
    //==================================================

    private void bloquearEdicao() {

        view.getTxtNumero().setEditable(false);

        view.getTxtModelo().setEditable(false);

        view.getCbTipo().setDisable(true);

        view.getTxtDataEmissao().setEditable(false);

        view.getTxtEmitente().setEditable(false);

        view.getTxtCnpjEmitente().setEditable(false);

        view.getTxtMunicipioEmitente().setEditable(false);

        view.getTxtUfEmitente().setEditable(false);

        view.getTxtDestinatario().setEditable(false);

        view.getTxtCnpjDestinatario().setEditable(false);

        view.getTxtMunicipioDestinatario().setEditable(false);

        view.getTxtUfDestinatario().setEditable(false);

        view.getTxtValorTotal().setEditable(false);

        view.getTxtSituacao().setEditable(false);

        view.getTxtChave().setEditable(false);
    }


    //==================================================
    // ESTADO DOS BOTÕES
    //==================================================

    private void atualizarEstadoBotoes(
            boolean existeSelecionada) {

        view.getCrudButtonBar()
                .getBtNovo()
                .setDisable(!existeSelecionada);

        view.getCrudButtonBar()
                .getBtExcluir()
                .setDisable(!existeSelecionada);

        view.getCrudButtonBar()
                .getBtSalvar()
                .setDisable(true);

        view.getCrudButtonBar()
                .getBtFechar()
                .setDisable(false);
    }


    //==================================================
    // AUXILIAR
    //==================================================

    private String valorTexto(String valor) {

        return valor == null
                ? ""
                : valor;
    }

}