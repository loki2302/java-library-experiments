/*
 * This file is generated by jOOQ.
 */
package io.agibalov.db;


import io.agibalov.db.routines.AddNumbers;

import org.jooq.Configuration;


/**
 * Convenience access to all stored procedures and functions in 
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Routines {

    /**
     * Call <code>AddNumbers</code>
     */
    public static void AddNumbers(Configuration configuration, Integer a, Integer b) {
        AddNumbers p = new AddNumbers();
        p.a(a);
        p.b(b);

        p.execute(configuration);
    }
}