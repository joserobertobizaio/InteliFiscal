package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Schema da tabela de Produtos.
 *
 * Cadastro mestre dos produtos identificados
 * nas Notas Fiscais Eletrônicas importadas.
 *
 * @author José Roberto Bizaio
 */
public final class TblProdutoSchema {

    public static final String TABLE_NAME = "tblProduto";

    private static final String SQL_CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS tblProduto (

            id INTEGER PRIMARY KEY AUTOINCREMENT,

            codigo_produto TEXT NOT NULL,

            codigo_barras TEXT,

            descricao TEXT NOT NULL,

            ncm TEXT,

            cest TEXT,

            unidade TEXT,

            data_cadastro TEXT NOT NULL,

            ativo INTEGER NOT NULL DEFAULT 1

        );
        """;

    private static final String SQL_INDEX_CODIGO = """
        CREATE INDEX IF NOT EXISTS idx_produto_codigo
        ON tblProduto(codigo_produto);
        """;

    private static final String SQL_INDEX_BARRAS = """
        CREATE INDEX IF NOT EXISTS idx_produto_barras
        ON tblProduto(codigo_barras);
        """;

    private static final String SQL_INDEX_DESCRICAO = """
        CREATE INDEX IF NOT EXISTS idx_produto_descricao
        ON tblProduto(descricao);
        """;

    private static final String SQL_INDEX_NCM = """
        CREATE INDEX IF NOT EXISTS idx_produto_ncm
        ON tblProduto(ncm);
        """;

    private TblProdutoSchema() {
    }

    public static SchemaDefinition getSchema() {

        return new SchemaDefinition(
                TABLE_NAME,
                List.of(
                        SQL_CREATE_TABLE,
                        SQL_INDEX_CODIGO,
                        SQL_INDEX_BARRAS,
                        SQL_INDEX_DESCRICAO,
                        SQL_INDEX_NCM
                )
        );

    }

}