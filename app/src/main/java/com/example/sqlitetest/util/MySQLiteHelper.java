// Updated MySQLiteHelper class to include date and image fields
package com.example.sqlitetest.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ecole";

    private static final String CREATE_TABLE_ETUDIANT = "CREATE TABLE etudiant (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nom TEXT," +
            "prenom TEXT," +
            "date_naissance TEXT," +
            "image_path TEXT)";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ETUDIANT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS etudiant");
        this.onCreate(db);
    }
}
