package br.com.intelifiscal.database.schema;

/**
 * Representa a definição de um schema do banco de dados.
 *
 * Cada definição contém:
 * - o nome da tabela ou objeto;
 * - o script SQL responsável por sua criação.
 *
 * @param name nome do schema
 * @param sql script SQL
 *
 * @author José Roberto Bizaio
 */
public record SchemaDefinition(
        String name,
        String sql
) {
}