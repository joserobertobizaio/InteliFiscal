package br.com.intelifiscal.service.produto;

import br.com.intelifiscal.repository.produto.ProdutoGrupoRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutoGrupoService {

    private final ProdutoGrupoRepository repository;

    public ProdutoGrupoService() {
        this.repository = new ProdutoGrupoRepository();
    }


    /**
     * Vincula vários produtos como sendo o mesmo produto.
     *
     * Regras:
     *
     * 1. É necessário selecionar pelo menos 2 produtos.
     *
     * 2. Se nenhum produto possuir vínculo,
     *    será criado um novo grupo.
     *
     * 3. Se alguns produtos já pertencerem ao mesmo grupo,
     *    os demais serão adicionados a esse grupo.
     *
     * 4. Se os produtos pertencerem a grupos diferentes,
     *    a operação será bloqueada.
     */
    public void vincularProdutos(
            List<Integer> idsProdutos) {

        validarSelecao(idsProdutos);

        Set<Integer> gruposExistentes =
                new HashSet<>();

        for (Integer idProduto : idsProdutos) {

            Integer idGrupo =
                    repository.buscarGrupoDoProduto(idProduto);

            if (idGrupo != null) {
                gruposExistentes.add(idGrupo);
            }
        }

        /*
         * Não permitimos juntar automaticamente
         * dois grupos que já existem.
         */
        if (gruposExistentes.size() > 1) {

            throw new IllegalArgumentException(
                    "Os produtos selecionados já pertencem " +
                            "a grupos diferentes. " +
                            "Não é possível juntar esses grupos automaticamente."
            );
        }

        int idGrupo;

        if (gruposExistentes.isEmpty()) {

            /*
             * Nenhum produto possui vínculo.
             * Criamos um novo grupo.
             */
            idGrupo = repository.criarGrupo();

        } else {

            /*
             * Já existe um grupo.
             * Vamos utilizar esse grupo.
             */
            idGrupo =
                    gruposExistentes.iterator().next();
        }

        /*
         * Adiciona ao grupo somente os produtos
         * que ainda não pertencem a ele.
         */
        for (Integer idProduto : idsProdutos) {

            Integer grupoAtual =
                    repository.buscarGrupoDoProduto(idProduto);

            if (grupoAtual == null) {

                repository.adicionarProdutoAoGrupo(
                        idGrupo,
                        idProduto
                );
            }
        }
    }


    /**
     * Retorna todos os produtos vinculados
     * ao mesmo grupo do produto informado.
     *
     * Se o produto não possuir vínculo,
     * retorna uma lista vazia.
     */
    public List<Integer> listarProdutosVinculados(
            int idProduto) {

        Integer idGrupo =
                repository.buscarGrupoDoProduto(idProduto);

        if (idGrupo == null) {
            return new ArrayList<>();
        }

        return repository.listarProdutosDoGrupo(
                idGrupo
        );
    }


    /**
     * Desvincula um produto do grupo.
     *
     * O produto NÃO é excluído da tblProduto.
     *
     * Se, depois da remoção, restar apenas um produto
     * no grupo, o grupo deixa de fazer sentido como
     * vínculo e será excluído.
     */
    public void desvincularProduto(
            int idProduto) {

        Integer idGrupo =
                repository.buscarGrupoDoProduto(idProduto);

        if (idGrupo == null) {

            throw new IllegalArgumentException(
                    "Este produto não possui vínculo."
            );
        }

        List<Integer> produtosDoGrupo =
                repository.listarProdutosDoGrupo(
                        idGrupo
                );

        repository.removerProdutoDoGrupo(
                idGrupo,
                idProduto
        );

        /*
         * Depois de remover o produto, verificamos
         * quantos produtos ainda permanecem no grupo.
         */
        List<Integer> restantes =
                repository.listarProdutosDoGrupo(
                        idGrupo
                );

        /*
         * Um grupo com apenas um produto não representa
         * mais um vínculo entre produtos.
         */
        if (restantes.size() <= 1) {

            repository.excluirGrupo(idGrupo);
        }
    }


    /**
     * Retorna o ID do grupo de um produto.
     *
     * Útil para a tela e para outras regras do sistema.
     */
    public Integer buscarGrupoDoProduto(
            int idProduto) {

        return repository.buscarGrupoDoProduto(
                idProduto
        );
    }


    /**
     * Valida a seleção feita pelo usuário.
     */
    private void validarSelecao(
            List<Integer> idsProdutos) {

        if (idsProdutos == null
                || idsProdutos.size() < 2) {

            throw new IllegalArgumentException(
                    "Selecione pelo menos 2 produtos para criar um vínculo."
            );
        }

        /*
         * Remove IDs repetidos para evitar operações
         * desnecessárias.
         */
        Set<Integer> idsUnicos =
                new HashSet<>(idsProdutos);

        if (idsUnicos.size() < 2) {

            throw new IllegalArgumentException(
                    "Selecione pelo menos 2 produtos diferentes."
            );
        }
    }
}