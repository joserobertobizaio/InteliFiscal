package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.repository.relatorio.ResumoVendasRepository;

import java.time.LocalDate;
import java.util.List;

public class TopClientesService {

    private final ResumoVendasRepository repository;

    public TopClientesService() {
        this.repository = new ResumoVendasRepository();
    }

    public List<ClienteVendaDTO> listarTopClientes(int limite) {

        if (limite != 5 &&
                limite != 10 &&
                limite != 20 &&
                limite != 50) {

            throw new IllegalArgumentException(
                    "O limite deve ser 5, 10, 20 ou 50."
            );
        }

        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusMonths(12);

        List<ClienteVendaDTO> lista =
                repository.consultarPorCliente(
                        dataInicio,
                        dataFim
                );

        if (lista.size() > limite) {
            return lista.subList(0, limite);
        }

        return lista;
    }
}