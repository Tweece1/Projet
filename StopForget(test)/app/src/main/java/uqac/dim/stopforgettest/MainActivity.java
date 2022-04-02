package uqac.dim.stopforgettest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    String[] tableautest = {"Liste 1", "Liste 2","Liste 3","Liste 4","Liste 5","Liste 6","Liste 7","Liste 8","Liste 9",
            "Liste 10","Liste 11","Liste 12","Liste 13","Liste 14","Liste 15"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.listview,tableautest);

        ListView listView = findViewById(R.id.listeview);
        listView.setAdapter(adapter);
    }

    public void creation(View v){

    }

}