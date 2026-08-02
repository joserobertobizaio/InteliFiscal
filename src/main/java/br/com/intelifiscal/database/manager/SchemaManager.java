package br.com.intelifiscal.database.manager;

import br.com.intelifiscal.database.schema.SchemaDefinition;
import br.com.intelifiscal.database.schema.TblVersaoBancoSchema;
import br.com.intelifiscal.database.schema.TblMinhaEmpresaSchema;
import br.com.intelifiscal.database.schema.TblNFeSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsável por disponibilizar as definições dos schemas
 * do banco de dados.
 *
 * A ordem em que os schemas são adicionados representa
 * a ordem de criação das tabelas.
 *
 * Esta classe não executa SQL.
 *
 * @author José Roberto Bizaio
 */
public final class SchemaManager {

    private SchemaManager() {
    }

    /**
     * Retorna a lista de definições dos schemas.
     *
     * @return lista imutável contendo as definições.
     */
    public static List<SchemaDefinition> getSchemas() {

        List<SchemaDefinition> schemas = new ArrayList<>();

        schemas.add(TblVersaoBancoSchema.getSchema());

        schemas.add(TblMinhaEmpresaSchema.getSchema());

        schemas.add(TblNFeSchema.getSchema());

        return Collections.unmodifiableList(schemas);

    }

}