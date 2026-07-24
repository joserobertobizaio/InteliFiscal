package br.com.intelifiscal.constants;

public final class DatabaseInfo {

    /**
     * Nome da pasta onde ficará o banco de dados.
     */
    public static final String DATABASE_DIRECTORY = "banco";

    /**
     * Nome do arquivo do banco.
     */
    public static final String DATABASE_NAME = "InteliFiscal.db";

    /**
     * Caminho completo do banco.
     */
    public static final String DATABASE_PATH =
            DATABASE_DIRECTORY + "/" + DATABASE_NAME;

    /**
     * URL JDBC utilizada pelo SQLite.
     */
    public static final String JDBC_URL =
            "jdbc:sqlite:" + DATABASE_PATH;

    private DatabaseInfo() {
        // Impede a instanciação da classe.
    }
}