package uqac.dim.stopforgettest;

import static android.content.ContentValues.TAG;

import static uqac.dim.stopforgettest.MainActivity.database;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private EditText titre;
    private String new_titre;
    private String current_title;
    private ArrayList<Element> container;
    private Liste current_list;
    private long id;
    private int array_id;

    private ArrayList<String> listedetest;
    private ArrayAdapter<String> adapter;

    private ListView listView2;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText edttest;
    private int currentpos;
    private ArrayList<Element> copy;
    private ArrayList<Element> aff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Intent intent=getIntent();
        id=intent.getLongExtra("id",-1);
        Log.i("DIM", "id : "+ id);
        array_id=intent.getIntExtra("array_id",0);
        copy= database.getAllListsElement(id);
        container=new ArrayList<>();
        titre=findViewById(R.id.listtitre);


        int i=0;
        while (i<copy.size()){
            Element e=copy.get(i);
            if (e.parent==null){
                container.add(e);
                if (e.type== Element.Type.SOUSLISTE){
                    getElementOfSl(i+1,e);
                }
            }
            i++;
        }

        current_list=database.getList(id);

        new_titre=intent.getStringExtra("titre");
        titre.setText(new_titre);

        listedetest = new ArrayList<>();
        aff = new ArrayList<>();
        for (Element e: container) {
            current_list.liste.add(e);
            if(e.parent==null){
                aff.add(e);
                listedetest.add(e.afficher());
            }
        }
        adapter = new ArrayAdapter<>(this,R.layout.listview2,listedetest);
        listView2 = findViewById(R.id.listeview2);
        listView2.setAdapter(adapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cocher_decocher(view,i,l);
            }
        });
        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentpos = i;
                if(container.get(i).type== Element.Type.ITEM){
                    supp(view);
                }
                else {
                    ajout(view);
                }
                return true;
            }
        });
    }

    public void getElementOfSl(int i, Element e){
        while (i<copy.size()){
            Element element=copy.get(i);
            String parent_name="";
            if (element.parent!=null){
                parent_name=element.parent.name;
            }
            if (parent_name.equals(e.name)){
                container.add(element);
                SousListe sl=(SousListe)e;
                sl.liste.add(element);
                if (element.type.equals(Element.Type.SOUSLISTE))
                    getElementOfSl(i+1,element);
            }
            i++;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        };
        new Handler().postDelayed(runnable,100);
    }


    public void onBack(View v){
        current_title=titre.getText().toString();
        if (isthere(current_title) && !current_title.equals(new_titre)){
            if (current_title.equals(""))
                Toast.makeText(this,"Ce nom : "+current_title+" est invalider car vide",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this,"Ce nom : "+current_title+" existe déjà",Toast.LENGTH_SHORT).show();
        }
        else{
            for (Element e:container){
                database.updateElement(e);
            }
            Intent intent=new Intent();
            intent.putExtra("titre",current_title);
            intent.putExtra("id",id);
            intent.putExtra("array_id",array_id);
            setResult(2,intent);
            finish();
        }
    }

    public boolean isthere(String name){
        boolean not_there=true;
        int i=0;
        while (not_there && i<MainActivity.listes.size()){
            if (name.equals(MainActivity.listes.get(i).name))
                not_there=false;
            i++;
        }
        return !not_there;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void cocher_decocher(View v, int position, long id){
        if(v.getBackground().getConstantState()==getResources().getDrawable(R.drawable.bg_list).getConstantState()){
            v.setBackgroundResource(R.drawable.bg_list2);
            container.get(position).cocher();
        }
        else{
            v.setBackgroundResource(R.drawable.bg_list);
            container.get(position).decocher();
        }
        refresh();
    }

    public void ajout(View v){
        dialogBuilder = new AlertDialog.Builder(this);
        View view;
        if(v instanceof TextView)
        {
            view = getLayoutInflater().inflate(R.layout.pop_up_ajout_sup,null);
        }
        else {
            view = getLayoutInflater().inflate(R.layout.pop_up_ajout,null);
        }
        edttest = (EditText) view.findViewById(R.id.nom_ajout);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void ajout_sousliste(View v){
        String s = edttest.getText().toString();
        if(contient(container,s)){
            Toast.makeText(this,"Cet élément existe déjà", Toast.LENGTH_SHORT).show();
        }
        else if(!s.equals("")){
            s = edttest.getText().toString();
            SousListe sousListe=new SousListe(s,null,current_list);
            adapter.add(sousListe.afficher());
            container.add(sousListe);
            current_list.add_element(sousListe);
            aff.add(sousListe);
            database.addElement(sousListe);
            dialog.dismiss();
            refresh();
        }
    }

    public void ajout_sousliste2(View v){
        String s = edttest.getText().toString();
        if(contient(container,s)){
            Toast.makeText(this,"Cet élément existe déjà", Toast.LENGTH_SHORT).show();
        }
        else if(!s.equals("")){
            s = edttest.getText().toString();
            Element e = container.get(currentpos);
            SousListe sl;
            if(e instanceof SousListe){
                sl = (SousListe) e;
            }
            else{
                Item it = (Item) e;
                sl = new SousListe(it.name,it.parent,it.ancetre);
                sl.setId(it.getId());
            }
            SousListe sousListe = new SousListe(s,sl,current_list);
            sousListe.ajou();
            sl.liste.add(sousListe);

            database.updateElement(sl);

            adapter.remove(adapter.getItem(currentpos));
            adapter.insert(sl.afficher(),currentpos);
            adapter.insert(sousListe.afficher(),currentpos+1);

            container.remove(e);
            container.add(currentpos,sl);
            container.add(currentpos+1,sousListe);

            current_list.delete_element(e);
            current_list.add_element(sl);
            current_list.add_element(sousListe);

            database.addElement(sousListe);
            database.updateList(current_list);

            dialog.dismiss();
            refresh();
        }
    }

    public void ajout_item(View v){
        String s = edttest.getText().toString();
        if(contient(container,s)){
            Toast.makeText(this,"Cet élément existe déjà", Toast.LENGTH_SHORT).show();
        }
        else if(!s.equals("")){
            Item item=new Item(s,null,current_list);
            adapter.add(item.afficher());
            container.add(item);
            aff.add(item);
            current_list.add_element(item);

            database.addElement(item);
            database.updateList(current_list);

            dialog.dismiss();
            refresh();
        }
    }

    public void ajout_item2(View v){
        String s = edttest.getText().toString();
        if(contient(container,s)){
            Toast.makeText(this,"Cet élément existe déjà", Toast.LENGTH_SHORT).show();
        }
        else if(!s.equals("")){
            s = edttest.getText().toString();
            Element e = container.get(currentpos);
            SousListe sl;
            if(e instanceof SousListe){
                sl = (SousListe) e;
            }
            else{
                Item it = (Item) e;
                sl = new SousListe(it.name,it.parent,it.ancetre);
                sl.setId(it.getId());
            }
            Item item = new Item(s,sl,current_list);
            item.ajou();
            sl.liste.add(item);

            database.updateElement(sl);

            adapter.remove(adapter.getItem(currentpos));
            adapter.insert(sl.afficher(),currentpos);
            adapter.insert(item.afficher(),currentpos+1);

            container.remove(e);
            container.add(currentpos,sl);
            container.add(currentpos+1,item);

            current_list.delete_element(e);
            current_list.add_element(sl);
            current_list.add_element(item);

            database.addElement(item);
            database.updateList(current_list);

            dialog.dismiss();
            refresh();
        }
    }

    public void annuler(View v){
        dialog.dismiss();
    }

    public void supprimer(View v){
        String s = adapter.getItem(currentpos);
        current_list.delete_element(container.get(currentpos));
        container.get(currentpos).dele();
        Element e=container.get(currentpos);
        container.remove(currentpos);
        aff.remove(e);
        adapter.remove(s);
        database.delete(e.getId());
        database.updateList(current_list);
        dialog.dismiss();
        refresh();
    }

    public void refresh(){
        adapter.clear();
        for(int k =0; k<container.size();k++){
            Element element = container.get(k);
            if(aff.contains(element)){
                adapter.add(element.afficher());
                TextView v = (TextView) getViewByPosition(k,listView2);
                if(element.checked){
                    v.setBackgroundResource(R.drawable.bg_list2);
                }
                else {
                    v.setBackgroundResource(R.drawable.bg_list);
                }
            }
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void supp(View v){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_up_sup,null);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void reduire(View v){
        SousListe sl = (SousListe) container.get(currentpos);
        for(Element e:sl.liste){
            aff.remove(e);
        }
        dialog.dismiss();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("DIM","OOOOOOOOOOOOOOOOOOO");
                refresh();
            }
        };
        new Handler().postDelayed(runnable,100);
        //refresh();
    }

    public void augmenter(View v){
        SousListe sl = (SousListe) container.get(currentpos);
        int po = aff.indexOf(sl);
        for(Element e:sl.liste){
            if(!aff.contains(e)){
                aff.add(po+1,e);
                po+=1;
            }
        }
        dialog.dismiss();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("DIM","OOOOOOOOOOOOOOOOOOO");
                refresh();
            }
        };
        new Handler().postDelayed(runnable,100);
        //refresh();
    }

    public boolean contient(ArrayList<Element> conteneur, String str){
        int l = 0;
        boolean bo =false;
        while (!bo && l<conteneur.size()){
            Element element = conteneur.get(l);
            if(element.name.equals(str)){
                bo = true;
            }
            l+=1;
        }
        return bo;
    }
}
