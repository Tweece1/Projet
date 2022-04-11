package uqac.dim.stopforgettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> tableautest;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableautest=new ArrayList<String>();
        adapter=new ArrayAdapter<>(this,R.layout.listview,tableautest);

        ListView listView = findViewById(R.id.listeview);
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

        }
    }
}