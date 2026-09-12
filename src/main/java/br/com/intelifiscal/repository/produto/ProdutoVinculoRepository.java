package br.com.intelifiscal.repository.produto;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.produto.ProdutoVinculoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoVinculoRepository {

    // ============================================================
    // GARANTE A EXISTÊNCIA DA TABELA
    // ============================================================

    private void criarTabelaSeNecessario(
            Connection conn) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS tblProdutoVinculo (

                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    codigo_compra TEXT NOT NULL,

                    codigo_venda TEXT NOT NULL,

                    UNIQUE (
                        codigo_compra,
                        codigo_venda
                    )
                )
                """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.executeUpdate();
        }
    }


    // ============================================================
    // VINCULAR
    // ============================================================

    public void vincular(
            String codigoCompra,
            String codigoVenda) {

        String sql = """
                INSERT INTO tblProdutoVinculo (
                    codigo_compra,
                    codigo_venda
                )
                VALUES (?, ?)
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            criarTabelaSeNecessario(conn);

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        codigoCompra
                );

                ps.setString(
                        2,
                        codigoVenda
                );

                ps.executeUpdate();
            }

        } catch (SQLException e) {

            // ====================================================
            // JÁ EXISTE
            // ====================================================

            if (e.getMessage() != null
                    && e.getMessage()
                    .toLowerCase()
                    .contains("unique")) {

                throw new IllegalArgumentException(
                        "Os produtos já estão vinculados."
                );
            }

            throw new RuntimeException(
                    "Erro ao vincular os produtos.",
                    e
            );
        }
    }


    // ============================================================
    // VERIFICA VÍNCULO
    // ============================================================

    public boolean existeVinculo(
            String codigoCompra,
            String codigoVenda) {

        String sql = """
                SELECT 1
                FROM tblProdutoVinculo
                WHERE codigo_compra = ?
                  AND codigo_venda = ?
                LIMIT 1
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            criarTabelaSeNecessario(conn);

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        codigoCompra
                );

                ps.setString(
                        2,
                        codigoVenda
                );

                try (ResultSet rs =
                             ps.executeQuery()) {

                    return rs.next();
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar vínculo dos produtos.",
                    e
            );
        }
    }


    // ============================================================
    // DESVINCULAR
    // ============================================================

    public void desvincular(
            String codigoCompra,
            String codigoVenda) {

        String sql = """
                DELETE FROM tblProdutoVinculo
                WHERE codigo_compra = ?
                  AND codigo_venda = ?
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection()
        ) {

            criarTabelaSeNecessario(conn);

            try (PreparedStatement ps =
                         conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        codigoCompra
                );

                ps.setString(
                        2,
                        codigoVenda
                );

                ps.executeUpdate();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao desvincular os produtos.",
                    e
            );
        }
    }

    public java.util.List<ProdutoVinculoDTO> listarTodos() {

        String sql = """
            SELECT
                v.id,
                v.codigo_compra,
                pc.descricao AS descricao_compra,
                pc.unidade AS unidade_compra,
                v.codigo_venda,
                pv.descricao AS descricao_venda,
                pv.unidade AS unidade_venda
            FROM tblProdutoVinculo v
            LEFT JOIN tblProduto pc
                ON pc.codigo_produto = v.codigo_compra
            LEFT JOIN tblProduto pv
                ON pv.codigo_produto = v.codigo_venda
            ORDER BY
                pc.descricao,
                pv.descricao
            """;

        java.util.List<ProdutoVinculoDTO> lista = new java.util.ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ProdutoVinculoDTO dto = new ProdutoVinculoDTO();

                dto.setId(rs.getInt("id"));
                dto.setCodigoCompra(rs.getString("codigo_compra"));
                dto.setDescricaoCompra(rs.getString("descricao_compra"));
                dto.setUnidadeCompra(rs.getString("unidade_compra"));

                dto.setCodigoVenda(rs.getString("codigo_venda"));
                dto.setDescricaoVenda(rs.getString("descricao_venda"));
                dto.setUnidadeVenda(rs.getString("unidade_venda"));

                lista.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao listar os produtos vinculados.",
                    e
            );
        }

        return lista;
    }
}