package br.com.intelifiscal.database.schema;

/**
 * Responsável pela criação da tabela de controle de versão
 * do banco de dados.
 *
 * Esta tabela registra a versão atual do schema,
 * permitindo futuras migrações.
 *
 * @author José Roberto Bizaio
 */
public final class TblVersaoBancoSchema {

    public static final String TABLE_NAME = "tblVersaoBanco";

    private TblVersaoBancoSchema() {
    }

    /**
     * Retorna o SQL responsável pela criação da tabela.
     *
     * @return SQL CREATE TABLE.
     */
    public static String getSql() {

        return """
                CREATE TABLE IF NOT EXISTS tblVersaoBanco (
                
                    id INTEGER PRIMARY KEY,
                    
                    versao INTEGER NOT NULL,
                    
                    data_atualizacao TEXT NOT NULL
                    
                );
                """;
    }

}