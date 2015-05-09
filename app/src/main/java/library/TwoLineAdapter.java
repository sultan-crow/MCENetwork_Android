package library;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import scarecrow.beta.mcenetwork.R;

public class TwoLineAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    TwoLineStructure[] data = null;

    public TwoLineAdapter(Context context, int layoutResourceId, TwoLineStructure[] data) {

        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View row = convertView;
        TwoLineHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TwoLineHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.list_title);
            holder.txtSubtext = (TextView)row.findViewById(R.id.list_subtext);
            holder.txtSubtextRight = (TextView)row.findViewById(R.id.list_subtext_right);

            row.setTag(holder);

        } else {

            holder = (TwoLineHolder) row.getTag();

        }

        TwoLineStructure structure = data[position];
        holder.txtTitle.setText(structure.title);
        holder.txtSubtext.setText(structure.subtext);
        holder.txtSubtextRight.setText(structure.subtext_right);

        return row;

    }

    static class TwoLineHolder {
        TextView txtTitle;
        TextView txtSubtext;
        TextView txtSubtextRight;
    }
}
