package br.com.intelifiscal.database.version;

import br.com.intelifiscal.database.manager.SchemaManager;
import br.com.intelifiscal.database.executor.SqlExecutor;
import br.com.intelifiscal.database.schema.SchemaDefinition;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Responsável pelo gerenciamento de versões do banco de dados.
 *
 * Esta classe controla a criação inicial do banco
 * e futuras migrações entre versões.
 *
 * Nenhum SQL de criação de tabelas deve permanecer aqui.
 * As instruções SQL ficarão nas classes do pacote schema
 * e nas futuras classes de migration.
 *
 * @author José Roberto Bizaio
 */
public final class DatabaseVersionManager {

    private DatabaseVersionManager() {
    }

    /**
     * Inicializa o controle de versão do banco.
     *
     * @param connection conexão ativa com o banco de dados.
     * @throws SQLException erro de acesso ao banco.
     */
    public static void initialize(Connection connection) throws SQLException {

        List<SchemaDefinition> schemas = SchemaManager.getSchemas();

        for (SchemaDefinition schema : schemas) {

            for (String sql : schema.scripts()) {

                SqlExecutor.execute(connection, sql);

            }

            // Primeira versão do sistema.
            //
            // Fluxo futuro:
            //
            // 1 - Verificar existência da tabela tblVersaoBanco.
            // 2 - Caso não exista:
            //      - criar tabela;
            //      - criar demais tabelas;
            //      - registrar versão 1.
            //
            // 3 - Caso exista:
            //      - verificar versão atual.
            //      - executar migrações necessárias.
        }
    }
}