package com.example.currencyconverter.db;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Handles SQLite database methods.
 *
 * @author Vladislav
 * @version 1.2
 * @since 1.2
 */
public final class DBHandler extends SQLiteOpenHelper {
    /**
     * A storage for set of values.
     */
    private static final ContentValues CONTENT_VALUES = new ContentValues();
    /**
     * A nullable column.
     */
    private static final String NULL = null;

    /**
     * Constructs a database handler.
     *
     * @param context an instantiator activity's context.
     *
     * @author Vladislav
     * @since 1.2
     */
    public DBHandler(Context context) {
        super(context, DBConfig.DATABASE, null, 1);
    }

    /**
     * Creates a new table and inserts default values into the created table.
     *
     * @param database a database to populate.
     *
     * @author Vladislav
     * @since 1.2
     */
    @Override
    public final void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + DBConfig.RATE + "(" + DBConfig.ID +
                " INTEGER PRIMARY KEY NOT NULL, " + DBConfig.JSON + " TEXT NOT NULL, " +
                DBConfig.TIMESTAMP + " INTEGER NOT NULL)");
        CONTENT_VALUES.put(DBConfig.ID, 1);
        CONTENT_VALUES.put(DBConfig.JSON, "{\"RUB\":1,\"USD\":1," + "\"EUR\":1,\"JPY\":1," +
                "\"AUD\":1,\"AZN\":1,\"GBP\":1,\"AMD\":1,\"BYN\":1," + "\"BGN\":1,\"KZT\":1," +
                "\"CNY\":1,\"INR\":1}}");
        CONTENT_VALUES.put(DBConfig.TIMESTAMP, 0);
        database.insert(DBConfig.RATE, NULL, CONTENT_VALUES);
        database.close();
    }

    /**
     * Drops a created table from {@code database} and repopulates cleared {@code database}.
     *
     * @param database a database to drop the created table.
     * @param oldVersion an old version of {@code database}.
     * @param newVersion a new version number of {@code database}.
     *
     * @author Vladislav
     * @since 1.2
     */
    @Override
    public final void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DBConfig.RATE);
        onCreate(database);
    }
}
