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
                + " WHERE " + ID + " = " + id;

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

    public int updateList(Liste l){
        ContentValues values=new ContentValues();
        values.put(MySQLiteHelper.NAME,l.name);
        values.put(MySQLiteHelper.NB,l.nb);
        values.put(MySQLiteHelper.NBC,l.nbc);
        values.put(MySQLiteHelper.PARENT_ID,-1);
        values.put(MySQLiteHelper.ANCETRE_ID,-1);
        values.put(MySQLiteHelper.TYPE,2);

        return database.update(TABLE_LISTES, values,ID + " = ?",new String[]{String.valueOf(l.getId())});
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
        @SuppressLint("Range") long ancetre_id=c.getLong(c.getColumnIndex(ANCETRE_ID));
        @SuppressLint("Range") long id1=c.getLong(c.getColumnIndex(ID));
        c.close();
        if (type==0){
            Item item;
            if (parent_id==ancetre_id)
                item=new Item(name,null,getList(ancetre_id));
            else{
                item=new Item(name,(SousListe)getElement(parent_id),getList(ancetre_id));
            }
            item.checked=isChecked(checked);
            item.type=Element.Type.ITEM;
            item.setId(id1);
            return item;
        }
        else{
            SousListe sousListe;
            if (parent_id==ancetre_id)
                sousListe=new SousListe(name,null,getList(ancetre_id));
            else
                sousListe=new SousListe(name,(SousListe)getElement(parent_id),getList(ancetre_id));
            sousListe.checked=isChecked(checked);
            sousListe.type=Element.Type.SOUSLISTE;
            sousListe.nb=nb;
            sousListe.nbc=nbc;
            sousListe.setId(id1);
            return sousListe;
        }
    }

    public int updateElement(Element e) {
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
            values.put(MySQLiteHelper.NB,((SousListe)e).nb);
            values.put(MySQLiteHelper.NBC,((SousListe)e).nbc);
        }
        return database.update(TABLE_LISTES,values,ID + " = ?",new String[]{String.valueOf(e.id)});
    }

    public ArrayList<String> getAllListsName(){
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

    public ArrayList<Liste> getAllLists(){
        ArrayList<Liste> list=new ArrayList<>();

        Cursor c=database.query(TABLE_LISTES,allColums,null,null,null,null,null);

        c.moveToFirst();
        while (!c.isAfterLast()){
            @SuppressLint("Range") int ancetre_id=c.getInt(c.getColumnIndex(ANCETRE_ID));
            if (ancetre_id==-1) {
                @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
                @SuppressLint("Range") int nb=c.getInt(c.getColumnIndex(NB));
                @SuppressLint("Range") int id=c.getInt(c.getColumnIndex(ID));
                Liste l=new Liste(name);
                l.nb=nb;
                l.setId(id);
                l.type= Element.Type.LISTE;
                list.add(l);
            }
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public ArrayList<Element> getAllListsElement(long id){
        Liste ancetre=getList(id);

        ArrayList<Element> res=new ArrayList<>();

        Cursor c=database.query(TABLE_LISTES,allColums,null,null,null,null,null);

        c.moveToFirst();
        while (!c.isAfterLast()){
            @SuppressLint("Range") int ancetre_id=c.getInt(c.getColumnIndex(ANCETRE_ID));
            if (ancetre_id==id){
                @SuppressLint("Range") int type=c.getInt(c.getColumnIndex(TYPE));
                if(type==0){
                    @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
                    @SuppressLint("Range") int che=c.getInt(c.getColumnIndex(CHECKED));
                    @SuppressLint("Range") int p=c.getInt(c.getColumnIndex(PARENT_ID));
                    Log.i("DIM", "ici "+String.valueOf(p));
                    @SuppressLint("Range") int id1=c.getInt(c.getColumnIndex(ID));
                    if (p==ancetre_id) {
                        Item item=new Item(name,null,ancetre);
                        item.checked=isChecked(che);
                        item.setId(id1);
                        res.add(item);
                    }
                    else{
                        Log.i("DIM", String.valueOf(((SousListe)getElement(p)).getId()));
                        Item item=new Item(name,(SousListe)getElement(p),ancetre);
                        item.checked=isChecked(che);
                        item.setId(id1);
                        res.add(item);
                    }
                }
                else {
                    @SuppressLint("Range") String name=c.getString(c.getColumnIndex(NAME));
                    @SuppressLint("Range") int che=c.getInt(c.getColumnIndex(CHECKED));
                    @SuppressLint("Range") int p=c.getInt(c.getColumnIndex(PARENT_ID));
                    @SuppressLint("Range") int nb=c.getInt(c.getColumnIndex(NB));
                    @SuppressLint("Range") int nbc=c.getInt(c.getColumnIndex(NBC));
                    @SuppressLint("Range") int id1=c.getInt(c.getColumnIndex(ID));
                    Log.i("DIM", "ici "+String.valueOf(p));
                    if (p==ancetre_id) {
                        SousListe sousListe=new SousListe(name,null,ancetre);
                        sousListe.checked=isChecked(che);
                        sousListe.nb=nb;
                        sousListe.nbc=nbc;
                        sousListe.setId(id1);
                        res.add(sousListe);
                    }
                    else{
                        SousListe sousListe=new SousListe(name,(SousListe)getElement(p),ancetre);
                        sousListe.checked=isChecked(che);
                        sousListe.nb=nb;
                        sousListe.nbc=nbc;
                        sousListe.setId(id1);
                        res.add(sousListe);
                    }
                }
            }
            c.moveToNext();
        }

        return res;
    }

    public void deleteAllListsElement(long id){
        ArrayList<Element> deleted=getAllListsElement(id);
        for (Element e: deleted){
            delete(e.getId());
        }
    }

    public void delete(long id){
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
