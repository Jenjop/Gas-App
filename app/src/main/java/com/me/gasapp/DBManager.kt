package com.me.gasapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException

import android.database.sqlite.SQLiteDatabase

//https://www.journaldev.com/9438/android-sqlite-database-example-tutorial
class DBManager(c: Context) {
    private var dbHelper: DatabaseHelper? = null
    private val context: Context = c
    private var database: SQLiteDatabase? = null

    @Throws(SQLException::class)
    fun open(): DBManager {
        dbHelper = DatabaseHelper(context)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(date: Long, dist: Double, gas: Double) {
        val contentValue = ContentValues()
        contentValue.put(DatabaseHelper.DATE, date)
        contentValue.put(DatabaseHelper.DIST, dist)
        contentValue.put(DatabaseHelper.GAS, gas)
        database!!.insert(DatabaseHelper.TABLE_NAME, null, contentValue)
    }

    fun fetch(): Cursor? {
        val columns =
            arrayOf(DatabaseHelper._ID, DatabaseHelper.DATE, DatabaseHelper.DIST, DatabaseHelper.GAS)
        val cursor: Cursor? =
            database!!.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null)
        cursor?.moveToFirst()
        return cursor
    }

    fun update(_id: Long, date: Long?, dist: Double?, gas: Double?): Int {
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.DATE, date)
        contentValues.put(DatabaseHelper.DIST, dist)
        contentValues.put(DatabaseHelper.GAS, gas)
        return database!!.update(
            DatabaseHelper.TABLE_NAME,
            contentValues,
            DatabaseHelper._ID + " = " + _id,
            null
        )
    }

    fun delete(_id: Long) {
        database!!.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null)
    }

}