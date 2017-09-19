package cc.foxtail.new_version;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class GridAdapter extends BaseAdapter {

    ArrayList<GridItem>  itemlist = new ArrayList<GridItem>();

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }

    public void removeAll() { itemlist.clear(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(GridItem item){
        itemlist.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        GridItemView view  = new GridItemView(parent.getContext());
        GridItem item = itemlist.get(position);
        view.setImageView(item.getCheckState());
        return view;
    }
}
