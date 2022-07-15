package com.example.currencyconverter.db;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
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
     * Null selection arguments.
     */
    private static final String[] NULL_SELECTIONS = null;
    /**
     * Identity number of the first table row.
     */
    private static final int ID = 1;

    /**
     * Creates a new database handler with an instantiator activity context.
     *
     * @param context the instantiator activity context.
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
        database.execSQL("CREATE TABLE " + DBConfig.RESPONSE + "(" + DBConfig.ID +
                " INTEGER PRIMARY KEY NOT NULL, " + DBConfig.JSON + " TEXT NOT NULL, " +
                DBConfig.TIMESTAMP + " INTEGER NOT NULL)");
        CONTENT_VALUES.put(DBConfig.ID, ID);
        CONTENT_VALUES.put(DBConfig.JSON, "{\"RUB\":1,\"USD\":1,\"EUR\":1,\"JPY\":1," +
                "\"AUD\":1,\"AZN\":1,\"GBP\":1,\"AMD\":1,\"BYN\":1,\"BGN\":1,\"KZT\":1," +
                "\"CNY\":1,\"INR\":1}");
        CONTENT_VALUES.put(DBConfig.TIMESTAMP, 0);
        database.insert(DBConfig.RESPONSE, null, CONTENT_VALUES);
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
        database.execSQL("DROP TABLE IF EXISTS " + DBConfig.RESPONSE);
        onCreate(database);
    }

    /**
     * Gets a latest saved json response.
     *
     * @return the latest saved json response.
     *
     * @author Vladislav
     * @since 1.2
     */
    public final String getJson() {
        String result;
        final Cursor cursor = getReadableDatabase().rawQuery("SELECT " + DBConfig.JSON +
                " FROM " + DBConfig.RESPONSE + " WHERE " + DBConfig.ID + " = " + ID,
                NULL_SELECTIONS);

        cursor.moveToFirst();

        result = cursor.getString(0);

        cursor.close();
        System.out.println("RESULT:" + result);

        return result;
    }

    /**
     * Gets a latest saved json response's timestamp.
     *
     * @return the latest saved json response's timestamp.
     *
     * @author Vladislav
     * @since 1.2
     */
    public final long getTimestamp() {
        long result;
        final Cursor cursor = getReadableDatabase().rawQuery("SELECT " + DBConfig.TIMESTAMP +
                        " FROM " + DBConfig.RESPONSE + " WHERE " + DBConfig.ID + " = " + ID,
                NULL_SELECTIONS);

        cursor.moveToFirst();

        result = cursor.getLong(0);

        cursor.close();

        return result;
    }

    /**
     * Updates a latest saved json response.
     *
     * @param json the latest json response.
     * @param timestamp the latest json response's timestamp.
     *
     * @author Vladislav
     * @since 1.2
     */
    public final void updateResponse(String json, long timestamp) {
        final long hour = 3600000;
        final String whereClause = DBConfig.ID + " = ?";
        final String[] idStatement = {"1"};

        CONTENT_VALUES.put(DBConfig.JSON, json);
        CONTENT_VALUES.put(DBConfig.TIMESTAMP, timestamp + hour);
        getWritableDatabase().update(DBConfig.RESPONSE, CONTENT_VALUES, whereClause, idStatement);
        CONTENT_VALUES.clear();
    }
}
