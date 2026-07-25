package br.com.intelifiscal.database.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsável pela execução de comandos SQL.
 *
 * Esta classe é utilizada para executar scripts de criação
 * de tabelas, índices, triggers, views e futuras migrações.
 *
 * Não contém regras de negócio.
 *
 * @author José Roberto Bizaio
 */
public final class SqlExecutor {

    private SqlExecutor() {
    }

    /**
     * Executa um comando SQL.
     *
     * @param connection conexão ativa com o banco.
     * @param sql comando SQL a ser executado.
     * @throws SQLException erro durante a execução.
     */
    public static void execute(Connection connection, String sql) throws SQLException {

        try (Statement statement = connection.createStatement()) {

            statement.execute(sql);

        }

    }

}