package uqac.dim.stopforgettest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private EditText titre;
    private String new_titre;
    private ArrayList<Element> container;
    private long id;
    private int array_id;

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
}
