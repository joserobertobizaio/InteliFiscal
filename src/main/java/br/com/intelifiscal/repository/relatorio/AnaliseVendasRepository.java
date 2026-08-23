package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.AnaliseVendasDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnaliseVendasRepository {

    //==================================================
    // CONSULTAR ANÁLISE DE VENDAS
    //==================================================

    public List<AnaliseVendasDTO> consultar(
            LocalDate dataInicio,
            LocalDate dataFim,
            String cnpjCliente,
            String codigoProduto
    ) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                n.cnpj_destinatario AS cnpj_cliente,

                MAX(n.destinatario) AS cliente,

                COUNT(DISTINCT n.id) AS notas,

                MAX(date(n.data_emissao)) AS data_ultima_venda,

                ROUND(
                    COALESCE(SUM(i.quantidade), 0),
                    3
                ) AS quantidade,

                ROUND(
                    COALESCE(SUM(i.valor_total), 0),
                    2
                ) AS valor_total,

                ROUND(
                    (
                        COALESCE(SUM(i.valor_total), 0)
                        /
                        NULLIF(
                            (
                                SELECT
                                    COALESCE(SUM(i2.valor_total), 0)
                                FROM tblNFe n2

                                CROSS JOIN tblMinhaEmpresa e2

                                LEFT JOIN tblNFeItem i2
                                    ON i2.id_nfe = n2.id

                                WHERE n2.tipo = 'Venda'

                                AND i2.cfop IN (
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
            """);


        //==================================================
        // FILTRO DE DATA - SUBCONSULTA
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
                                AND date(n2.data_emissao)
                                    BETWEEN date(?) AND date(?)
            """);
        }


        //==================================================
        // FILTRO DE CLIENTE - SUBCONSULTA
        //==================================================

        if (cnpjCliente != null
                && !cnpjCliente.isBlank()) {

            sql.append("""
                                AND n2.cnpj_destinatario = ?
            """);
        }


        //==================================================
        // FILTRO DE PRODUTO - SUBCONSULTA
        //==================================================

        if (codigoProduto != null
                && !codigoProduto.isBlank()) {

            sql.append("""
                                AND i2.codigo_produto = ?
            """);
        }


        sql.append("""
                            ),
                            0
                        )
                    ) * 100,
                    2
                ) AS participacao

            FROM tblNFe n

            CROSS JOIN tblMinhaEmpresa e

            LEFT JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.tipo = 'Venda'

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
            """);


        //==================================================
        // FILTRO DE DATA - CONSULTA PRINCIPAL
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
                AND date(n.data_emissao)
                    BETWEEN date(?) AND date(?)
            """);
        }


        //==================================================
        // FILTRO DE CLIENTE - CONSULTA PRINCIPAL
        //==================================================

        if (cnpjCliente != null
                && !cnpjCliente.isBlank()) {

            sql.append("""
                AND n.cnpj_destinatario = ?
            """);
        }


        //==================================================
        // FILTRO DE PRODUTO - CONSULTA PRINCIPAL
        //==================================================

        if (codigoProduto != null
                && !codigoProduto.isBlank()) {

            sql.append("""
                AND i.codigo_produto = ?
            """);
        }


        //==================================================
        // AGRUPAMENTO
        //==================================================

        sql.append("""
            GROUP BY n.cnpj_destinatario

            ORDER BY valor_total DESC
            """);


        List<AnaliseVendasDTO> lista =
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


            //==================================================
            // PARÂMETROS DA SUBCONSULTA
            //==================================================

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


            if (cnpjCliente != null
                    && !cnpjCliente.isBlank()) {

                ps.setString(
                        parametro++,
                        cnpjCliente
                );
            }


            if (codigoProduto != null
                    && !codigoProduto.isBlank()) {

                ps.setString(
                        parametro++,
                        codigoProduto
                );
            }


            //==================================================
            // PARÂMETROS DA CONSULTA PRINCIPAL
            //==================================================

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


            if (cnpjCliente != null
                    && !cnpjCliente.isBlank()) {

                ps.setString(
                        parametro++,
                        cnpjCliente
                );
            }


            if (codigoProduto != null
                    && !codigoProduto.isBlank()) {

                ps.setString(
                        parametro++,
                        codigoProduto
                );
            }


            //==================================================
            // EXECUÇÃO
            //==================================================

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    AnaliseVendasDTO dto =
                            new AnaliseVendasDTO();


                    dto.setCnpjCliente(
                            rs.getString(
                                    "cnpj_cliente"
                            )
                    );


                    dto.setCliente(
                            rs.getString(
                                    "cliente"
                            )
                    );


                    dto.setNotas(
                            rs.getInt(
                                    "notas"
                            )
                    );


                    //==================================================
                    // DATA DA ÚLTIMA VENDA
                    //==================================================

                    String dataUltimaVenda =
                            rs.getString(
                                    "data_ultima_venda"
                            );

                    if (dataUltimaVenda != null
                            && !dataUltimaVenda.isBlank()) {

                        dto.setDataUltimaVenda(
                                LocalDate.parse(
                                        dataUltimaVenda
                                )
                        );
                    }


                    dto.setQuantidade(
                            rs.getDouble(
                                    "quantidade"
                            )
                    );


                    dto.setValorTotal(
                            rs.getBigDecimal(
                                    "valor_total"
                            )
                    );


                    dto.setParticipacao(
                            rs.getBigDecimal(
                                    "participacao"
                            )
                    );


                    lista.add(dto);
                }
            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar análise de vendas.",
                    e
            );
        }


        return lista;
    }
}