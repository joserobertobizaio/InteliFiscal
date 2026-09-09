package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Schema da tabela de itens das Notas Fiscais Eletrônicas.
 *
 * Armazena os itens pertencentes às NF-e importadas.
 *
 * @author José Roberto Bizaio
 */
public final class TblNFeItemSchema {

    public static final String TABLE_NAME = "tblNFeItem";

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblNFeItem (

                id INTEGER PRIMARY KEY AUTOINCREMENT,

                id_nfe INTEGER NOT NULL,

                numero_item INTEGER NOT NULL,

                codigo_produto TEXT,

                codigo_barras TEXT,

                descricao TEXT,

                ncm TEXT,

                cest TEXT,

                cfop TEXT,

                unidade TEXT,

                quantidade NUMERIC,

                valor_unitario NUMERIC,

                valor_total NUMERIC,

                desconto NUMERIC,

                frete NUMERIC,

                seguro NUMERIC,

                outras_despesas NUMERIC,

                valor_icms NUMERIC,

                valor_ipi NUMERIC,

                valor_pis NUMERIC,

                valor_cofins NUMERIC,

                data_importacao TEXT

            );
            """;

    private static final String SQL_INDEX_NFE = """
            CREATE INDEX IF NOT EXISTS idx_nfe_item_nfe
            ON tblNFeItem(id_nfe);
            """;

    private static final String SQL_INDEX_CODIGO = """
            CREATE INDEX IF NOT EXISTS idx_nfe_item_codigo
            ON tblNFeItem(codigo_produto);
            """;

    private static final String SQL_INDEX_DESCRICAO = """
            CREATE INDEX IF NOT EXISTS idx_nfe_item_descricao
            ON tblNFeItem(descricao);
            """;

    private TblNFeItemSchema() {
    }

    public static SchemaDefinition getSchema() {

        return new SchemaDefinition(
                TABLE_NAME,
                List.of(
                        SQL_CREATE_TABLE,
                        SQL_INDEX_NFE,
                        SQL_INDEX_CODIGO,
                        SQL_INDEX_DESCRICAO
                )
        );
    }
}