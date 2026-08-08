package br.com.intelifiscal.service.nfeitem;

import br.com.intelifiscal.dto.nfeitem.NFeItemDTO;
import br.com.intelifiscal.repository.nfeitem.NFeItemRepository;

public class NFeItemService {

    private final NFeItemRepository repository =
            new NFeItemRepository();

    public void salvar(NFeItemDTO item) {

        repository.salvar(item);

    }

}