package br.com.intelifiscal.service.venda;

import br.com.intelifiscal.dto.venda.VendaDTO;
import br.com.intelifiscal.dto.venda.VendaItemDTO;
import br.com.intelifiscal.repository.venda.VendaRepository;
import java.util.List;

public class VendaService {

    private final VendaRepository repository =
            new VendaRepository();

    public List<VendaDTO> listarTodas() {

        return repository.listarTodas();
    }

    public List<VendaItemDTO> listarItensPorNfe(
            Integer idNfe) {

        if (idNfe == null) {
            return List.of();
        }

        return repository.listarItensPorNfe(
                idNfe
        );
    }

}