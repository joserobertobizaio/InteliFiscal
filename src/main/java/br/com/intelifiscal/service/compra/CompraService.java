package br.com.intelifiscal.service.compra;

import br.com.intelifiscal.dto.compra.CompraDTO;
import br.com.intelifiscal.dto.compra.CompraItemDTO;
import br.com.intelifiscal.repository.compra.CompraRepository;

import java.util.List;

public class CompraService {

    private final CompraRepository repository =
            new CompraRepository();


    public List<CompraDTO> listarTodas() {

        return repository.listarTodas();
    }


    public List<CompraItemDTO> listarItensPorNfe(
            Integer idNfe) {

        if (idNfe == null) {

            return List.of();
        }

        return repository.listarItensPorNfe(
                idNfe
        );
    }
}