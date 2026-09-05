package br.com.intelifiscal.database.version;

import br.com.intelifiscal.database.manager.SchemaManager;
import br.com.intelifiscal.database.executor.SqlExecutor;
import br.com.intelifiscal.database.schema.SchemaDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
     * Inicializa o banco de dados e executa as migrações necessárias.
     *
     * @param connection conexão ativa com o banco de dados.
     * @throws SQLException erro de acesso ao banco.
     */
    public static void initialize(Connection connection)
            throws SQLException {

        // ========================================================
        // CRIA AS TABELAS E ÍNDICES DOS SCHEMAS
        // ========================================================

        List<SchemaDefinition> schemas =
                SchemaManager.getSchemas();

        for (SchemaDefinition schema : schemas) {

            for (String sql : schema.scripts()) {

                SqlExecutor.execute(
                        connection,
                        sql
                );
            }
        }


        // ========================================================
        // EXECUTA MIGRAÇÕES
        // ========================================================

        executarMigracaoVersao2(
                connection
        );
    }


    // ============================================================
    // MIGRAÇÃO VERSÃO 2
    // ============================================================
    //
    // Acrescenta:
    //
    // municipio_emitente
    // municipio_destinatario
    //
    // A migração verifica primeiro se as colunas já existem.
    // Portanto pode ser executada várias vezes com segurança.
    // ============================================================

    private static void executarMigracaoVersao2(
            Connection connection)
            throws SQLException {

        // --------------------------------------------------------
        // MUNICÍPIO DO EMITENTE
        // --------------------------------------------------------

        if (!colunaExiste(
                connection,
                "tblNFe",
                "municipio_emitente"
        )) {

            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 """
                                 ALTER TABLE tblNFe
                                 ADD COLUMN municipio_emitente TEXT
                                 """
                         )) {

                ps.executeUpdate();
            }
        }


        // --------------------------------------------------------
        // MUNICÍPIO DO DESTINATÁRIO
        // --------------------------------------------------------

        if (!colunaExiste(
                connection,
                "tblNFe",
                "municipio_destinatario"
        )) {

            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 """
                                 ALTER TABLE tblNFe
                                 ADD COLUMN municipio_destinatario TEXT
                                 """
                         )) {

                ps.executeUpdate();
            }
        }


        // --------------------------------------------------------
        // REGISTRA VERSÃO 2
        // --------------------------------------------------------

        registrarVersao(
                connection,
                2
        );
    }


    // ============================================================
    // VERIFICA SE UMA COLUNA EXISTE
    // ============================================================

    private static boolean colunaExiste(
            Connection connection,
            String tabela,
            String coluna)
            throws SQLException {

        String sql =
                "PRAGMA table_info(" + tabela + ")";


        try (PreparedStatement ps =
                     connection.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                String nomeColuna =
                        rs.getString("name");

                if (coluna.equalsIgnoreCase(
                        nomeColuna
                )) {

                    return true;
                }
            }
        }

        return false;
    }


    // ============================================================
    // REGISTRA VERSÃO DO BANCO
    // ============================================================

    private static void registrarVersao(
            Connection connection,
            int versao)
            throws SQLException {

        // --------------------------------------------------------
        // VERIFICA SE JÁ EXISTE REGISTRO
        // --------------------------------------------------------

        Integer versaoAtual = null;


        String sqlConsulta =
                """
                SELECT versao
                FROM tblVersaoBanco
                WHERE id = 1
                """;


        try (PreparedStatement ps =
                     connection.prepareStatement(
                             sqlConsulta
                     );
             ResultSet rs =
                     ps.executeQuery()) {

            if (rs.next()) {

                versaoAtual =
                        rs.getInt("versao");
            }
        }


        // --------------------------------------------------------
        // NÃO EXISTE → INSERT
        // --------------------------------------------------------

        if (versaoAtual == null) {

            String sqlInsert =
                    """
                    INSERT INTO tblVersaoBanco
                    (
                        id,
                        versao,
                        data_atualizacao
                    )
                    VALUES
                    (
                        1,
                        ?,
                        datetime('now')
                    )
                    """;


            try (PreparedStatement ps =
                         connection.prepareStatement(
                                 sqlInsert
                         )) {

                ps.setInt(
                        1,
                        versao
                );

                ps.executeUpdate();
            }

            return;
        }


        // --------------------------------------------------------
        // JÁ ESTÁ NA MESMA OU VERSÃO SUPERIOR
        // --------------------------------------------------------

        if (versaoAtual >= versao) {

            return;
        }


        // --------------------------------------------------------
        // ATUALIZA VERSÃO
        // --------------------------------------------------------

        String sqlUpdate =
                """
                UPDATE tblVersaoBanco
                SET versao = ?,
                    data_atualizacao = datetime('now')
                WHERE id = 1
                """;


        try (PreparedStatement ps =
                     connection.prepareStatement(
                             sqlUpdate
                     )) {

            ps.setInt(
                    1,
                    versao
            );

            ps.executeUpdate();
        }
    }
}