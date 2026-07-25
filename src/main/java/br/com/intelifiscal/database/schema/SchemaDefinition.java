package br.com.intelifiscal.database.schema;

import java.util.List;

/**
 * Representa a definição de um schema do banco de dados.
 *
 * Cada definição contém:
 * - o nome do schema;
 * * - a lista de scripts SQL necessários para sua criação.
 *
 * @param name nome do schema
 * @param scripts lista de scripts SQL
 *
 * @author José Roberto Bizaio
 */
public record SchemaDefinition(
        String name,
        List<String> scripts
) {
}