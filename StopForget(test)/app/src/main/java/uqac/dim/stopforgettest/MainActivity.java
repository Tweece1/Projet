package uqac.dim.stopforgettest;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> listes_name;
    public static ArrayList<Liste> listes;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    public static DataBase database;

    private TextView txt=null;

    private ArrayList<TextView> to_do;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText day;
    private EditText month;
    private EditText hour;
    private EditText min;

    public static NotificationManager nm;
    public static String CHANNEL_ID = "glucose";
    public static String CHANNEL_NAME = "wesh";
    public static String CHANNEL_DESCRIPTION = "pourquoi";
    public static int NOTIFICATION_ID = 1111;

    public static int[] date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database=new DataBase(this);
        database.open();
        createNotificationChannel();

        listes=database.getAllLists();
        listes_name = new ArrayList<>();
        for (Liste l:listes){
            listes_name.add(l.afficher());
        }
        to_do =new ArrayList<>();
        date_time=new int[4];

        adapter=new ArrayAdapter<>(this,R.layout.listview, listes_name);


        listView=findViewById(R.id.listeview);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                txt = (TextView) view;
                //boolean can=(txt.getBackground() instanceof GradientDrawable);
                boolean can=(txt.getBackground().getConstantState()==getResources().getDrawable(R.drawable.bg_main).getConstantState());
                if (can){
                    //txt.setBackgroundColor(R.color.blue);
                    txt.setBackgroundResource(R.drawable.bg_main2);
                    to_do.add(txt);
                }
                else {
                    txt.setBackground(getDrawable(R.drawable.bg_main));
                    to_do.remove(txt);
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

    //Nous avons laiss?? la possibilit?? ?? l'utilisateur de mettre une alarme, malheureusement celle-ci ne marche pas.
    public void makeAlarm(View v){
        dialogBuilder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.pop_up_layout,null);

        day=(EditText)view.findViewById(R.id.day);
        month=(EditText)view.findViewById(R.id.month);
        hour=(EditText)view.findViewById(R.id.hour);
        min=(EditText)view.findViewById(R.id.min);

        dialogBuilder.setView(view);
        dialog=dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void annuler2(View v){
        dialog.dismiss();
    }

    public void checkDate(View v){
        if (!day.getText().toString().equals("") && !hour.getText().toString().equals("") && !month.getText().toString().equals("")
            && !min.getText().toString().equals("")) {
            date_time[0]=Integer.parseInt(day.getText().toString());
            date_time[1]=Integer.parseInt(hour.getText().toString());
            date_time[2]=Integer.parseInt(min.getText().toString());
            date_time[3]=Integer.parseInt(month.getText().toString());

            Intent i=new Intent(this,MonServiceAlarm.class);
            String[] all_lists=new String[to_do.size()];
            int j=0;
            for (TextView t:to_do){
                all_lists[j]=t.getText().toString();
                j++;
            }
            i.putExtra("all",all_lists);
            i.putExtra("test",date_time);
            startService(i);
            dialog.dismiss();
        }
        else{
            Toast.makeText(this, "Date non valide", Toast.LENGTH_SHORT).show();
        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    public void creation(View v){
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        Liste l=new Liste("");
        adapter.add(l.afficher());
        database.addList(l);
        listes.add(l);
        intent.putExtra("id",l.getId());
        intent.putExtra("array_id",adapter.getPosition(l.afficher()));
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            long id=data.getLongExtra("id",0);
            int array_id=data.getIntExtra("array_id",0);
            Liste l=database.getList(id);
            l.name=data.getStringExtra("titre");
            database.updateList(l);
            Log.i("DIM", String.valueOf(array_id));
            listes.set(array_id,l);
            listes_name.set(array_id,l.afficher());
            adapter.notifyDataSetChanged();
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
        String [] sp = name.split("\n");
        intent.putExtra("titre",sp[0]);
        int array_id=adapter.getPosition(name);
        Liste l=searchList(array_id);
        Log.i("DIM", "array id : "+String.valueOf(array_id));
        intent.putExtra("id",l.getId());
        intent.putExtra("array_id",array_id);
        startActivityForResult(intent,2);
    }

    public void deleteList(View v){
        for (TextView textView : to_do){
            String name=textView.getText().toString();
            String [] sp = name.split("\n");
            Liste liste=searchList(adapter.getPosition(name));
            database.deleteAllListsElement(liste.getId());
            database.delete(liste.getId());
            listes.remove(liste);
            adapter.remove(name);
        }
        adapter.notifyDataSetChanged();
        to_do.clear();
    }

    public Liste searchList(int id){
        Liste res=new Liste(" ");
        res=listes.get(id);
        return res;
    }
}