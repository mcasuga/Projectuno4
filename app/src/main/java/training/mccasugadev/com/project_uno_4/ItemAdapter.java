package training.mccasugadev.com.project_uno_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Map<String, Double> map;
    List<String> names;
    List<Double> prices;

    public ItemAdapter(Context c, Map m) {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        map = m;
        names = new ArrayList<>(map.keySet());
        prices = new ArrayList<>(map.values());
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.listview_layout, null);
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView priceTextView = view.findViewById(R.id.nameTextView);

        nameTextView.setText(names.get(position));
        priceTextView.setText("PHP " + prices.get(position).toString());

        return view;
    }
}
