package br.com.intelifiscal.repository.produto;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.produto.ProdutoVinculoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProdutoVinculoRepository {

    // ============================================================
    // SALVAR VÍNCULO
    // ============================================================

    public void salvar(
            int idProduto,
            String codigoProdutoNfe,
            String descricaoProdutoNfe,
            String cnpjEmitente) {

        String sql = """
                INSERT INTO tblProdutoVinculo (
                    id_produto,
                    codigo_produto_nfe,
                    descricao_produto_nfe,
                    cnpj_emitente,
                    data_vinculo,
                    ativo
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProduto);

            ps.setString(
                    2,
                    codigoProdutoNfe
            );

            ps.setString(
                    3,
                    descricaoProdutoNfe
            );

            ps.setString(
                    4,
                    cnpjEmitente
            );

            ps.setString(
                    5,
                    LocalDateTime.now().toString()
            );

            ps.setInt(6, 1);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao salvar vínculo do produto.",
                    e
            );
        }
    }


    // ============================================================
    // LISTAR VÍNCULOS DO PRODUTO
    // ============================================================

    public List<ProdutoVinculoDTO> listarPorProduto(
            int idProduto) {

        String sql = """
                SELECT
                    id,
                    id_produto,
                    codigo_produto_nfe,
                    descricao_produto_nfe,
                    cnpj_emitente,
                    data_vinculo,
                    ativo
                FROM tblProdutoVinculo
                WHERE id_produto = ?
                  AND ativo = 1
                ORDER BY descricao_produto_nfe
                """;

        List<ProdutoVinculoDTO> lista =
                new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProduto);

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    lista.add(
                            mapear(rs)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao listar vínculos do produto.",
                    e
            );
        }

        return lista;
    }


    // ============================================================
    // DESVINCULAR
    // ============================================================

    public void desvincular(int id) {

        String sql = """
                UPDATE tblProdutoVinculo
                SET ativo = 0
                WHERE id = ?
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao desvincular produto.",
                    e
            );
        }
    }


    // ============================================================
    // VERIFICA SE JÁ EXISTE VÍNCULO
    // ============================================================

    public boolean existeVinculo(
            int idProduto,
            String codigoProdutoNfe,
            String cnpjEmitente) {

        String sql = """
                SELECT 1
                FROM tblProdutoVinculo
                WHERE id_produto = ?
                  AND codigo_produto_nfe = ?
                  AND (
                        cnpj_emitente = ?
                        OR (
                            cnpj_emitente IS NULL
                            AND ? IS NULL
                        )
                      )
                  AND ativo = 1
                LIMIT 1
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    idProduto
            );

            ps.setString(
                    2,
                    codigoProdutoNfe
            );

            ps.setString(
                    3,
                    cnpjEmitente
            );

            ps.setString(
                    4,
                    cnpjEmitente
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                return rs.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao verificar vínculo do produto.",
                    e
            );
        }
    }


    // ============================================================
    // BUSCAR POR ID
    // ============================================================

    public ProdutoVinculoDTO buscarPorId(
            int id) {

        String sql = """
                SELECT
                    id,
                    id_produto,
                    codigo_produto_nfe,
                    descricao_produto_nfe,
                    cnpj_emitente,
                    data_vinculo,
                    ativo
                FROM tblProdutoVinculo
                WHERE id = ?
                LIMIT 1
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    id
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {

                    return mapear(rs);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao buscar vínculo do produto.",
                    e
            );
        }

        return null;
    }


    // ============================================================
    // MAPEAR
    // ============================================================

    private ProdutoVinculoDTO mapear(
            ResultSet rs)
            throws SQLException {

        ProdutoVinculoDTO dto =
                new ProdutoVinculoDTO();

        dto.setId(
                rs.getInt("id")
        );

        dto.setIdProduto(
                rs.getInt("id_produto")
        );

        dto.setCodigoProdutoNfe(
                rs.getString(
                        "codigo_produto_nfe"
                )
        );

        dto.setDescricaoProdutoNfe(
                rs.getString(
                        "descricao_produto_nfe"
                )
        );

        dto.setCnpjEmitente(
                rs.getString(
                        "cnpj_emitente"
                )
        );

        String data =
                rs.getString(
                        "data_vinculo"
                );

        if (data != null
                && !data.isBlank()) {

            dto.setDataVinculo(
                    LocalDateTime.parse(data)
            );
        }

        dto.setAtivo(
                rs.getInt("ativo") == 1
        );

        return dto;
    }
}