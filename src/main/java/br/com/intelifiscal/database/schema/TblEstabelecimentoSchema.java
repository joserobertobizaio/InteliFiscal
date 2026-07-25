package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Responsável pela definição da tabela de estabelecimentos
 * pertencentes ao usuário do sistema.
 *
 * Esta tabela identifica quais CNPJs pertencem à empresa,
 * permitindo classificar automaticamente uma NF-e como
 * Entrada ou Saída.
 *
 * @author José Roberto Bizaio
 */
public final class TblEstabelecimentoSchema {

    public static final String TABLE_NAME = "tblEstabelecimento";

    /**
     * Criação da tabela.
     */
    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblEstabelecimento (
            
                id INTEGER PRIMARY KEY AUTOINCREMENT,

                cnpj TEXT NOT NULL UNIQUE,

                razao_social TEXT NOT NULL,

                nome_fantasia TEXT,

                inscricao_estadual TEXT,

                ativo INTEGER NOT NULL DEFAULT 1,

                data_cadastro TEXT NOT NULL,

                data_atualizacao TEXT NOT NULL

            );
            """;

    private TblEstabelecimentoSchema() {
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
                        SQL_CREATE_TABLE
                )
        );

    }

}