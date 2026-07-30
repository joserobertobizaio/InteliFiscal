package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Responsável pela definição da tabela da empresa proprietária
 * do sistema.
 *
 * Esta tabela identifica quais CNPJs pertencem à empresa,
 * permitindo classificar automaticamente uma NF-e como
 * Entrada ou Saída.
 *
 * @author José Roberto Bizaio
 */
public final class TblMinhaEmpresaSchema {

    public static final String TABLE_NAME = "tblMinhaEmpresa";

    /**
     * Criação da tabela.
     */
    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tblMinhaEmpresa (
            
                id INTEGER PRIMARY KEY AUTOINCREMENT,
            
                cnpj TEXT NOT NULL UNIQUE,
            
                razao_social TEXT NOT NULL,
            
                nome_fantasia TEXT,
            
                inscricao_estadual TEXT,
            
                cep TEXT,
            
                uf TEXT,
            
                cidade TEXT,
            
                bairro TEXT,
            
                endereco TEXT,
            
                numero TEXT,
            
                telefone TEXT,
            
                email TEXT,
            
                ativo INTEGER NOT NULL DEFAULT 1,
            
                data_cadastro TEXT NOT NULL,
            
                data_atualizacao TEXT NOT NULL
            
            );
            """;

    private TblMinhaEmpresaSchema() {
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