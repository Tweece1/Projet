package uqac.dim.stopforgettest;

import android.content.Intent;
import android.os.Bundle;
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
    private boolean valider;
    private ArrayList<Element> container;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        container=new ArrayList<>();
        new_titre="";
        titre=findViewById(R.id.listtitre);
        valider=false;
    }

    public void ValiderTitre(View v){
        new_titre=titre.getText().toString();
        if (valider==false){valider=true;}
    }

    public void onBack(View v){
        Intent intent=new Intent();
        intent.putExtra("titre",new_titre);
        intent.putExtra("valider?",valider);
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
