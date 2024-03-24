package com.example.practical_9_sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    MyDB mdb;

    EditText userName, password;

    Button bregister, bdrop, blogin;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.txtusername);
        password = findViewById(R.id.txtpass);
        blogin = findViewById(R.id.login);
        bregister = findViewById(R.id.register);
        bdrop = findViewById(R.id.drop);

        mdb = new MyDB(this);
    }

    public void login_fun(View v) {
        String un = userName.getText().toString();
        String pas = password.getText().toString();

        db = mdb.getReadableDatabase();

        String q = "SELECT * FROM login WHERE username = ? AND password = ?";
        try {
            Cursor c = db.rawQuery(q, new String[]{un, pas});

            if (c.moveToFirst()) {
                int usernameIndex = c.getColumnIndex("username");
                int passwordIndex = c.getColumnIndex("password");

                if (usernameIndex >= 0 && passwordIndex >= 0) {
                    String uname = c.getString(usernameIndex);
                    String passw = c.getString(passwordIndex);

                    Toast.makeText(getApplicationContext(), "Username: " + uname + "\nPassword: " + passw, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Welcome user: " + un, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid cursor column index", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_SHORT).show();
            }
            c.close();
        } catch (SQLiteException sqle) {
            sqle.printStackTrace();
        }
    }

    public void register_fun(View v) {
        String un = userName.getText().toString();
        String pas = password.getText().toString();

        db = mdb.getWritableDatabase();

        String q = "INSERT INTO login (username, password) VALUES ('" + un + "', '" + pas + "')";

        db.execSQL(q);

        Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();
    }

    public void drop_fun(View v) {
        db = mdb.getWritableDatabase();
        mdb.onUpgrade(db, 1, 2);
        Toast.makeText(getApplicationContext(), "All Users Deleted", Toast.LENGTH_SHORT).show();
    }
}
