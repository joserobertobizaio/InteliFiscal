package br.com.intelifiscal.service.periodo;

import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import br.com.intelifiscal.repository.periodo.ResumoPeriodoRepository;
import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;
import java.util.List;

public class ResumoPeriodoService {

    private final ResumoPeriodoRepository repository =
            new ResumoPeriodoRepository();


    //==================================================
    // ÚLTIMOS 12 MESES
    //==================================================

    public List<ResumoPeriodoDTO> consultarUltimos12Meses() {

        return repository.consultarUltimos12Meses();
    }

    //==================================================
    // RESUMO MENSAL - ÚLTIMOS 12 MESES
    //==================================================

    public List<ResumoMensalDTO> consultarResumoMensal() {

        return repository.consultarResumoMensal();
    }
}