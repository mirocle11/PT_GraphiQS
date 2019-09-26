package Data;

public class DataHolder {

    private static DataHolder instance = null;
    public static DataHolder getInstance() {
        if(instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

}
