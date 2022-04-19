package uqac.dim.stopforgettest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "listes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LISTES="listes";

    private static final String ID= "id";
    private static final String NAME ="list_elem";
    private static final String PARENT_ID="parent";
    private static final String NB="nb";
    private static final String NBC="nbc";
    private static final String TYPE="type";
    private static final String CHECKED="checked";
    private static final String ANCETRE_ID="ancetre_id";

    private static final String CREATE_LISTES= "create table "
            + TABLE_LISTES + "(" + ID
            + "integer primary key autoincrement, " + NAME
            + "text not null, " + PARENT_ID + "integer, " + NB + "integer, "
            + NBC + "integer, " + TYPE + "integer, " + ANCETRE_ID + "integer, "
            + CHECKED + "boolean) " ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public void adList(Liste l){
        SQLiteDatabase db=getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(MySQLiteHelper.NAME,l.name);
        values.put(MySQLiteHelper.NB,l.nb);
        values.put(MySQLiteHelper.NBC,l.nbc);
        values.put(MySQLiteHelper.PARENT_ID,-1);
        values.put(MySQLiteHelper.ANCETRE_ID,-1);
        values.put(MySQLiteHelper.TYPE,2);

        l.setId(db.insert(TABLE_LISTES,null,values));
    }

    public Liste getList(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        String selectquery="SELECT * FROM " + TABLE_LISTES
                + " WHERE " + ID + " = " + id;

        Cursor c=db.rawQuery(selectquery,null);
        if (c!=null)
            c.moveToFirst();
        else
            return null;

        @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
        @SuppressLint("Range") String nb=c.getString(c.getColumnIndex(NB));
        @SuppressLint("Range") String nbc=c.getString(c.getColumnIndex(NBC));

        Liste l=new Liste(name);
        l.nb=Integer.parseInt(nb);
        l.nbc=Integer.parseInt(nbc);
        l.setId(id);

        return l;
    }

    public void adElement(Element e){
        SQLiteDatabase db=getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(MySQLiteHelper.NAME,e.name);
        values.put(MySQLiteHelper.CHECKED,e.checked);
        values.put(MySQLiteHelper.ANCETRE_ID,e.ancetre.getId());
        if (e.parent==null)
            values.put(MySQLiteHelper.PARENT_ID,e.ancetre.getId());
        else
            values.put(MySQLiteHelper.PARENT_ID,e.parent.getId());
        if (e.type== Element.Type.ITEM)
            values.put(MySQLiteHelper.TYPE,0);
        else {
            values.put(MySQLiteHelper.TYPE,1);
            values.put(MySQLiteHelper.NB,((SousListe) e).nb);
            values.put(MySQLiteHelper.NBC,((SousListe) e).nbc);
        }

        e.setId(db.insert(TABLE_LISTES,null,values));
    }

    public Element getElement(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        String selcectquery="SELECT * FROM " + TABLE_LISTES
                + " WHERE " + ID + " = " + id;

        Cursor c=db.rawQuery(selcectquery,null);
        if (c!=null)
            c.moveToFirst();
        else
            return null;

        @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
        @SuppressLint("Range") String nb=c.getString(c.getColumnIndex(NB));
        @SuppressLint("Range") String nbc=c.getString(c.getColumnIndex(NBC));
        @SuppressLint("Range") String parent_id=c.getString(c.getColumnIndex(PARENT_ID));
        @SuppressLint("Range") String type=c.getString(c.getColumnIndex(TYPE));
        @SuppressLint("Range") String checked=c.getString(c.getColumnIndex(CHECKED));
        @SuppressLint("Range") String ancetre_id=c.getString(c.getColumnIndex(ANCETRE_ID));
        if (type=="0"){
            Item item;
            if (parent_id=="-1")
                item=new Item(name,null,getList(Integer.parseInt(ancetre_id)));
            else
                item=new Item(name,(SousListe)getElement(Integer.parseInt(parent_id)),getList(Integer.parseInt(ancetre_id)));
            item.checked=Boolean.getBoolean(checked);
            item.type=Element.Type.ITEM;
            return item;
        }
        else{
            SousListe sousListe;
            if (parent_id=="-1")
                sousListe=new SousListe(name,null,getList(Integer.parseInt(ancetre_id)));
            else
                sousListe=new SousListe(name,(SousListe)getElement(Integer.parseInt(parent_id)),getList(Integer.parseInt(ancetre_id)));
            sousListe.checked=Boolean.getBoolean(checked);
            sousListe.type=Element.Type.SOUSLISTE;
            sousListe.nb=Integer.parseInt(nb);
            sousListe.nbc=Integer.parseInt(nbc);
            return sousListe;
        }
    }
}
