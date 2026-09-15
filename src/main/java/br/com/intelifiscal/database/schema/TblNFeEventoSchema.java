package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Schema da tabela de eventos das Notas Fiscais Eletrônicas.
 *
 * Armazena os eventos vinculados às NF-e, como cancelamento.
 *
 * @author José Roberto Bizaio
 */
public final class TblNFeEventoSchema {

    public static final String TABLE_NAME = "tblNFeEvento";

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblNFeEvento (

                id INTEGER PRIMARY KEY AUTOINCREMENT,

                id_nfe INTEGER,
            
                chave_nfe TEXT NOT NULL,
            
                tipo_evento TEXT NOT NULL,

                sequencia INTEGER,

                data_evento TEXT,

                protocolo TEXT,

                motivo TEXT,

                status TEXT,

                descricao TEXT,

                data_registro TEXT,

                FOREIGN KEY (id_nfe)
                    REFERENCES tblNFe(id)
                    ON DELETE CASCADE

            );
            """;

    private static final String SQL_INDEX_NFE = """
            CREATE INDEX IF NOT EXISTS idx_nfe_evento_nfe
            ON tblNFeEvento(id_nfe);
            """;

    private TblNFeEventoSchema() {
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