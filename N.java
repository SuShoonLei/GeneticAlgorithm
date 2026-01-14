// Station object (N)
public class N {
    private F type;

    public N(F type) {
        this.type = type;
    }

    public F getType() {
        return type;
    }

    public void setType(F type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name();
    }
}