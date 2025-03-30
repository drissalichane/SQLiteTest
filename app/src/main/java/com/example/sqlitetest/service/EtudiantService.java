package com.example.sqlitetest.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sqlitetest.classes.Etudiant;
import com.example.sqlitetest.util.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class EtudiantService {
    private static final String TABLE_NAME = "etudiant";

    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_DATE_NAISSANCE = "date_naissance"; // Field for date of birth
    private static final String KEY_IMAGE_PATH = "image_path"; // Field for image path

    private static String[] COLUMNS = {KEY_ID, KEY_NOM, KEY_PRENOM, KEY_DATE_NAISSANCE, KEY_IMAGE_PATH};

    private MySQLiteHelper helper;

    public EtudiantService(Context context) {
        this.helper = new MySQLiteHelper(context);
    }

    public void create(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, e.getNom());
        values.put(KEY_PRENOM, e.getPrenom());
        values.put(KEY_DATE_NAISSANCE, e.getDateNaissance());

        // Ensure we store the URI string as-is
        values.put(KEY_IMAGE_PATH, e.getImagePath());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void update(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, e.getId());
        values.put(KEY_NOM, e.getNom());
        values.put(KEY_PRENOM, e.getPrenom());
        values.put(KEY_DATE_NAISSANCE, e.getDateNaissance());
        values.put(KEY_IMAGE_PATH, e.getImagePath());

        db.update(TABLE_NAME,
                values,
                "id = ?",
                new String[]{String.valueOf(e.getId())});
        db.close();
    }

    public Etudiant findById(int id) {
        Etudiant e = null;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c;
        c = db.query(TABLE_NAME,
                COLUMNS,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        if (c.moveToFirst()) {
            e = new Etudiant();
            e.setId(c.getInt(0));
            e.setNom(c.getString(1));
            e.setPrenom(c.getString(2));
            e.setDateNaissance(c.getString(3)); // Set date of birth
            e.setImagePath(c.getString(4)); // Set image path
        }

        db.close();
        return e;
    }

    public void delete(Etudiant e) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        db.delete(TABLE_NAME,
                "id = ?",
                new String[]{String.valueOf(e.getId())});
        db.close();
    }

    public List<Etudiant> findAll() {
        List<Etudiant> eds = new ArrayList<>();
        String req = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor c = null;

        try {
            c = db.rawQuery(req, null);
            if (c != null && c.moveToFirst()) {
                do {
                    Etudiant e = new Etudiant();
                    e.setId(c.getInt(0));
                    e.setNom(c.getString(1));
                    e.setPrenom(c.getString(2));
                    e.setDateNaissance(c.getString(3)); // Set date of birth
                    e.setImagePath(c.getString(4)); // Set image path
                    eds.add(e);
                    Log.d("Etudiant", "ID = " + e.getId() + ", Nom = " + e.getNom());
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("DB_ERROR", "Error fetching students: " + ex.getMessage());
        } finally {
            if (c != null) {
                c.close(); // Close cursor after use
            }
            db.close(); // Close database after finishing work
        }

        return eds;
    }

}
