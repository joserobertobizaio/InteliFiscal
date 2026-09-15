package br.com.intelifiscal.constants;

import java.nio.file.Path;

/**
 * Informações e localização do banco de dados SQLite.
 *
 * <p>O projeto trabalha com dois ambientes:</p>
 *
 * <ul>
 *     <li>Desenvolvimento: banco dentro da pasta do projeto.</li>
 *     <li>Executável: banco na pasta de dados do usuário.</li>
 * </ul>
 */
public final class DatabaseInfo {

    /**
     * Define se a aplicação deve utilizar o banco
     * no diretório de dados do usuário.
     *
     * <p>
     * Para o teste do executável, deixar como {@code true}.
     * </p>
     *
     * <p>
     * No ambiente de desenvolvimento original,
     * manteremos {@code false}.
     * </p>
     */
    public static final boolean USAR_DIRETORIO_USUARIO = false;

    /**
     * Nome da pasta utilizada pelo banco no ambiente
     * de desenvolvimento.
     */
    public static final String DATABASE_DIRECTORY = "banco";

    /**
     * Nome do arquivo do banco.
     */
    public static final String DATABASE_NAME = "InteliFiscal.db";

    /**
     * Diretório de dados do InteliFiscal no computador do usuário.
     */
    public static final Path USER_DATABASE_DIRECTORY =
            Path.of(
                    System.getenv("LOCALAPPDATA"),
                    "InteliFiscal"
            );

    /**
     * Caminho físico do banco de dados.
     */
    public static final Path DATABASE_PATH;

    /**
     * URL JDBC utilizada pelo SQLite.
     */
    public static final String JDBC_URL;

    static {

        if (USAR_DIRETORIO_USUARIO) {

            DATABASE_PATH =
                    USER_DATABASE_DIRECTORY.resolve(
                            DATABASE_NAME
                    );

        } else {

            DATABASE_PATH =
                    Path.of(
                            DATABASE_DIRECTORY,
                            DATABASE_NAME
                    );
        }

        JDBC_URL =
                "jdbc:sqlite:" +
                        DATABASE_PATH.toString();
    }

    private DatabaseInfo() {
        // Impede a instanciação da classe.
    }
}