package uqac.dim.stopforgettest;

import android.util.Log;

import java.util.ArrayList;

public class Liste {

    public String name;
    public Element.Type type;
    public ArrayList<Element> liste;
    public int nb;
    public int nbc;
    public long id;

    public Liste(String name) {
        this.name = name;
        this.liste = new ArrayList<Element>();
        this.nb = 0;
        this.nbc = 0;
        this.type= Element.Type.LISTE;
    }

    public void add_element(Element e) {
        liste.add(e);
        if (e.type == Element.Type.ITEM) {
            Log.i("DIM","je passe ici");
            nb++;
        }
    }

    public void delete_element(Element e) {
        liste.remove(e);
        if (e.getClass() == Item.class) {
            nb--;
            if(e.checked){
                nbc--;
            }
        } else {
            delete_sl((SousListe) e);
            //nb-=((SousListe)e).nb;
            //nbc-=((SousListe)e).nbc;
        }
    }

    public void delete_sl(SousListe e){
        ArrayList<Element> l = e.liste;
        for(Element el:l){
            if (el.type != Element.Type.ITEM) {
                delete_sl((SousListe) el);
            }
            liste.remove(el);
        }
    }

    public void count(){
        int numb=0;
        for (Element e:liste){
            if (e.type==Element.Type.ITEM){
                numb+=1;
            }
        }
        nb=numb;
    }

    public void countNBC(){
        int numb=0;
        for (Element e:liste){
            if (e.type==Element.Type.ITEM && e.checked){
                numb+=1;
            }
        }
        nbc=numb;
    }

    public String afficher(){
        String res = name + "\n" + nbc + " éléments cochés sur " + nb;
        return res;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
