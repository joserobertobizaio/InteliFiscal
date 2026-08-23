package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.AnaliseVendasDTO;
import br.com.intelifiscal.repository.relatorio.AnaliseVendasRepository;

import java.time.LocalDate;
import java.util.List;

public class AnaliseVendasService {

    private final AnaliseVendasRepository repository =
            new AnaliseVendasRepository();


    //==================================================
    // CONSULTAR ANÁLISE DE VENDAS
    //==================================================

    public List<AnaliseVendasDTO> consultar(
            LocalDate dataInicio,
            LocalDate dataFim,
            String cnpjCliente,
            String codigoProduto
    ) {

        return repository.consultar(
                dataInicio,
                dataFim,
                cnpjCliente,
                codigoProduto
        );
    }
}