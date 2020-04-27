package Core;

import java.util.ArrayList;

public class SaveFile {
    private String nameSave;
    private ArrayList<String> dataArray;

    public SaveFile(String nameSave, ArrayList<String> dataArray){
        this.nameSave = nameSave;
        this.dataArray = dataArray;
    }

    public String getNameSave() {
        return nameSave;
    }

    public void setNameSave(String nameSave) {
        this.nameSave = nameSave;
    }

    public ArrayList<String> getDataArray() {
        return dataArray;
    }

    public void setDataArray(ArrayList<String> dataArray) {
        this.dataArray = dataArray;
    }
}
