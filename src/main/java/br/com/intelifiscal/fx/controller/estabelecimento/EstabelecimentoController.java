package br.com.intelifiscal.fx.controller.estabelecimento;

import br.com.intelifiscal.fx.view.estabelecimento.EstabelecimentoView;
import br.com.intelifiscal.service.MinhaEmpresaService;
import br.com.intelifiscal.entity.MinhaEmpresa;
import br.com.intelifiscal.util.Mascara;
import br.com.intelifiscal.util.Mensagem;
import java.time.LocalDateTime;

public class EstabelecimentoController {

    private final EstabelecimentoView view;
    private final MinhaEmpresaService service;

    public EstabelecimentoController(EstabelecimentoView view) {

        this.view = view;
        this.service = new MinhaEmpresaService();

        configurarEventos();
        carregarEmpresa();

    }

    private void configurarEventos() {

        view.getCrudButtonBar()
                .getBtSalvar()
                .setOnAction(event -> salvar());

        view.getCrudButtonBar()
                .getBtNovo()
                .setOnAction(event -> habilitarEdicao());

    }

    private void carregarEmpresa() {

        service.buscarEmpresa().ifPresent(empresa -> {

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

        view.getTxtRazaoSocial().requestFocus();
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

        service.salvar(empresa);

        bloquearEdicao();

        Mensagem.sucesso("Dados da empresa salvos com sucesso.");

    }

}