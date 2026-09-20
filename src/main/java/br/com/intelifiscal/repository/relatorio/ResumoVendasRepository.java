package br.com.intelifiscal.repository.relatorio;

import br.com.intelifiscal.database.connection.DatabaseConnection;
import br.com.intelifiscal.dto.relatorio.ResumoVendasDTO;
import br.com.intelifiscal.dto.relatorio.ClienteVendaDTO;
import br.com.intelifiscal.dto.venda.ResumoVendaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumoVendasRepository {

    //==================================================
    // RESUMO GERAL DE VENDAS
    //==================================================

    public ResumoVendasDTO consultarResumo() {

        return consultarResumo(null, null);
    }


    //==================================================
    // RESUMO POR PERÍODO
    //==================================================

    public ResumoVendasDTO consultarResumo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
        WITH notas_filtradas AS (

            SELECT DISTINCT
                n.id,
                n.valor_total

            FROM tblNFe n

            INNER JOIN tblMinhaEmpresa e
                ON e.cnpj = n.cnpj_emitente
               AND e.ativo = 1

            WHERE n.tipo = 'Venda'
              AND n.situacao <> 'CANCELADA'

              AND EXISTS (
                  SELECT 1
                  FROM tblNFeItem i
                  WHERE i.id_nfe = n.id
              )

              AND NOT EXISTS (
                  SELECT 1
                  FROM tblNFeItem i
                  WHERE i.id_nfe = n.id
                    AND (
                        i.cfop IS NULL
                        OR i.cfop NOT IN (
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

            ROUND(
                COALESCE(
                    SUM(nf.valor_total),
                    0
                )
                /
                NULLIF(
                    COUNT(nf.id),
                    0
                ),
                2
            ) AS ticket_medio

        FROM notas_filtradas nf

        LEFT JOIN itens_por_nota ipn
            ON ipn.id_nfe = nf.id
        """);

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
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar resumo de vendas.",
                    e
            );
        }
    }


    //==================================================
    // VENDAS POR CLIENTE
    //==================================================

    public List<ClienteVendaDTO> consultarPorCliente() {

        return consultarPorCliente(null, null);
    }


    //==================================================
    // VENDAS POR CLIENTE - COM PERÍODO
    //==================================================

    public List<ClienteVendaDTO> consultarPorCliente(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        String sql = """
        WITH notas_filtradas AS (

            SELECT DISTINCT
                n.id,
                n.cnpj_destinatario,
                n.destinatario,
                n.data_emissao,
                n.valor_total

            FROM tblNFe n

            INNER JOIN tblMinhaEmpresa e
                ON e.cnpj = n.cnpj_emitente
               AND e.ativo = 1

            WHERE n.tipo = 'Venda'
              AND n.situacao <> 'CANCELADA'

              AND EXISTS (
                  SELECT 1
                  FROM tblNFeItem i
                  WHERE i.id_nfe = n.id
              )

              AND NOT EXISTS (
                  SELECT 1
                  FROM tblNFeItem i
                  WHERE i.id_nfe = n.id
                    AND (
                        i.cfop IS NULL
                        OR i.cfop NOT IN (
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

              AND date(n.data_emissao)
                  BETWEEN date(?) AND date(?)
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

            nf.cnpj_destinatario AS cnpj,

            MAX(nf.destinatario) AS cliente,

            COUNT(nf.id) AS notas,

            MAX(
                date(nf.data_emissao)
            ) AS data_ultima_venda,

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
            ) AS valor_total

        FROM notas_filtradas nf

        LEFT JOIN itens_por_nota ipn
            ON ipn.id_nfe = nf.id

        GROUP BY
            nf.cnpj_destinatario

        ORDER BY
            valor_total DESC
        """;

        List<ClienteVendaDTO> lista =
                new ArrayList<>();

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    dataInicio.toString()
            );

            ps.setString(
                    2,
                    dataFim.toString()
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    ClienteVendaDTO dto =
                            new ClienteVendaDTO();

                    dto.setCnpj(
                            rs.getString("cnpj")
                    );

                    dto.setCliente(
                            rs.getString("cliente")
                    );

                    dto.setNotas(
                            rs.getInt("notas")
                    );

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
                            rs.getDouble("quantidade")
                    );

                    dto.setValorTotal(
                            rs.getBigDecimal(
                                    "valor_total"
                            )
                    );

                    lista.add(dto);
                }
            }

            return lista;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar vendas por cliente.",
                    e
            );
        }
    }


    //==================================================
    // VENDAS PARA EXPORTAÇÃO
    //==================================================

    public List<ResumoVendaDTO> consultarVendasParaExportacao(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        StringBuilder sql = new StringBuilder("""
        SELECT
            n.numero AS nf,
            n.data_emissao,
            n.destinatario,
            n.cnpj_destinatario,

            CASE
                WHEN ROW_NUMBER() OVER (
                    PARTITION BY n.id
                    ORDER BY i.numero_item
                ) = 1
                THEN n.valor_total
                ELSE NULL
            END AS valor_total_nf,

            i.codigo_produto AS codigo_item,
            i.descricao AS descricao_item,
            i.cfop,
            i.quantidade,
            i.unidade AS unidade,
            i.valor_unitario

        FROM tblNFe n

        INNER JOIN tblMinhaEmpresa e
            ON e.cnpj = n.cnpj_emitente
           AND e.ativo = 1

        INNER JOIN tblNFeItem i
            ON i.id_nfe = n.id

        WHERE n.tipo = 'Venda'
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


        //==================================================
        // ORDENAÇÃO
        //==================================================

        sql.append("""
        
        ORDER BY
            date(n.data_emissao),
            CAST(n.numero AS INTEGER),
            i.numero_item
        """);


        List<ResumoVendaDTO> lista =
                new ArrayList<>();


        //==================================================
        // EXECUÇÃO
        //==================================================

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql.toString()
                        )
        ) {

            //==================================================
            // PARÂMETROS
            //==================================================

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


            //==================================================
            // RESULTADO
            //==================================================

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    ResumoVendaDTO dto =
                            new ResumoVendaDTO();


                    dto.setNf(
                            rs.getString("nf")
                    );


                    dto.setDataEmissao(
                            rs.getString("data_emissao")
                    );


                    dto.setDestinatario(
                            rs.getString("destinatario")
                    );


                    dto.setCnpjDestinatario(
                            rs.getString("cnpj_destinatario")
                    );


                    dto.setValorTotalNF(
                            rs.getBigDecimal(
                                    "valor_total_nf"
                            )
                    );


                    dto.setCodigoItem(
                            rs.getString("codigo_item")
                    );


                    dto.setDescricaoItem(
                            rs.getString("descricao_item")
                    );


                    dto.setCfop(
                            rs.getString("cfop")
                    );


                    dto.setQuantidade(
                            rs.getBigDecimal(
                                    "quantidade"
                            )
                    );


                    dto.setUnidade(
                            rs.getString(
                                    "unidade"
                            )
                    );


                    dto.setValorUnitario(
                            rs.getBigDecimal(
                                    "valor_unitario"
                            )
                    );


                    lista.add(dto);
                }
            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar vendas para exportação.",
                    e
            );
        }


        return lista;
    }


    //==================================================
    // DETALHAMENTO DE VENDAS POR CLIENTE
    //==================================================

    public List<br.com.intelifiscal.dto.relatorio.DetalhamentoVendaDTO>
    consultarDetalhamentoPorCliente(
            LocalDate dataInicio,
            LocalDate dataFim,
            String cnpjCliente
    ) {

        StringBuilder sql = new StringBuilder("""
            SELECT
                n.cnpj_destinatario AS cnpj,
                n.id AS id_nfe,
                n.destinatario AS cliente,
                n.numero AS numero_nota,
                n.data_emissao AS data_venda,
                n.municipio_destinatario AS municipio_cliente,
                n.uf_destinatario AS uf_cliente,

                i.numero_item,
                i.codigo_produto AS codigo_produto,
                i.descricao AS produto,
                i.cfop,
                i.quantidade,
                i.unidade,
                i.valor_unitario,
                i.valor_total,

                n.valor_total AS valor_total_nf

            FROM tblNFe n

            INNER JOIN tblMinhaEmpresa e
                ON e.cnpj = n.cnpj_emitente
               AND e.ativo = 1

            INNER JOIN tblNFeItem i
                ON i.id_nfe = n.id

            WHERE n.tipo = 'Venda'
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

              AND n.cnpj_destinatario = ?
            """);

        //==================================================
        // FILTRO DE PERÍODO
        //==================================================

        if (dataInicio != null && dataFim != null) {

            sql.append("""
                
                AND date(n.data_emissao)
                    BETWEEN date(?) AND date(?)
                """);

        } else if (dataInicio != null) {

            sql.append("""
                
                AND date(n.data_emissao) >= date(?)
                """);

        } else if (dataFim != null) {

            sql.append("""
                
                AND date(n.data_emissao) <= date(?)
                """);
        }


        //==================================================
        // ORDENAÇÃO
        //==================================================

        sql.append("""
            
            ORDER BY
                date(n.data_emissao),
                CAST(n.numero AS INTEGER),
                i.numero_item
            """);


        List<br.com.intelifiscal.dto.relatorio.DetalhamentoVendaDTO> lista =
                new ArrayList<>();


        //==================================================
        // EXECUÇÃO
        //==================================================

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(
                                sql.toString()
                        )
        ) {

            //==================================================
            // PARÂMETROS
            //==================================================

            int parametro = 1;

            ps.setString(
                    parametro++,
                    cnpjCliente
            );

            if (dataInicio != null && dataFim != null) {

                ps.setString(
                        parametro++,
                        dataInicio.toString()
                );

                ps.setString(
                        parametro,
                        dataFim.toString()
                );

            } else if (dataInicio != null) {

                ps.setString(
                        parametro,
                        dataInicio.toString()
                );

            } else if (dataFim != null) {

                ps.setString(
                        parametro,
                        dataFim.toString()
                );
            }


            //==================================================
            // RESULTADO
            //==================================================

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    br.com.intelifiscal.dto.relatorio.DetalhamentoVendaDTO dto =
                            new br.com.intelifiscal.dto.relatorio.DetalhamentoVendaDTO();


                    dto.setCnpj(
                            rs.getString("cnpj")
                    );


                    dto.setIdNfe(
                            rs.getLong("id_nfe")
                    );


                    dto.setCliente(
                            rs.getString("cliente")
                    );


                    dto.setNumeroNota(
                            rs.getString("numero_nota")
                    );


                    String dataVenda =
                            rs.getString("data_venda");

                    if (dataVenda != null
                            && !dataVenda.isBlank()) {

                        if (dataVenda.length() >= 10) {

                            dto.setDataVenda(
                                    LocalDate.parse(
                                            dataVenda.substring(0, 10)
                                    )
                            );
                        }
                    }


                    dto.setMunicipioCliente(
                            rs.getString("municipio_cliente")
                    );


                    dto.setUfCliente(
                            rs.getString("uf_cliente")
                    );


                    dto.setNumeroItem(
                            rs.getInt("numero_item")
                    );


                    dto.setCodigoProduto(
                            rs.getString("codigo_produto")
                    );


                    dto.setProduto(
                            rs.getString("produto")
                    );


                    dto.setCfop(
                            rs.getString("cfop")
                    );


                    dto.setQuantidade(
                            rs.getBigDecimal("quantidade")
                    );


                    dto.setUnidade(
                            rs.getString("unidade")
                    );


                    dto.setValorUnitario(
                            rs.getBigDecimal("valor_unitario")
                    );


                    dto.setValorTotal(
                            rs.getBigDecimal("valor_total")
                    );


                    dto.setValorTotalNF(
                            rs.getBigDecimal("valor_total_nf")
                    );


                    lista.add(dto);
                }
            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Erro ao consultar detalhamento de vendas por cliente.",
                    e
            );
        }

        return lista;
    }

}