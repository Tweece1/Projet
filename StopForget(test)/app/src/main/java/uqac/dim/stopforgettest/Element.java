package uqac.dim.stopforgettest;

public abstract class Element {

    public enum Type{SOUSLISTE,ITEM};
    public String name;
    public Type type;
    public boolean checked;
    public SousListe parent;

    public abstract void cocher();
    public abstract void decocher();
}
