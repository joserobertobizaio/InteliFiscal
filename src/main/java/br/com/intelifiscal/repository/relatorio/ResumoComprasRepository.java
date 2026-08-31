package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.FornecedorCompraDTO;
import br.com.intelifiscal.dto.relatorio.ResumoComprasDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumoComprasRepository {

    //==================================================
    // RESUMO GERAL DE COMPRAS
    //==================================================

    public ResumoComprasDTO consultarResumo() {

        return consultarResumo(null, null);
    }


    public ResumoComprasDTO consultarResumo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                COUNT(DISTINCT n.id) AS notas,

                COUNT(i.id) AS itens,

                ROUND(
                    COALESCE(SUM(i.quantidade), 0),
                    3
                ) AS quantidade,

                ROUND(
                    COALESCE(SUM(i.valor_total), 0),
                    2
                ) AS valor_total,

                ROUND(
                    COALESCE(SUM(i.valor_total), 0)
                    /
                    NULLIF(COUNT(DISTINCT n.id), 0),
                    2
                ) AS ticket_medio

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e

            LEFT JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.cnpj_emitente <> e.cnpj
            """);


        if (dataInicio != null && dataFim != null) {

            sql.append("""
                
                AND date(n.data_emissao)
                    BETWEEN date(?) AND date(?)
                """);
        }


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql.toString())
        ) {

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        1,
                        dataInicio.toString()
                );

                ps.setString(
                        2,
                        dataFim.toString()
                );
            }


            try (ResultSet rs = ps.executeQuery()) {

                ResumoComprasDTO dto =
                        new ResumoComprasDTO();

                if (rs.next()) {

                    dto.setNotas(
                            rs.getInt("notas")
                    );

                    dto.setItens(
                            rs.getInt("itens")
                    );

                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );

                    dto.setTicketMedio(
                            rs.getBigDecimal("ticket_medio")
                    );
                }

                return dto;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar resumo de compras.",
                    e
            );
        }
    }


    //==================================================
    // COMPRAS POR FORNECEDOR
    //==================================================

    public List<FornecedorCompraDTO> consultarPorFornecedor() {

        return consultarPorFornecedor(null, null);
    }


    public List<FornecedorCompraDTO> consultarPorFornecedor(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                n.cnpj_emitente AS cnpj,

                MAX(n.emitente) AS fornecedor,

                COUNT(DISTINCT n.id) AS notas,

                COUNT(i.id) AS itens,

                ROUND(
                    COALESCE(SUM(i.quantidade), 0),
                    3
                ) AS quantidade,

                ROUND(
                    COALESCE(SUM(i.valor_total), 0),
                    2
                ) AS valor_total,

                MAX(n.data_emissao) AS data_ultima_compra

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e

            LEFT JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.cnpj_emitente <> e.cnpj
            """);


        if (dataInicio != null && dataFim != null) {

            sql.append("""
                
                AND date(n.data_emissao)
                    BETWEEN date(?) AND date(?)
                """);
        }


        sql.append("""

            GROUP BY n.cnpj_emitente

            ORDER BY valor_total DESC
            """);


        List<FornecedorCompraDTO> lista =
                new ArrayList<>();


        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql.toString())
        ) {

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        1,
                        dataInicio.toString()
                );

                ps.setString(
                        2,
                        dataFim.toString()
                );
            }


            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    FornecedorCompraDTO dto =
                            new FornecedorCompraDTO();

                    dto.setFornecedor(
                            rs.getString("fornecedor")
                    );

                    dto.setNotas(
                            rs.getInt("notas")
                    );

                    dto.setItens(
                            rs.getInt("itens")
                    );

                    dto.setQuantidade(
                            rs.getDouble("quantidade")
                    );

                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );

                    dto.setDataUltimaCompra(
                            rs.getString("data_ultima_compra")
                    );

                    lista.add(dto);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar compras por fornecedor.",
                    e
            );
        }


        return lista;
    }
}