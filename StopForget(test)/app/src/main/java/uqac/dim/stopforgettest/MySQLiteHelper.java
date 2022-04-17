package uqac.dim.stopforgettest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "listes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LISTES="listes";
    private static final String TABLE_ELEMENTS="elements";

    private static final String ID= "id";
    private static final String LIST_ELEM="list of elem";

    private static final String CREATE_LISTES= "create table "
            + TABLE_LISTES + "(" + ID
            + "integer primary key autoincrement, " + LIST_ELEM
            + "array of string)";

    private static final String CREATE_ELEMENTS= "create table "
            + TABLE_ELEMENTS + "(" + ID
            + "integer primary key autoincrement, " + LIST_ELEM
            + "array of Element)";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ELEMENTS);
        db.execSQL(CREATE_LISTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LISTES);
        onCreate(db);
    }
}
