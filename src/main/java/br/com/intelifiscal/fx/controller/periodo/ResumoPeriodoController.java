package br.com.intelifiscal.fx.controller.periodo;

import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import br.com.intelifiscal.fx.view.periodo.ResumoPeriodoView;
import br.com.intelifiscal.service.periodo.ResumoPeriodoService;

import java.util.List;

public class ResumoPeriodoController {

    private final ResumoPeriodoView view;

    private final ResumoPeriodoService service =
            new ResumoPeriodoService();


    //==================================================
    // CONSTRUTOR
    //==================================================

    public ResumoPeriodoController(
            ResumoPeriodoView view
    ) {

        this.view = view;

        carregarDados();
    }


    //==================================================
    // CARREGAR DADOS
    //==================================================

    private void carregarDados() {

        List<ResumoPeriodoDTO> lista =
                service.consultarUltimos12Meses();

        List<ResumoMensalDTO> listaMensal =
                service.consultarResumoMensal();

        view.atualizar(lista);

        view.atualizarGrafico(listaMensal);
    }

}