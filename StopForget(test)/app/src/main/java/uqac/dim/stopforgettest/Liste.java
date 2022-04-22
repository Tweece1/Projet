package uqac.dim.stopforgettest;

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
            nb -= ((SousListe)e).nb;
            nbc -= ((SousListe)e).nbc;
        }
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
