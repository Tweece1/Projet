package uqac.dim.stopforgettest;

public abstract class Element {



    public enum Type{SOUSLISTE,ITEM,LISTE};
    public String name;
    public Type type;
    public boolean checked;
    public SousListe parent;
    public Liste ancetre;
    public long id;

    public abstract void cocher();
    public abstract void cocher2();
    public abstract void decocher();
    public abstract String afficher();
    public abstract void setId(long id);
    public abstract long getId();
}
