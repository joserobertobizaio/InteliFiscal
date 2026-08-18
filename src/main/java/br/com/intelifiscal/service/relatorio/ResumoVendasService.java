package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.repository.relatorio.ResumoVendasRepository;

import java.time.LocalDate;
import java.util.List;

public class ResumoVendasService {

    private final ResumoVendasRepository repository =
            new ResumoVendasRepository();


    //==================================================
    // RESUMO GERAL
    //==================================================

    public ResumoVendasDTO consultarResumo() {

        return repository.consultarResumo();
    }


    //==================================================
    // RESUMO POR PERÍODO
    //==================================================

    public ResumoVendasDTO consultarResumo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        return repository.consultarResumo(
                dataInicio,
                dataFim
        );
    }


    //==================================================
    // VENDAS POR CLIENTE
    //==================================================

    public List<ClienteVendaDTO> consultarPorCliente() {

        return repository.consultarPorCliente();
    }


    //==================================================
    // VENDAS POR CLIENTE E PERÍODO
    //==================================================

    public List<ClienteVendaDTO> consultarPorCliente(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        return repository.consultarPorCliente(
                dataInicio,
                dataFim
        );
    }
}