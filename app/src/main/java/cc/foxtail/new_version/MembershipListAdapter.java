package cc.foxtail.new_version;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sang on 2017. 4. 9..
 */

public class MembershipListAdapter extends BaseAdapter {

    private Context context;
    private List<MembershipItem> List = new ArrayList<MembershipItem>();


    public MembershipListAdapter(Context context) {
        this.context = context;
    }

    public void addItem(MembershipItem item) { List.add(item); }

    public void removeAll() { List.clear(); };

    public void setItemList(List<MembershipItem> list) { List = list; }

    public void remove(Object item) { List.remove(item); }

    @Override
    public int getCount() { return List.size(); }

    @Override
    public Object getItem(int i) { return List.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return List.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        MembershipText itemView = new MembershipText(context, List.get(position));
        return itemView;

    }

}
