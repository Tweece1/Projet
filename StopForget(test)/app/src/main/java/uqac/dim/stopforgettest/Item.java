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
                p.cocher();
                p=null;
            }
            else {

                p=p.parent;
            }
        }
    }

    @Override
    public void decocher() {
        checked=false;
        SousListe p=parent;
        while (p!=null)
        {
            p.nbc-=1;
            if (p.nbc==p.nb-1){
                p.decocher();
                p=null;
            }
            else {
                p=p.parent;
            }
        }
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
