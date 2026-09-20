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

                COALESCE(
                    ROUND(
                        COALESCE(SUM(i.valor_total), 0)
                        /
                        NULLIF(COUNT(DISTINCT n.id), 0),
                        2
                    ),
                    0
                ) AS ticket_medio

            FROM tblNFe n

            LEFT JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.tipo = 'Compra'
              AND n.situacao <> 'CANCELADA'

              AND EXISTS (
                  SELECT 1
                  FROM tblNFeItem ix
                  WHERE ix.id_nfe = n.id
              )

              AND NOT EXISTS (
                  SELECT 1
                  FROM tblNFeItem ix
                  WHERE ix.id_nfe = n.id
                    AND (
                        ix.cfop IS NULL
                        OR ix.cfop NOT IN (
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
                    )
              )
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
        WITH notas_filtradas AS (

            SELECT
                n.id,
                n.cnpj_emitente,
                n.emitente,
                n.data_emissao,
                n.valor_total

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e

            WHERE n.cnpj_emitente <> e.cnpj
              AND n.tipo = 'Compra'
              AND n.situacao <> 'CANCELADA'

              AND EXISTS (
                  SELECT 1
                  FROM tblNFeItem ix
                  WHERE ix.id_nfe = n.id
              )

              AND NOT EXISTS (
                  SELECT 1
                  FROM tblNFeItem ix
                  WHERE ix.id_nfe = n.id
                    AND (
                        ix.cfop IS NULL
                        OR ix.cfop NOT IN (
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
                    )
              )
        """);

        //==================================================
        // FILTRO DE DATA
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
            
              AND date(n.data_emissao)
                  BETWEEN date(?) AND date(?)
            """);
        }

        sql.append("""
        ),

        itens_por_nota AS (

            SELECT
                i.id_nfe,

                COUNT(i.id) AS itens,

                ROUND(
                    COALESCE(SUM(i.quantidade), 0),
                    3
                ) AS quantidade

            FROM tblNFeItem i

            INNER JOIN notas_filtradas nf
                ON nf.id = i.id_nfe

            GROUP BY
                i.id_nfe
        )

        SELECT

            nf.cnpj_emitente AS cnpj,

            MAX(nf.emitente) AS fornecedor,

            COUNT(nf.id) AS notas,

            COALESCE(
                SUM(ipn.itens),
                0
            ) AS itens,

            ROUND(
                COALESCE(
                    SUM(ipn.quantidade),
                    0
                ),
                3
            ) AS quantidade,

            ROUND(
                COALESCE(
                    SUM(nf.valor_total),
                    0
                ),
                2
            ) AS valor_total,

            MAX(nf.data_emissao) AS data_ultima_compra

        FROM notas_filtradas nf

        LEFT JOIN itens_por_nota ipn
            ON ipn.id_nfe = nf.id

        GROUP BY
            nf.cnpj_emitente

        ORDER BY
            valor_total DESC
        """);

        List<FornecedorCompraDTO> lista =
                new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql.toString()
                        )
        ) {

            int parametro = 1;

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        parametro++,
                        dataInicio.toString()
                );

                ps.setString(
                        parametro++,
                        dataFim.toString()
                );
            }

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    FornecedorCompraDTO dto =
                            new FornecedorCompraDTO();

                    dto.setCnpj(
                            rs.getString("cnpj")
                    );

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