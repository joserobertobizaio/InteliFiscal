package br.com.intelifiscal.fx.controller.estabelecimento;

import br.com.intelifiscal.fx.navigation.NavigationManager;
import br.com.intelifiscal.fx.navigation.ScreenType;
import br.com.intelifiscal.fx.view.estabelecimento.EstabelecimentoView;
import br.com.intelifiscal.service.MinhaEmpresaService;
import br.com.intelifiscal.entity.MinhaEmpresa;
import javafx.application.Platform;
import br.com.intelifiscal.util.Mascara;
import br.com.intelifiscal.util.Mensagem;
import java.time.LocalDateTime;

public class EstabelecimentoController {

    private final EstabelecimentoView view;
    private final MinhaEmpresaService service;
    private boolean novaEmpresa = false;

    public EstabelecimentoController(EstabelecimentoView view) {

        this.view = view;
        this.service = new MinhaEmpresaService();

        configurarEventos();

        carregarEmpresas();

        atualizarEstadoBotoes(
                true,
                false,
                false,
                true
        );

    }

    private void atualizarEstadoBotoes(
            boolean editar,
            boolean salvar,
            boolean excluir,
            boolean novaEmpresa
    ) {

        view.getCrudButtonBar()
                .getBtNovo()
                .setDisable(!editar);

        view.getCrudButtonBar()
                .getBtSalvar()
                .setDisable(!salvar);

        view.getCrudButtonBar()
                .getBtExcluir()
                .setDisable(!excluir);

        view.getBtNovaEmpresa()
                .setDisable(!novaEmpresa);

    }

    private void configurarEventos() {

        view.getCrudButtonBar()
                .getBtSalvar()
                .setOnAction(event -> salvar());

        view.getCrudButtonBar()
                .getBtNovo()
                .setOnAction(event -> {

                    novaEmpresa = false;

                    habilitarEdicao();

                });

        view.getCrudButtonBar()
                .getBtExcluir()
                .setOnAction(event -> excluir());

        view.getCrudButtonBar()
                .getBtFechar()
                .setOnAction(event -> fechar());

        view.getBtNovaEmpresa()
                .setOnAction(event -> novaEmpresa());

        view.getCbEmpresa()
                .setOnAction(event -> {

                    MinhaEmpresa empresaSelecionada =
                            view.getCbEmpresa().getValue();

                    if (empresaSelecionada != null) {

                        carregarEmpresa(empresaSelecionada.getId());

                    }

                });

    }

    private void carregarEmpresa(Long id) {

        service.buscarPorId(id).ifPresent(empresa -> {

            view.getTxtCnpj().setText(
                    Mascara.formatarCnpj(empresa.getCnpj())
            );

            view.getTxtInscricaoEstadual().setText(empresa.getInscricaoEstadual());
            view.getTxtRazaoSocial().setText(empresa.getRazaoSocial());
            view.getTxtNomeFantasia().setText(empresa.getNomeFantasia());

            view.getTxtCep().setText(empresa.getCep());
            view.getTxtUf().setText(empresa.getUf());
            view.getTxtCidade().setText(empresa.getCidade());
            view.getTxtBairro().setText(empresa.getBairro());
            view.getTxtEndereco().setText(empresa.getEndereco());
            view.getTxtNumero().setText(empresa.getNumero());

            view.getTxtTelefone().setText(empresa.getTelefone());
            view.getTxtEmail().setText(empresa.getEmail());

            bloquearEdicao();

        });

    }

    private void carregarEmpresas() {

        view.getCbEmpresa().getItems().clear();

        var empresas = service.buscarTodas();

        System.out.println("Quantidade = " + empresas.size());

        view.getCbEmpresa().getItems().addAll(empresas);

        if (!empresas.isEmpty()) {

            view.getCbEmpresa()
                    .getSelectionModel()
                    .selectFirst();

            carregarEmpresa(
                    empresas.getFirst().getId()
            );

        }

    }

    private void habilitarEdicao() {

        view.getTxtCnpj().setEditable(true);
        view.getTxtInscricaoEstadual().setEditable(true);
        view.getTxtRazaoSocial().setEditable(true);
        view.getTxtNomeFantasia().setEditable(true);

        view.getTxtCep().setEditable(true);
        view.getTxtUf().setEditable(true);
        view.getTxtCidade().setEditable(true);
        view.getTxtBairro().setEditable(true);
        view.getTxtEndereco().setEditable(true);
        view.getTxtNumero().setEditable(true);

        view.getTxtTelefone().setEditable(true);
        view.getTxtEmail().setEditable(true);

       // view.getTxtRazaoSocial().requestFocus();

        Platform.runLater(() -> {

            view.getScrollPane().setVvalue(0);

            view.getTxtCnpj().requestFocus();

        });

        atualizarEstadoBotoes(
                false,   // Editar
                true,    // Salvar
                true,    // Excluir
                false    // Nova Empresa
        );
    }

    private void bloquearEdicao() {

        view.getTxtCnpj().setEditable(false);
        view.getTxtInscricaoEstadual().setEditable(false);
        view.getTxtRazaoSocial().setEditable(false);
        view.getTxtNomeFantasia().setEditable(false);

        view.getTxtCep().setEditable(false);
        view.getTxtUf().setEditable(false);
        view.getTxtCidade().setEditable(false);
        view.getTxtBairro().setEditable(false);
        view.getTxtEndereco().setEditable(false);
        view.getTxtNumero().setEditable(false);

        view.getTxtTelefone().setEditable(false);
        view.getTxtEmail().setEditable(false);
    }

    private void salvar() {

        System.out.println("Nova empresa = " + novaEmpresa);

        System.out.println("Nova empresa = " + novaEmpresa);
        System.out.println("Empresa selecionada = "
                + view.getCbEmpresa().getValue());

        System.out.println("IE = "
                + view.getTxtInscricaoEstadual().getText());

        if (view.getTxtCnpj().getText().trim().isEmpty()) {

            Mensagem.aviso("Informe o CNPJ da empresa.");
            view.getTxtCnpj().requestFocus();
            return;

        }

        if (view.getTxtRazaoSocial().getText().trim().isEmpty()) {

            Mensagem.aviso("Informe a Razão Social da empresa.");
            view.getTxtRazaoSocial().requestFocus();
            return;

        }

        MinhaEmpresa empresa = new MinhaEmpresa();

        String cnpj = view.getTxtCnpj()
                .getText()
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();

        empresa.setCnpj(cnpj);
        empresa.setInscricaoEstadual(view.getTxtInscricaoEstadual().getText().trim());
        empresa.setRazaoSocial(view.getTxtRazaoSocial().getText().trim());
        empresa.setNomeFantasia(view.getTxtNomeFantasia().getText().trim());

        empresa.setCep(view.getTxtCep().getText().trim());
        empresa.setUf(view.getTxtUf().getText().trim());
        empresa.setCidade(view.getTxtCidade().getText().trim());
        empresa.setBairro(view.getTxtBairro().getText().trim());
        empresa.setEndereco(view.getTxtEndereco().getText().trim());
        empresa.setNumero(view.getTxtNumero().getText().trim());
        empresa.setTelefone(view.getTxtTelefone().getText().trim());
        empresa.setEmail(view.getTxtEmail().getText().trim());

        empresa.setAtivo(true);

        LocalDateTime agora = LocalDateTime.now();

        empresa.setDataCadastro(agora);
        empresa.setDataAtualizacao(agora);

        if (novaEmpresa) {

            service.salvar(empresa);

        } else {

            MinhaEmpresa selecionada =
                    view.getCbEmpresa().getValue();

            empresa.setId(selecionada.getId());

            service.atualizar(empresa);

        }

        novaEmpresa = false;

        bloquearEdicao();

        atualizarComboEmpresas();

        atualizarEstadoBotoes(
                true,   // Editar
                false,  // Salvar
                false,  // Excluir
                true    // Nova Empresa
        );

        Mensagem.sucesso("Dados da empresa salvos com sucesso.");

    }

    private void atualizarComboEmpresas() {

        view.getCbEmpresa().getItems().setAll(
                service.buscarTodas()
        );

    }

    private void fechar() {

        NavigationManager.show(ScreenType.DASHBOARD);

    }

    private void excluir() {

        if (!Mensagem.confirmar(
                "Deseja realmente excluir o cadastro da empresa?"
        )) {
            return;
        }

        MinhaEmpresa selecionada = view.getCbEmpresa().getValue();

        if (selecionada == null) {

            Mensagem.aviso("Selecione uma empresa.");
            return;

        }

        service.excluir(selecionada.getId());

        carregarEmpresas();

        bloquearEdicao();

        atualizarEstadoBotoes(
                true,   // Editar
                false,  // Salvar
                false,  // Excluir
                true    // Nova Empresa
        );

        Mensagem.sucesso("Empresa excluída com sucesso.");

    }

    private void novaEmpresa() {

        if (!Mensagem.confirmar(
                "Deseja cadastrar uma nova empresa?"
        )) {
            return;
        }

        novaEmpresa = true;

        view.getCbEmpresa().getSelectionModel().clearSelection();

        view.getTxtCnpj().clear();
        view.getTxtInscricaoEstadual().clear();
        view.getTxtRazaoSocial().clear();
        view.getTxtNomeFantasia().clear();

        view.getTxtCep().clear();
        view.getTxtUf().clear();
        view.getTxtCidade().clear();
        view.getTxtBairro().clear();
        view.getTxtEndereco().clear();
        view.getTxtNumero().clear();

        view.getTxtTelefone().clear();
        view.getTxtEmail().clear();

        habilitarEdicao();

        view.getTxtCnpj().requestFocus();

        atualizarEstadoBotoes(
                false,   // Editar
                true,    // Salvar
                false,   // Excluir
                false    // Nova Empresa
        );

    }

}