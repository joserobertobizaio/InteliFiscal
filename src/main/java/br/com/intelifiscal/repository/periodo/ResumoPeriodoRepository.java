package br.com.intelifiscal.repository.periodo;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.periodo.ResumoPeriodoDTO;
import br.com.intelifiscal.dto.periodo.ResumoMensalDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResumoPeriodoRepository {


    //==================================================
    // ÚLTIMOS 12 MESES
    //==================================================

    public List<ResumoPeriodoDTO> consultarUltimos12Meses() {

        String sql = """

            WITH periodo AS (

                SELECT
                    MAX(data_emissao) AS data_final,
                    date(MAX(data_emissao), '-12 months')
                        AS data_inicial

                FROM tblNFe
            )

            SELECT

                CASE
                    WHEN n.cnpj_emitente = e.cnpj
                        THEN 'VENDA'
                    ELSE 'COMPRA'
                END AS operacao,

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

            CROSS JOIN periodo p

            LEFT JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE
                n.data_emissao >= p.data_inicial

                AND n.data_emissao <= p.data_final

            GROUP BY
                operacao

            ORDER BY
                operacao

            """;


        List<ResumoPeriodoDTO> lista =
                new ArrayList<>();


        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {


            while (rs.next()) {

                ResumoPeriodoDTO dto =
                        new ResumoPeriodoDTO();


                dto.setOperacao(
                        rs.getString("operacao")
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


                dto.setTicketMedio(
                        rs.getBigDecimal("ticket_medio")
                );


                lista.add(dto);
            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar resumo dos últimos 12 meses.",
                    e
            );
        }


        return lista;
    }

    //==================================================
    // RESUMO MENSAL - ÚLTIMOS 12 MESES
    //==================================================

    public List<ResumoMensalDTO> consultarResumoMensal() {

        String sql = """

            WITH periodo AS (

                SELECT
                    MAX(data_emissao) AS data_final,
                    date(MAX(data_emissao), '-12 months')
                        AS data_inicial

                FROM tblNFe
            )

            SELECT

                strftime('%Y-%m', n.data_emissao) AS mes,

                CASE
                    WHEN n.cnpj_emitente = e.cnpj
                        THEN 'VENDA'
                    ELSE 'COMPRA'
                END AS operacao,

                ROUND(
                    COALESCE(SUM(n.valor_total), 0),
                    2
                ) AS valor_total

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e

            CROSS JOIN periodo p

            WHERE
                n.data_emissao >= p.data_inicial

                AND n.data_emissao <= p.data_final

            GROUP BY
                mes,
                operacao

            ORDER BY
                mes,
                operacao

            """;


        List<ResumoMensalDTO> lista =
                new ArrayList<>();


        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ) {


            while (rs.next()) {

                ResumoMensalDTO dto =
                        new ResumoMensalDTO();


                dto.setMes(
                        rs.getString("mes")
                );


                dto.setOperacao(
                        rs.getString("operacao")
                );


                dto.setValorTotal(
                        rs.getBigDecimal("valor_total")
                );


                lista.add(dto);
            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar resumo mensal dos últimos 12 meses.",
                    e
            );
        }


        return lista;
    }
}