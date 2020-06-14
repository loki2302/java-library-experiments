/*
 * This file is generated by jOOQ.
 */
package io.agibalov.db.tables;


import io.agibalov.db.DefaultSchema;
import io.agibalov.db.Keys;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Schools extends TableImpl<io.agibalov.db.tables.records.Schools> {

    private static final long serialVersionUID = 1626437711;

    /**
     * The reference instance of <code>Schools</code>
     */
    public static final Schools Schools = new Schools();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<io.agibalov.db.tables.records.Schools> getRecordType() {
        return io.agibalov.db.tables.records.Schools.class;
    }

    /**
     * The column <code>Schools.id</code>.
     */
    public final TableField<io.agibalov.db.tables.records.Schools, String> id = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>Schools.name</code>.
     */
    public final TableField<io.agibalov.db.tables.records.Schools, String> name = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * Create a <code>Schools</code> table reference
     */
    public Schools() {
        this(DSL.name("Schools"), null);
    }

    /**
     * Create an aliased <code>Schools</code> table reference
     */
    public Schools(String alias) {
        this(DSL.name(alias), Schools);
    }

    /**
     * Create an aliased <code>Schools</code> table reference
     */
    public Schools(Name alias) {
        this(alias, Schools);
    }

    private Schools(Name alias, Table<io.agibalov.db.tables.records.Schools> aliased) {
        this(alias, aliased, null);
    }

    private Schools(Name alias, Table<io.agibalov.db.tables.records.Schools> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Schools(Table<O> child, ForeignKey<O, io.agibalov.db.tables.records.Schools> key) {
        super(child, key, Schools);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<io.agibalov.db.tables.records.Schools> getPrimaryKey() {
        return Keys.KEY_Schools_PRIMARY;
    }

    @Override
    public List<UniqueKey<io.agibalov.db.tables.records.Schools>> getKeys() {
        return Arrays.<UniqueKey<io.agibalov.db.tables.records.Schools>>asList(Keys.KEY_Schools_PRIMARY);
    }

    @Override
    public Schools as(String alias) {
        return new Schools(DSL.name(alias), this);
    }

    @Override
    public Schools as(Name alias) {
        return new Schools(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Schools rename(String name) {
        return new Schools(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Schools rename(Name name) {
        return new Schools(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
