package uqac.dim.stopforgettest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private EditText titre;
    private String new_titre;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Intent intent=getIntent();
        id=intent.getLongExtra("id",-1);
        array_id=intent.getIntExtra("array_id",0);
        container=new ArrayList<>();
        titre=findViewById(R.id.listtitre);
        //current_list=MainActivity.database.getList(id);

        if (id==-1){
            new_titre="";
        }
        else{
            new_titre=intent.getStringExtra("titre");
            titre.setText(new_titre);
        }

        listedetest = new ArrayList<>();
        for (Element e: container
             ) {
            listedetest.add(e.afficher());
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
                ajout(view);
                return true;
            }
        });

    }

    public void onBack(View v){
        Intent intent=new Intent();
        new_titre=titre.getText().toString();
        intent.putExtra("titre",new_titre);
        intent.putExtra("id",id);
        intent.putExtra("array_id",array_id);
        setResult(2,intent);
        finish();
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
            //container.get(position).cocher();
        }
        else{
            v.setBackgroundResource(R.drawable.bg_list);
            //container.get(position).decocher();
        }
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
        if(!edttest.getText().toString().equals("")){
            String s = edttest.getText().toString();
            SousListe sousListe=new SousListe(s,null,current_list);
            adapter.add(sousListe.afficher());
            container.add(sousListe);
            dialog.dismiss();
        }
    }

    public void ajout_sousliste2(View v){
        if(!edttest.getText().toString().equals("")){
            String s = edttest.getText().toString();
            Element e = container.get(currentpos);
            SousListe sl;
            if(e instanceof SousListe){
                sl = (SousListe) e;
            }
            else{
                Item it = (Item) e;
                sl = new SousListe(it.name,it.parent,it.ancetre);
            }
            SousListe sousListe = new SousListe(s,sl,current_list);
            sl.nb += 1;
            adapter.remove(adapter.getItem(currentpos));
            adapter.insert(sl.afficher(),currentpos);
            adapter.insert(sousListe.afficher(),currentpos+1);
            container.remove(e);
            container.add(currentpos,sl);
            container.add(currentpos+1,sousListe);
            dialog.dismiss();
        }
    }

    public void ajout_item(View v){
        if(!edttest.getText().toString().equals("")){
            String s = edttest.getText().toString();
            Item item=new Item(s,null,current_list);
            adapter.add(item.afficher());
            container.add(item);
            dialog.dismiss();
        }
    }

    public void ajout_item2(View v){
        if(!edttest.getText().toString().equals("")){
            String s = edttest.getText().toString();
            Element e = container.get(currentpos);
            SousListe sl;
            if(e instanceof SousListe){
                sl = (SousListe) e;
            }
            else{
                Item it = (Item) e;
                sl = new SousListe(it.name,it.parent,it.ancetre);
            }
            Item item = new Item(s,sl,current_list);
            sl.nb += 1;
            adapter.remove(adapter.getItem(currentpos));
            adapter.insert(sl.afficher(),currentpos);
            adapter.insert(item.afficher(),currentpos+1);
            container.remove(e);
            container.add(currentpos,sl);
            container.add(currentpos+1,item);
            dialog.dismiss();
        }
    }

    public void annuler(View v){
        dialog.dismiss();
    }

    public void supprimer(View v){
        String s = adapter.getItem(currentpos);
        container.remove(currentpos);
        adapter.remove(s);
        dialog.dismiss();
    }
}
