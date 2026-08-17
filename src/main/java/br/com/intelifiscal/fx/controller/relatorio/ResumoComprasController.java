package br.com.intelifiscal.fx.controller.relatorio;

import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;
import br.com.intelifiscal.fx.view.relatorio.ResumoComprasView;
import br.com.intelifiscal.service.relatorio.ResumoComprasService;

import java.util.List;

public class ResumoComprasController {

    private final ResumoComprasView view;
    private final ResumoComprasService service;

    public ResumoComprasController(
            ResumoComprasView view
    ) {

        this.view = view;

        this.service =
                new ResumoComprasService();

        inicializar();

        configurarEventos();
    }


    //==================================================
    // INICIALIZAÇÃO
    //==================================================

    private void inicializar() {

        carregarResumo();

        carregarFornecedores();
    }


    //==================================================
    // CARREGAR RESUMO
    //==================================================

    private void carregarResumo() {

        ResumoComprasDTO dto =
                service.consultarResumo();

        view.atualizarResumo(dto);
    }


    //==================================================
    // CARREGAR FORNECEDORES
    //==================================================

    private void carregarFornecedores() {

        List<FornecedorCompraDTO> lista =
                service.consultarPorFornecedor();

        view.atualizarFornecedores(lista);
    }


    //==================================================
    // EVENTOS
    //==================================================

    private void configurarEventos() {

        view.getBtFechar().setOnAction(
                e -> fechar()
        );
    }


    //==================================================
    // FECHAR
    //==================================================

    private void fechar() {

        view.setVisible(false);
    }
}