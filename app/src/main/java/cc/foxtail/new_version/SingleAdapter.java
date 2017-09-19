package cc.foxtail.new_version;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class SingleAdapter extends BaseAdapter {

    ArrayList<SingleItem>  itemlist = new ArrayList<SingleItem>();

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void removeAll() { itemlist.clear(); }

    public void addItem(SingleItem item){
        itemlist.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SingleItemView view  = new SingleItemView(parent.getContext());
        SingleItem item = itemlist.get(position);
        view.setName(item.getName());
        view.setDate(item.getDate());
        view.setTime(item.getTime());
        if(item.isEnd==0){
            view.setCheckBackground(Color.WHITE);
        }else{
            view.setCheckBackground(Color.rgb(200,200,200));
        }
        return view;
    }
}
