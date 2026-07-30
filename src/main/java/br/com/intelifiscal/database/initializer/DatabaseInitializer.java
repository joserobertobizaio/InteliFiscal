package br.com.intelifiscal.database.initializer;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.database.version.DatabaseVersionManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Responsável pela inicialização do banco de dados.
 *
 * Esta classe apenas coordena o processo de criação e atualização
 * do banco de dados. Nenhum SQL deve ser escrito aqui.
 *
 * Fluxo:
 *
 * 1 - Abre conexão.
 * 2 - Executa o controle de versão.
 * 3 - Cria ou atualiza o banco.
 * 4 - Fecha conexão.
 *
 * @author José Roberto Bizaio
 */
public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    /**
     * Inicializa o banco de dados.
     */
    public static void initialize() {

        try (Connection connection = DatabaseConnection.getConnection()) {

            // Aqui será executado o gerenciamento de versões.
            //
            // Exemplo futuro:
            //

            DatabaseVersionManager.initialize(connection);

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao inicializar o banco de dados.",
                    e
            );
        }

    }

}