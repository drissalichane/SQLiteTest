package com.example.sqlitetest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sqlitetest.classes.Etudiant;
import com.example.sqlitetest.service.EtudiantService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText nom, prenom, id;
    private Button add, rechercher, viewStudents, pickDate, pickImage;
    private TextView res, selectedDate;
    private ImageView selectedImage;
    private EtudiantService es;
    private String imagePath;

    private static final int PICK_IMAGE_REQUEST = 1;

    void clear() {
        nom.setText("");
        prenom.setText("");
        selectedDate.setText("Sélectionner une date");
        selectedImage.setImageBitmap(null);
        imagePath = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        es = new EtudiantService(this);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        id = findViewById(R.id.id);
        add = findViewById(R.id.bn);
        rechercher = findViewById(R.id.load);
        viewStudents = findViewById(R.id.view_students);
        res = findViewById(R.id.res);
        pickDate = findViewById(R.id.pick_date);
        pickImage = findViewById(R.id.pick_image);
        selectedDate = findViewById(R.id.selected_date);
        selectedImage = findViewById(R.id.selected_image);

        pickDate.setOnClickListener(v -> showDatePicker());

        pickImage.setOnClickListener(v -> openGallery());

        add.setOnClickListener(v -> {
            if (nom.getText().toString().isEmpty() || prenom.getText().toString().isEmpty() || selectedDate.getText().toString().equals("Sélectionner une date") || imagePath == null) {
                Toast.makeText(MainActivity.this, "Veuillez compléter tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                es.create(new Etudiant(nom.getText().toString(), prenom.getText().toString(), selectedDate.getText().toString(), imagePath));
                clear();
                Toast.makeText(MainActivity.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        rechercher.setOnClickListener(v -> {
            String studentId = id.getText().toString();
            if (studentId.isEmpty()) {
                Toast.makeText(MainActivity.this, "Veuillez entrer un ID", Toast.LENGTH_SHORT).show();
            } else {
                Etudiant e = es.findById(Integer.parseInt(studentId));
                if (e != null) {
                    res.setText(e.getNom() + " " + e.getPrenom() + "\n" + e.getDateNaissance() + "\n" + (e.getImagePath() != null ? "Image disponible" : "Aucune image"));
                } else {
                    Toast.makeText(MainActivity.this, "Étudiant introuvable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewStudents.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
            startActivity(intent);
            Log.e("viewStudents","it reached here");
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            selectedDate.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File directory = new File(getFilesDir(), "student_images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, System.currentTimeMillis() + ".jpg");

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // In your MainActivity's onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Take persistent permission
            try {
                getContentResolver().takePersistableUriPermission(
                        selectedImageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                // Store the URI string directly
                imagePath = selectedImageUri.toString();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    selectedImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(index);
            cursor.close();
            return path;
        }
    }
}
