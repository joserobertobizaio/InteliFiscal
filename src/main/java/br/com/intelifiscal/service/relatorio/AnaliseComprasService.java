package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.AnaliseComprasDTO;
import br.com.intelifiscal.repository.relatorio.AnaliseComprasRepository;

import java.time.LocalDate;
import java.util.List;

public class AnaliseComprasService {

    private final AnaliseComprasRepository repository =
            new AnaliseComprasRepository();


    //==================================================
    // CONSULTAR ANÁLISE DE COMPRAS
    //==================================================

    public List<AnaliseComprasDTO> consultar(
            LocalDate dataInicio,
            LocalDate dataFim,
            String cnpjFornecedor,
            String codigoProduto
    ) {

        return repository.consultar(
                dataInicio,
                dataFim,
                cnpjFornecedor,
                codigoProduto
        );
    }
}