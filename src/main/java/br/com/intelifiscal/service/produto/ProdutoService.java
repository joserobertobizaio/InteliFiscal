package br.com.intelifiscal.service.produto;

import br.com.intelifiscal.dto.produto.ProdutoDTO;
import br.com.intelifiscal.repository.produto.ProdutoVinculoRepository;
import br.com.intelifiscal.dto.produto.ProdutoHistoricoDTO;
import br.com.intelifiscal.repository.nfeitem.NFeItemRepository;
import br.com.intelifiscal.repository.produto.ProdutoRepository;
import java.time.LocalDate;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ProdutoService {

    private final ProdutoRepository repository =
            new ProdutoRepository();

    private final NFeItemRepository nfeItemRepository =
            new NFeItemRepository();

    private final ProdutoVinculoRepository vinculoRepository =
            new ProdutoVinculoRepository();

    public void salvar(ProdutoDTO produto) {

        validar(produto);

        repository.salvar(produto);
    }


    public void atualizar(ProdutoDTO produto) {

        validar(produto);

        if (produto.getId() <= 0) {

            throw new IllegalArgumentException(
                    "Produto inválido para atualização."
            );
        }

        repository.atualizar(produto);
    }


    public void excluir(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "Produto inválido para exclusão."
            );
        }

        repository.excluir(id);
    }


    public ProdutoDTO buscarPorCodigoProduto(
            String codigoProduto) {

        if (codigoProduto == null
                || codigoProduto.isBlank()) {

            return null;
        }

        return repository.buscarPorCodigoProduto(
                codigoProduto
        );
    }


    public ProdutoDTO buscarPorCodigoBarras(
            String codigoBarras) {

        if (codigoBarras == null
                || codigoBarras.isBlank()) {

            return null;
        }

        return repository.buscarPorCodigoBarras(
                codigoBarras
        );
    }


    public List<ProdutoDTO> listarTodos() {

        return repository.listarTodos();
    }


    public void salvarSeNaoExistir(
            ProdutoDTO produto) {

        if (produto == null) {
            return;
        }

        if (produto.getCodigoProduto() == null
                || produto.getCodigoProduto().isBlank()) {

            return;
        }

        ProdutoDTO existente =
                repository.buscarPorCodigoProduto(
                        produto.getCodigoProduto()
                );

        if (existente != null) {
            return;
        }

        repository.salvar(produto);
    }


    private void validar(ProdutoDTO produto) {

        if (produto == null) {

            throw new IllegalArgumentException(
                    "Produto não informado."
            );
        }

        if (produto.getCodigoProduto() == null
                || produto.getCodigoProduto().isBlank()) {

            throw new IllegalArgumentException(
                    "Código do produto é obrigatório."
            );
        }

        if (produto.getDescricao() == null
                || produto.getDescricao().isBlank()) {

            throw new IllegalArgumentException(
                    "Descrição do produto é obrigatória."
            );
        }
    }

    public List<ProdutoHistoricoDTO> listarHistoricoPorCodigoProduto(
            String codigoProduto,
            LocalDate inicio,
            LocalDate fim) {

        if (codigoProduto == null
                || codigoProduto.isBlank()) {

            return List.of();
        }

        if (inicio == null || fim == null) {

            return List.of();
        }

        if (inicio.isAfter(fim)) {

            throw new IllegalArgumentException(
                    "Período inválido."
            );
        }

        return nfeItemRepository.listarHistoricoPorCodigoProduto(
                codigoProduto,
                inicio,
                fim
        );
    }

    public List<ProdutoHistoricoDTO> listarHistoricoPorCodigoProduto(
            String codigoProduto) {

        if (codigoProduto == null
                || codigoProduto.isBlank()) {

            return List.of();
        }

        return nfeItemRepository.listarHistoricoPorCodigoProduto(
                codigoProduto
        );
    }

    //==================================================
    // VINCULAR PRODUTO DE COMPRA COM PRODUTO DE VENDA
    //==================================================

    public void vincularProdutos(
            String codigoCompra,
            String codigoVenda) {

        if (codigoCompra == null
                || codigoCompra.isBlank()
                || codigoVenda == null
                || codigoVenda.isBlank()) {

            throw new IllegalArgumentException(
                    "Os códigos dos produtos são obrigatórios."
            );
        }

        vinculoRepository.vincular(
                codigoCompra.trim(),
                codigoVenda.trim()
        );
    }


    //==================================================
    // VERIFICA SE DOIS PRODUTOS ESTÃO VINCULADOS
    //==================================================

    public boolean existeVinculo(
            String codigoCompra,
            String codigoVenda) {

        if (codigoCompra == null
                || codigoCompra.isBlank()
                || codigoVenda == null
                || codigoVenda.isBlank()) {

            return false;
        }

        return vinculoRepository.existeVinculo(
                codigoCompra.trim(),
                codigoVenda.trim()
        );
    }

}