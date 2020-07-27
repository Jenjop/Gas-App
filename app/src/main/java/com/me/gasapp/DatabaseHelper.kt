package com.me.gasapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DatabaseHelper.Companion.DB_NAME,
        null,
        DatabaseHelper.Companion.DB_VERSION
    ) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DatabaseHelper.Companion.CREATE_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseHelper.Companion.TABLE_NAME)
        onCreate(db)
    }

    companion object {
        const val TABLE_NAME: String = "ENTRIES"

        const val _ID: String = "_id"
        const val DATE: String = "Date"
        const val DIST: String = "Distance"
        const val GAS: String = "Gas"

        const val DB_NAME: String = "ENTRIES.DB"

        const val DB_VERSION: Int = 1

        private val CREATE_TABLE: String = "create table $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DATE INTEGER NOT NULL, " +
                "$DIST REAL NOT NULL" +
                "$GAS REAL NOT NULL" +
                ");"
    }
}
