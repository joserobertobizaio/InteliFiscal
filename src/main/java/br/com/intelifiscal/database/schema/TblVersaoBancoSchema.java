package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Responsável pela definição da tabela de controle de versão
 * do banco de dados.
 *
 * @author José Roberto Bizaio
 */
public final class TblVersaoBancoSchema {

    public static final String TABLE_NAME = "tblVersaoBanco";

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblVersaoBanco (
            
                id INTEGER PRIMARY KEY,
                
                versao INTEGER NOT NULL,
                
                data_atualizacao TEXT NOT NULL
                
            );
            """;

    private TblVersaoBancoSchema() {
    }

    /**
     * Retorna a definição completa do schema.
     *
     * @return definição do schema.
     */
    public static SchemaDefinition getSchema() {

        return new SchemaDefinition(
                TABLE_NAME,
                List.of(
                        CREATE_TABLE
                )
        );

    }

}