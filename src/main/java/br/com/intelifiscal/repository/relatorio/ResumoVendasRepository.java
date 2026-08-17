package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResumoVendasRepository {

    //==================================================
    // RESUMO GERAL DE VENDAS
    //==================================================

    public ResumoVendasDTO consultarResumo() {

        String sql = """
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

            JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.cnpj_emitente = e.cnpj

              AND i.cfop IN (
                  '5101',
                  '5102',
                  '5401',
                  '5405',
                  '6101',
                  '6102',
                  '6107',
                  '6108',
                  '6401',
                  '6404'
              )
            """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            ResumoVendasDTO dto =
                    new ResumoVendasDTO();

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

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar resumo de vendas.",
                    e
            );
        }
    }
}