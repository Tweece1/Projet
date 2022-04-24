package uqac.dim.stopforgettest;

public class Item extends Element {

    public Item(String name, SousListe parent, Liste ancetre){
        this.name=name;
        checked=false;
        type=Type.ITEM;
        this.parent=parent;
        this.ancetre=ancetre;
    }

    @Override
    public void cocher() {
        checked=true;
        SousListe p=parent;
        while (p!=null)
        {
            p.nbc+=1;
            if (p.nbc==p.nb){
                p.cocher2();
                p=null;
            }
            else {

                p=p.parent;
            }
        }
        ancetre.nbc+=1;
    }

    @Override
    public void cocher2(){
        checked=true;
    }

    @Override
    public void decocher() {
        checked=false;
        ancetre.nbc-=1;
        SousListe p=parent;
        while (p!=null)
        {
            p.nbc-=1;
            if (p.nbc==p.nb-1){
                p.checked = false;
            }
            p=p.parent;
        }
    }

    @Override
    public String afficher(){
        return name;
    }

    @Override
    public void dele(){
        SousListe p = parent;
        int a=0;
        if(checked){
            a=1;
        }
        while (p!=null){
            p.nb-=1;
            p.nbc-=a;
            p.liste.remove(this);
            p = p.parent;
        }
    }

    @Override
    public void ajou(){
        SousListe p = parent;
        while(p!=null){
            p.nb+=1;
            p = p.parent;
        }
        ancetre.nb++;
    }

    @Override
    public void setId(long id) {
        this.id=id;
    }

    @Override
    public long getId() {
        return id;
    }
}
