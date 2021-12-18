package com.example.basicactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    /*
    Constantes para la BD, tabla y columnas
    **/
    public static final String DB_NAME = "NameDB";
    public static final String TABLE_NAME = "names";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATE = "status";

    //version BD
    public static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*Creación de la base de datos*/
        String sql = " CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " VARCHAR, "
                + COLUMN_STATE + " TINYINT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    /*
    *metodo para guargar los nombres e indicar su estado
    * 0 sincronizado con el server
    * 1 no sincronizado con el server
    * */
    public boolean addNombre (String name, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_STATE,status);

        db.insert(TABLE_NAME,null, contentValues);
        db.close();

        return true;
    }

    /*El siguiente método toma el id del registro para
    luego actualizar es estado de sincronizacion con el campo estado */
    public boolean updateNameStatus (int id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATE, status);
        db.update(TABLE_NAME,contentValues,COLUMN_ID + "=" + id,null);
        db.close();
        return true;
    }
    /*Este método nos dara todos los nombres alamcenados localmente */
    public Cursor getNames(){
        SQLiteDatabase db=this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME + " ASC;";
        Cursor c  = db.rawQuery(sql,null);
        return c;
    }

    /*El sigueinte metodo trea los registros no sincronizados*/
    public Cursor getUnsynceNames(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATE + " = 0;";
        Cursor c = db.rawQuery(sql,null);
        return  c;
    }

    public boolean addName(String name, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_STATE, status);


        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

}
