package recycler.htn.recyclerfinder;

public class RecycleObjectDB {

    boolean is_recyclable, special;
    String type;

    public RecycleObjectDB(boolean is_recyclable, boolean special, String type) {
        this.is_recyclable = is_recyclable;
        this.special = special;
        this.type = type;
    }

}
