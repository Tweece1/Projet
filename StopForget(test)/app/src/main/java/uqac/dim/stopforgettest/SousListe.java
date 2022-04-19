package uqac.dim.stopforgettest;

import java.util.ArrayList;

public class SousListe extends Element{

    public ArrayList<Element> liste;
    public int nb;
    public int nbc;

    public SousListe(String name, SousListe parent, Liste ancetre){
        this.name=name;
        liste=new ArrayList<Element>();
        nb=0;
        nbc=0;
        checked=false;
        type=Type.SOUSLISTE;
        this.parent=parent;
        this.ancetre=ancetre;
    }

    @Override
    public void cocher() {
        checked=true;
        for (Element e:liste) {
            e.cocher();
        }
        __cocher();
    }

    public void __cocher(){
        SousListe p=parent;
        while (p!=null)
        {
            p.nbc+=this.nb;
            if (p.nbc==p.nb){
                p.checked=true;
                p.__cocher();
                p=null;
            }
            else {
                p=p.parent;
            }
        }
    }

    public void cocher2(){
        checked = true;
        __cocher2();
    }

    public void __cocher2(){
        SousListe p=parent;
        while (p!=null)
        {
            p.nbc+=1;
            if (p.nbc==p.nb){
                p.checked=true;
                p.__cocher2();
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
        for (Element e:liste) {
            e.decocher();
        }
        __decocher();
    }

    public void __decocher(){
        SousListe p=parent;
        while (p!=null)
        {
            p.nbc-=this.nb;
            if (p.nbc==p.nb-(this.nb)){
                p.checked=false;
                p.__decocher();
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
