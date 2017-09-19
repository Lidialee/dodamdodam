package cc.foxtail.new_version;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public class HealthyListAdapter extends BaseAdapter {

    private Context context;
    private List<HealthyCardItem> cardList = new ArrayList<HealthyCardItem>();

    public HealthyListAdapter(Context context) {
        this.context = context;
    }
    public void addItem(HealthyCardItem item) { cardList.add(item); }
    public void removeAll() { cardList.clear(); };
    @Override
    public int getCount() { return cardList.size(); }
    @Override
    public Object getItem(int i) { return cardList.get(i); }
    @Override
    public long getItemId(int i) { return i; }
    public View getView(int position, View convertView, ViewGroup parent) {
        HealthyCard itemView = new HealthyCard(context, cardList.get(position));
        return itemView;
    }


    public boolean areAllItemsSelectable() {
        return false;
    }
    public boolean isSelectable(int position) {
        try {
            return cardList.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
    public void setItemList(List<HealthyCardItem> list) { cardList = list; }
    public void remove(Object item) { cardList.remove(item); }

}
