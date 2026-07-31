package br.com.intelifiscal.database.initializer;

import br.com.intelifiscal.constants.DatabaseInfo;
import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.database.version.DatabaseVersionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Responsável pela inicialização do banco de dados.
 *
 * Fluxo:
 *
 * 1 - Garante a existência da pasta do banco.
 * 2 - Abre conexão.
 * 3 - Executa o controle de versão.
 * 4 - Cria ou atualiza o banco.
 * 5 - Fecha conexão.
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

        try {

            createDatabaseDirectory();

            try (Connection connection = DatabaseConnection.getConnection()) {

                DatabaseVersionManager.initialize(connection);

            }

        } catch (SQLException | IOException e) {

            throw new RuntimeException(
                    "Erro ao inicializar o banco de dados.",
                    e
            );

        }

    }

    /**
     * Garante que a pasta do banco exista.
     */
    private static void createDatabaseDirectory() throws IOException {

        Path directory = Path.of(DatabaseInfo.DATABASE_DIRECTORY);

        if (Files.notExists(directory)) {

            Files.createDirectories(directory);

        }

    }

}