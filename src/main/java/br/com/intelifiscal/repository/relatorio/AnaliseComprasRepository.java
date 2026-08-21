package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.AnaliseComprasDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnaliseComprasRepository {


    //==================================================
    // CONSULTAR ANÁLISE DE COMPRAS
    //==================================================

    public List<AnaliseComprasDTO> consultar(
            LocalDate dataInicio,
            LocalDate dataFim,
            String cnpjFornecedor,
            String codigoProduto
    ) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                n.cnpj_emitente AS cnpj_fornecedor,

                MAX(n.emitente) AS fornecedor,

                COUNT(DISTINCT n.id) AS notas,

                MAX(date(n.data_emissao)) AS data_ultima_compra,

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

                                WHERE n2.cnpj_emitente <> e2.cnpj
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
        // FILTRO DE FORNECEDOR - SUBCONSULTA
        //==================================================

        if (cnpjFornecedor != null
                && !cnpjFornecedor.isBlank()) {

            sql.append("""
                                AND n2.cnpj_emitente = ?
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

            WHERE n.cnpj_emitente <> e.cnpj
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
        // FILTRO DE FORNECEDOR - CONSULTA PRINCIPAL
        //==================================================

        if (cnpjFornecedor != null
                && !cnpjFornecedor.isBlank()) {

            sql.append("""
                AND n.cnpj_emitente = ?
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
            GROUP BY n.cnpj_emitente

            ORDER BY valor_total DESC
            """);


        List<AnaliseComprasDTO> lista =
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


            if (cnpjFornecedor != null
                    && !cnpjFornecedor.isBlank()) {

                ps.setString(
                        parametro++,
                        cnpjFornecedor
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


            if (cnpjFornecedor != null
                    && !cnpjFornecedor.isBlank()) {

                ps.setString(
                        parametro++,
                        cnpjFornecedor
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

                    AnaliseComprasDTO dto =
                            new AnaliseComprasDTO();


                    dto.setCnpjFornecedor(
                            rs.getString(
                                    "cnpj_fornecedor"
                            )
                    );


                    dto.setFornecedor(
                            rs.getString(
                                    "fornecedor"
                            )
                    );


                    dto.setNotas(
                            rs.getInt(
                                    "notas"
                            )
                    );


                    //==================================================
                    // DATA DA ÚLTIMA COMPRA
                    //==================================================

                    String dataUltimaCompra =
                            rs.getString(
                                    "data_ultima_compra"
                            );

                    if (dataUltimaCompra != null
                            && !dataUltimaCompra.isBlank()) {

                        dto.setDataUltimaCompra(
                                LocalDate.parse(
                                        dataUltimaCompra
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
                    "Erro ao consultar análise de compras.",
                    e
            );
        }


        return lista;
    }
}