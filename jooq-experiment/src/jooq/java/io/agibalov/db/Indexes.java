/*
 * This file is generated by jOOQ.
 */
package io.agibalov.db;


import io.agibalov.db.tables.Students;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code></code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index schoolId = Indexes0.schoolId;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index schoolId = Internal.createIndex("schoolId", Students.Students, new OrderField[] { Students.Students.schoolId }, false);
    }
}
