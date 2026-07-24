package br.com.intelifiscal.database.connection;

import br.com.intelifiscal.constants.DatabaseInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Responsável por gerenciar a conexão com o banco de dados SQLite.
 */
public final class DatabaseConnection {

    private static Connection connection;

    private DatabaseConnection() {
        // Impede a instanciação da classe.
    }

    /**
     * Retorna uma conexão ativa com o banco de dados.
     *
     * @return Connection
     * @throws SQLException caso ocorra erro na conexão
     */
    public static Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DatabaseInfo.JDBC_URL);
        }

        return connection;
    }

    /**
     * Fecha a conexão com o banco de dados.
     */
    public static void closeConnection() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        connection = null;
    }
}