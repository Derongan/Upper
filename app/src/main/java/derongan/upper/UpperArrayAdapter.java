package derongan.upper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class UpperArrayAdapter extends ArrayAdapter<String>{

    public UpperArrayAdapter(Context context, int layoutResourceId, List<String> data){
        super(context, layoutResourceId, R.id.count_type, data);
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        return position == 0 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            View view = super.getView(position, convertView, parent);

            TextView textView = (TextView) view.findViewById(R.id.count_type);
            textView.setVisibility(View.GONE);

            EditText editText = (EditText) view.findViewById(R.id.edit_text);
            editText.setVisibility(View.VISIBLE);

            return view;
        }
        else {
            return super.getView(position, convertView, parent);
        }
    }
}
