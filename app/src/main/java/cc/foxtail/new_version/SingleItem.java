package cc.foxtail.new_version;

import org.json.JSONArray;
import org.json.JSONException;

public class SingleItem {

    int id;
    String name, date, time;
    int period, isEnd;
    String[]check_list = new String[5];
    JSONArray jsonArray;

    private boolean selectable = true;


    public SingleItem(int id,String name, String date,String time,
                      int period, int isEnd,JSONArray list) throws JSONException {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.period = period;
        this.isEnd = isEnd;
        this.jsonArray = list;
        for(int i=0;i<5;i++){
            check_list[i]=list.getString(i);
        }
    }


    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }
    public int getPeriod(){
        return period;
    }
    public int getIsEnd()  { return isEnd; }
    public JSONArray getJsonCheckList() { return jsonArray; }
    public String[] getCheckList() { return check_list; }

}
