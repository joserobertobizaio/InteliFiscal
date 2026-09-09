package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Schema da tabela de duplicatas das Notas Fiscais Eletrônicas.
 *
 * Armazena as parcelas/duplicatas informadas no XML da NF-e.
 *
 * @author José Roberto Bizaio
 */
public final class TblNFeDuplicataSchema {

    public static final String TABLE_NAME = "tblNFeDuplicata";

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblNFeDuplicata (

                id INTEGER PRIMARY KEY AUTOINCREMENT,

                id_nfe INTEGER NOT NULL,

                numero_duplicata TEXT,

                data_vencimento TEXT,

                valor NUMERIC,

                FOREIGN KEY (id_nfe)
                    REFERENCES tblNFe(id)
                    ON DELETE CASCADE

            );
            """;

    private static final String SQL_INDEX_NFE = """
            CREATE INDEX IF NOT EXISTS idx_nfe_duplicata_nfe
            ON tblNFeDuplicata(id_nfe);
            """;

    private TblNFeDuplicataSchema() {
    }

    public static SchemaDefinition getSchema() {

        return new SchemaDefinition(
                TABLE_NAME,
                List.of(
                        SQL_CREATE_TABLE,
                        SQL_INDEX_NFE
                )
        );

    }

}