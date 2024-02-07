package com.example.chatbien;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatbien.objetos.DAO_empleados;

public class menu_login extends AppCompatActivity {
    private ProgressBar progressBar;
    private CheckBox cbRecordar;
    private TextView etUsuario,etPaswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_login);
        progressBar = findViewById(R.id.progressBar);
        etPaswd= findViewById(R.id.etPaswd);
        etUsuario  = findViewById(R.id.etUsuario);
        cbRecordar= findViewById(R.id.cbRecordar);
        cargarCampos();
    }
    private void recordarPasword(){
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "BD_Usuario", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(cbRecordar.isChecked()){
            db.execSQL("DELETE FROM Usuario_Temp");
            db.execSQL("INSERT INTO Usuario_Temp (usuario, pasw) VALUES ('"+etUsuario.getText().toString()+"','"+etPaswd.getText().toString() +"')");
        }else if(!cbRecordar.isChecked()){
            db.execSQL("DELETE FROM Usuario_Temp");

        }
    }
    private void cargarCampos(){
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "BD_Usuario", null, 1);
        SQLiteDatabase bd = dbHelper.getWritableDatabase();
        Cursor cursor = bd.rawQuery("select * from Usuario_Temp ", null);
        if (cursor.moveToFirst()) {
            do {
                etUsuario.setText(cursor.getString(0));
                etPaswd.setText(cursor.getString(1));
                cbRecordar.setChecked(true);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No hay para recordar",
                    Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        bd.close();
    }
    public void conectr(View v) {
        progressBar.setVisibility(View.VISIBLE);
        recordarPasword();
        DAO_empleados empleados = new DAO_empleados();
        if(empleados.validarCodigo(Integer.parseInt(etUsuario.getText().toString()))){
            if(empleados.login(etPaswd.getText().toString(),Integer.parseInt(etUsuario.getText().toString()))){
                Intent i = new Intent(this, menu_chat.class);
                i.putExtra("dni", etUsuario.getText().toString());
                startActivity(i);
                finish();
                progressBar.setVisibility(View.INVISIBLE);
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Contraseña incorrecto")
                        .setMessage("La contraseña no es correcta")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                etPaswd.setText("");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();            }
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Usuario incorrecto")
                    .setMessage("No existe ningun usuario con ese dni")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            etUsuario.setText("");
                            etPaswd.setText("");
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();                    }


    }
}