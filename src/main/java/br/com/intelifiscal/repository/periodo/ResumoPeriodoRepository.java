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
import java.math.BigDecimal;

public class ResumoPeriodoRepository {

    //==================================================
    // ÚLTIMOS 12 MESES
    //==================================================

    public List<ResumoPeriodoDTO> consultarUltimos12Meses() {

        String sql = """
        WITH periodo AS (
        SELECT
            MAX(data_emissao) AS data_final,

            date(
                MAX(data_emissao),
                'start of month',
                '-11 months'
            ) AS data_inicial

        FROM tblNFe
        ),

        notas_periodo AS (

            SELECT
                n.id,
                n.valor_total,

                CASE
                    WHEN n.cnpj_emitente = e.cnpj
                        THEN 'VENDA'
                    ELSE 'COMPRA'
                END AS operacao

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e
            CROSS JOIN periodo p

            WHERE
                n.data_emissao >= p.data_inicial
                AND n.data_emissao <= p.data_final
        ),

        itens_por_nota AS (

            SELECT
                i.id_nfe,

                COUNT(i.id) AS itens,

                ROUND(
                    COALESCE(
                        SUM(i.quantidade),
                        0
                    ),
                    3
                ) AS quantidade

            FROM tblNFeItem i

            INNER JOIN notas_periodo n
                ON n.id = i.id_nfe

            GROUP BY
                i.id_nfe
        )

        SELECT

            n.operacao,

            COUNT(n.id) AS notas,

            COALESCE(
                SUM(i.itens),
                0
            ) AS itens,

            ROUND(
                COALESCE(
                    SUM(i.quantidade),
                    0
                ),
                3
            ) AS quantidade,

            ROUND(
                COALESCE(
                    SUM(n.valor_total),
                    0
                ),
                2
            ) AS valor_total,

            ROUND(
                COALESCE(
                    SUM(n.valor_total),
                    0
                )
                /
                NULLIF(
                    COUNT(n.id),
                    0
                ),
                2
            ) AS ticket_medio

        FROM notas_periodo n

        LEFT JOIN itens_por_nota i
            ON i.id_nfe = n.id

        GROUP BY
            n.operacao

        ORDER BY
            n.operacao

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

    public List<ResumoMensalDTO> consultarResumoMensalUltimos12Meses() {

        String sql = """
        WITH periodo AS (

            SELECT
                MAX(data_emissao) AS data_final,
                date(MAX(data_emissao), '-12 months')
                    AS data_inicial

            FROM tblNFe
        ),

        dados AS (

            SELECT

                strftime(
                    '%m/%Y',
                    n.data_emissao
                ) AS mes,

                strftime(
                    '%Y-%m',
                    n.data_emissao
                ) AS ordem_mes,

                CASE

                    WHEN n.cnpj_emitente = e.cnpj
                        THEN 'VENDA'

                    ELSE 'COMPRA'

                END AS operacao,

                n.valor_total

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e
            CROSS JOIN periodo p

            WHERE
                n.data_emissao >= p.data_inicial
                AND n.data_emissao <= p.data_final
        )

        SELECT

            mes,
            operacao,

            ROUND(
                COALESCE(
                    SUM(valor_total),
                    0
                ),
                2
            ) AS valor_total

        FROM dados

        GROUP BY
            mes,
            ordem_mes,
            operacao

        ORDER BY
            ordem_mes,
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