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
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private EditText titre;
    private String new_titre;
    private ArrayList<Element> container;
    private long id;
    private int array_id;
    private ArrayList<String> listedetest;
    private ArrayAdapter<String> adapter;
    private ListView listView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Intent intent=getIntent();
        id=intent.getLongExtra("id",-1);
        array_id=intent.getIntExtra("array_id",0);
        container=new ArrayList<>();
        titre=findViewById(R.id.listtitre);

        if (id==-1){
            new_titre="";
        }
        else{
            new_titre=intent.getStringExtra("titre");
            titre.setText(new_titre);
        }

        listedetest = new ArrayList<>();
        listedetest.add("test");
        listedetest.add("test1");
        listedetest.add("test2");
        adapter = new ArrayAdapter<>(this,R.layout.listview2,listedetest);
        listView2 = findViewById(R.id.listeview2);
        listView2.setAdapter(adapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cocher_decocher(view,i,l);
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
        }
        else{
            v.setBackgroundResource(R.drawable.bg_list);
        }
    }

    public void ajout(View v){

    }
}
