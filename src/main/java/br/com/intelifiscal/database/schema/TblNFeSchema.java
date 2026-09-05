package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Schema da tabela de Notas Fiscais Eletrônicas.
 *
 * Armazena o cabeçalho das NF-e importadas.
 *
 * @author José Roberto Bizaio
 */
public final class TblNFeSchema {

    public static final String TABLE_NAME = "tblNFe";

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblNFe (

                id INTEGER PRIMARY KEY AUTOINCREMENT,

                chave TEXT NOT NULL UNIQUE,

                modelo TEXT NOT NULL,

                numero TEXT NOT NULL,

                serie TEXT NOT NULL,

                tipo TEXT NOT NULL,

                data_emissao TEXT NOT NULL,

                cnpj_emitente TEXT NOT NULL,
            
                emitente TEXT NOT NULL,
            
                municipio_emitente TEXT,
            
                cnpj_destinatario TEXT NOT NULL,
            
                destinatario TEXT NOT NULL,
            
                municipio_destinatario TEXT,
            
                valor_total NUMERIC NOT NULL,

                situacao TEXT NOT NULL,

                data_importacao TEXT NOT NULL

            );
            """;

    private static final String SQL_INDEX_CHAVE = """
            CREATE UNIQUE INDEX IF NOT EXISTS idx_nfe_chave
            ON tblNFe(chave);
            """;

    private static final String SQL_INDEX_NUMERO = """
            CREATE INDEX IF NOT EXISTS idx_nfe_numero
            ON tblNFe(numero);
            """;

    private static final String SQL_INDEX_TIPO = """
            CREATE INDEX IF NOT EXISTS idx_nfe_tipo
            ON tblNFe(tipo);
            """;

    private static final String SQL_INDEX_EMISSAO = """
            CREATE INDEX IF NOT EXISTS idx_nfe_data
            ON tblNFe(data_emissao);
            """;

    private static final String SQL_INDEX_EMITENTE = """
            CREATE INDEX IF NOT EXISTS idx_nfe_emitente
            ON tblNFe(cnpj_emitente);
            """;

    private static final String SQL_INDEX_DESTINATARIO = """
            CREATE INDEX IF NOT EXISTS idx_nfe_destinatario
            ON tblNFe(cnpj_destinatario);
            """;

    private TblNFeSchema() {
    }

    public static SchemaDefinition getSchema() {

        return new SchemaDefinition(
                TABLE_NAME,
                List.of(
                        SQL_CREATE_TABLE,
                        SQL_INDEX_CHAVE,
                        SQL_INDEX_NUMERO,
                        SQL_INDEX_TIPO,
                        SQL_INDEX_EMISSAO,
                        SQL_INDEX_EMITENTE,
                        SQL_INDEX_DESTINATARIO
                )
        );

    }

}