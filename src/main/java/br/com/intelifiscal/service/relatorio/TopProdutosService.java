package br.com.intelifiscal.service.relatorio;

import br.com.intelifiscal.dto.relatorio.TopProdutosDTO;
import br.com.intelifiscal.repository.relatorio.TopProdutosRepository;

import java.util.List;

public class TopProdutosService {

    private final TopProdutosRepository repository;

    public TopProdutosService() {
        this.repository = new TopProdutosRepository();
    }

    /**
     * Retorna os produtos mais vendidos nos últimos 12 meses.
     *
     * @param limite quantidade de produtos desejada
     *               (5, 10, 20 ou 50)
     * @return lista dos produtos mais vendidos
     */
    public List<TopProdutosDTO> listarTopProdutos(int limite) {

        if (limite != 5 &&
                limite != 10 &&
                limite != 20 &&
                limite != 50) {

            throw new IllegalArgumentException(
                    "O limite deve ser 5, 10, 20 ou 50."
            );
        }

        return repository.listarTopProdutos(limite);
    }
}