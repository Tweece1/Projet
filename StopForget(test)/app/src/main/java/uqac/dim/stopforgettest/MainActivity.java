package uqac.dim.stopforgettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> listes_name;
    private ArrayList<Liste> listes;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    public static DataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=new DataBase(this);
        database.open();

        listes_name=database.getAllListsName();
        listes=database.getAllLists();
        adapter=new ArrayAdapter<>(this,R.layout.listview, listes_name);

        listView=findViewById(R.id.listeview);
        listView.setAdapter(adapter);
    }

    public void creation(View v){
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (data.getBooleanExtra("valider?",true)) {
                adapter.add(data.getStringExtra("titre"));
                Liste l=new Liste(data.getStringExtra("titre"));
                database.addList(l);
                listes.add(l);
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
        creation(v);
    }
}