package uqac.dim.stopforgettest;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> listes_name;
    private ArrayList<Liste> listes;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    public static DataBase database;
    private TextView txt=null;
    private ArrayList<TextView> to_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=new DataBase(this);
        database.open();

        listes_name=database.getAllListsName();
        listes=database.getAllLists();
        to_delete=new ArrayList<>();

        adapter=new ArrayAdapter<>(this,R.layout.listview, listes_name);

        listView=findViewById(R.id.listeview);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                txt = (TextView) view;
                boolean can=(txt.getBackground() instanceof GradientDrawable);
                if (can){
                    txt.setBackgroundColor(R.color.blue);
                    to_delete.add(txt);
                }
                else {
                    txt.setBackground(getDrawable(R.drawable.bg_main));
                    to_delete.remove(txt);
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                select(view);
            }
        });
    }

    public void creation(View v){
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        intent.putExtra("id",-1);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            Liste l=new Liste(data.getStringExtra("titre"));
            if (data.getLongExtra("id",-1)==-1){
                adapter.add(data.getStringExtra("titre"));
                database.addList(l);
                listes.add(l);
            }
            else{
                l.setId(data.getLongExtra("id",-1));
                int array_id=data.getIntExtra("array_id",0);
                database.updateList(l);
                Liste n=listes.get(array_id);
                n.name=l.name;
                listes_name.set(array_id,l.name);
                adapter.notifyDataSetChanged();
            }

        }
    }


    @Override
    protected void onResume() {
        database.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        database.open();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

    public void select(View v){
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        String name=((TextView)v).getText().toString();
        intent.putExtra("titre",name);
        Liste l=searchList(name);
        intent.putExtra("id",l.getId());
        intent.putExtra("array_id",adapter.getPosition(name));
        startActivityForResult(intent,2);
    }

    public void deleteList(View v){
        for (TextView textView : to_delete){
            String name=textView.getText().toString();
            Liste liste=searchList(name);
            database.delete(liste.getId());
            adapter.remove(name);
        }
        adapter.notifyDataSetChanged();
        to_delete.clear();
    }

    public Liste searchList(String name){
        boolean found=false;
        Liste res=new Liste(name);
        int index=0;
        while (!found){
            String is_name=(listes.get(index)).name;
            if (is_name.equals(name)){
                found=true;
                res=listes.get(index);
            }
            index++;
        }
        return res;
    }

}