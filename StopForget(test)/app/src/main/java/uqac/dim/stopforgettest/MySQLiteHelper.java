package uqac.dim.stopforgettest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "listes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_LISTES="listes";

    public static final String ID= "id";
    public static final String NAME ="name";
    public static final String PARENT_ID="parent";
    public static final String NB="nb";
    public static final String NBC="nbc";
    public static final String TYPE="type";
    public static final String CHECKED="checked";
    public static final String ANCETRE_ID="ancetre_id";


    private static final String CREATE_LISTES= " create table "
            + TABLE_LISTES + " ( " + ID
            + " integer primary key autoincrement, " + NAME
            + " text not null, " + PARENT_ID + " integer, " + NB + " integer, "
            + NBC + " integer, " + TYPE + " integer, " + ANCETRE_ID + " integer, "
            + CHECKED + " integer ) " ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LISTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTES);
        onCreate(db);
    }
}
