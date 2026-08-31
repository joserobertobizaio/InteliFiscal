package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.DetalhamentoCompraDTO;
import br.com.intelifiscal.repository.relatorio.DetalhamentoCompraRepository;

import java.time.LocalDate;
import java.util.List;

public class DetalhamentoCompraService {

    private final DetalhamentoCompraRepository repository;

    public DetalhamentoCompraService() {
        this.repository =
                new DetalhamentoCompraRepository();
    }

    public List<DetalhamentoCompraDTO> listarPorPeriodo(
            LocalDate inicio,
            LocalDate fim) {

        if (inicio == null) {
            throw new IllegalArgumentException(
                    "A data inicial deve ser informada."
            );
        }

        if (fim == null) {
            throw new IllegalArgumentException(
                    "A data final deve ser informada."
            );
        }

        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException(
                    "A data inicial não pode ser maior que a data final."
            );
        }

        return repository.listar(
                inicio,
                fim
        );
    }
}