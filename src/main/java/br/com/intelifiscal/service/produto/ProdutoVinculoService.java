package br.com.intelifiscal.service.produto;

import br.com.intelifiscal.repository.produto.ProdutoVinculoRepository;

public class ProdutoVinculoService {

    private final ProdutoVinculoRepository repository =
            new ProdutoVinculoRepository();


    // ============================================================
    // VINCULAR PRODUTOS
    // ============================================================

    public void vincular(
            String codigoCompra,
            String codigoVenda) {

        if (codigoCompra == null
                || codigoCompra.isBlank()) {

            throw new IllegalArgumentException(
                    "Código do produto de compra não informado."
            );
        }

        if (codigoVenda == null
                || codigoVenda.isBlank()) {

            throw new IllegalArgumentException(
                    "Código do produto de venda não informado."
            );
        }

        if (codigoCompra.equals(codigoVenda)) {

            throw new IllegalArgumentException(
                    "O produto de compra e o produto de venda não podem ser iguais."
            );
        }

        repository.vincular(
                codigoCompra.trim(),
                codigoVenda.trim()
        );
    }


    // ============================================================
    // VERIFICA VÍNCULO
    // ============================================================

    public boolean existeVinculo(
            String codigoCompra,
            String codigoVenda) {

        if (codigoCompra == null
                || codigoCompra.isBlank()) {

            return false;
        }

        if (codigoVenda == null
                || codigoVenda.isBlank()) {

            return false;
        }

        return repository.existeVinculo(
                codigoCompra.trim(),
                codigoVenda.trim()
        );
    }


    // ============================================================
    // DESVINCULAR PRODUTOS
    // ============================================================

    public void desvincular(
            String codigoCompra,
            String codigoVenda) {

        if (codigoCompra == null
                || codigoCompra.isBlank()) {

            throw new IllegalArgumentException(
                    "Código do produto de compra não informado."
            );
        }

        if (codigoVenda == null
                || codigoVenda.isBlank()) {

            throw new IllegalArgumentException(
                    "Código do produto de venda não informado."
            );
        }

        repository.desvincular(
                codigoCompra.trim(),
                codigoVenda.trim()
        );
    }
}