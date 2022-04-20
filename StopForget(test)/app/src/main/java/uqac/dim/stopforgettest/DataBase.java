package uqac.dim.stopforgettest;

import static uqac.dim.stopforgettest.MySQLiteHelper.ANCETRE_ID;
import static uqac.dim.stopforgettest.MySQLiteHelper.CHECKED;
import static uqac.dim.stopforgettest.MySQLiteHelper.ID;
import static uqac.dim.stopforgettest.MySQLiteHelper.NAME;
import static uqac.dim.stopforgettest.MySQLiteHelper.NB;
import static uqac.dim.stopforgettest.MySQLiteHelper.NBC;
import static uqac.dim.stopforgettest.MySQLiteHelper.PARENT_ID;
import static uqac.dim.stopforgettest.MySQLiteHelper.TABLE_LISTES;
import static uqac.dim.stopforgettest.MySQLiteHelper.TYPE;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DataBase {

    private MySQLiteHelper dbHelper;
    private SQLiteDatabase database;
    private static final String[] allColums={ID,NAME,PARENT_ID,NB,NBC,TYPE,CHECKED,ANCETRE_ID};

    public DataBase(Context context){dbHelper=new MySQLiteHelper(context);}

    public void open() throws SQLException{
        database=dbHelper.getWritableDatabase();
    }

    public void close(){dbHelper.close();}

    public void addList(Liste l){

        ContentValues values=new ContentValues();
        values.put(MySQLiteHelper.NAME,l.name);
        values.put(MySQLiteHelper.NB,l.nb);
        values.put(MySQLiteHelper.NBC,l.nbc);
        values.put(MySQLiteHelper.PARENT_ID,-1);
        values.put(MySQLiteHelper.ANCETRE_ID,-1);
        values.put(MySQLiteHelper.TYPE,2);

        l.setId(database.insert(TABLE_LISTES,null,values));
    }

    public Liste getList(long id){
        String selectquery="SELECT * FROM " + TABLE_LISTES
                + " WHERE " + NAME + " = " + id;

        Cursor c=database.rawQuery(selectquery,null);
        if (c!=null)
            c.moveToFirst();
        else
            return null;

        @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
        @SuppressLint("Range") int nb=c.getInt(c.getColumnIndex(NB));
        @SuppressLint("Range") int nbc=c.getInt(c.getColumnIndex(NBC));

        Liste l=new Liste(name);
        l.nb=nb;
        l.nbc=nbc;
        l.setId(id);

        c.close();
        return l;
    }

    public void addElement(Element e){

        ContentValues values=new ContentValues();
        values.put(MySQLiteHelper.NAME,e.name);
        values.put(MySQLiteHelper.CHECKED,isChecked2(e.checked));
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

        e.setId(database.insert(TABLE_LISTES,null,values));
    }

    public Element getElement(long id){
        String selcectquery="SELECT * FROM " + TABLE_LISTES
                + " WHERE " + ID + " = " + id;

        Cursor c=database.rawQuery(selcectquery,null);
        if (c!=null)
            c.moveToFirst();
        else
            return null;

        @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
        @SuppressLint("Range") int nb=c.getInt(c.getColumnIndex(NB));
        @SuppressLint("Range") int nbc=c.getInt(c.getColumnIndex(NBC));
        @SuppressLint("Range") int parent_id=c.getInt(c.getColumnIndex(PARENT_ID));
        @SuppressLint("Range") int type=c.getInt(c.getColumnIndex(TYPE));
        @SuppressLint("Range") int checked=c.getInt(c.getColumnIndex(CHECKED));
        @SuppressLint("Range") int ancetre_id=c.getInt(c.getColumnIndex(ANCETRE_ID));
        c.close();
        if (type==0){
            Item item;
            if (parent_id==-1)
                item=new Item(name,null,getList(ancetre_id));
            else
                item=new Item(name,(SousListe)getElement(parent_id),getList(ancetre_id));
            item.checked=isChecked(checked);
            item.type=Element.Type.ITEM;
            return item;
        }
        else{
            SousListe sousListe;
            if (parent_id==-1)
                sousListe=new SousListe(name,null,getList(ancetre_id));
            else
                sousListe=new SousListe(name,(SousListe)getElement(parent_id),getList(ancetre_id));
            sousListe.checked=isChecked(checked);
            sousListe.type=Element.Type.SOUSLISTE;
            sousListe.nb=nb;
            sousListe.nbc=nbc;
            return sousListe;
        }
    }

    public ArrayList<String> getAllLists(){
        ArrayList<String> list=new ArrayList<>();

        Cursor c=database.query(TABLE_LISTES,allColums,null,null,null,null,null);

        c.moveToFirst();
        while (!c.isAfterLast()){
            @SuppressLint("Range") int ancetre_id=c.getInt(c.getColumnIndex(ANCETRE_ID));
            if (ancetre_id==-1) {
                @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
                list.add(name);
            }
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public void delete(int id){
        database.delete(TABLE_LISTES, ID + " = " + id,null);
    }

    public boolean isChecked(int c){
        if (c==0)
            return true;
        else
            return false;
    }

    public int isChecked2(boolean c){
        if (c)
            return 0;
        else
            return 1;
    }
}
