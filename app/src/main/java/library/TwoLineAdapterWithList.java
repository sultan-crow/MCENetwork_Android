package library;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import scarecrow.beta.mcenetwork.R;

public class TwoLineAdapterWithList extends ArrayAdapter<TwoLineStructure> {

    Context context;
    int layoutResourceId;
    ArrayList<TwoLineStructure> data = null;
    String username;

    public TwoLineAdapterWithList(Context context, int layoutResourceId, String username,
                                  ArrayList<TwoLineStructure> data) {

        super(context, layoutResourceId, data);
        this.context = context;
        this.username = username;
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

        TwoLineStructure structure = data.get(position);
        holder.txtTitle.setText(structure.title);
        holder.txtSubtext.setText(structure.subtext);
        holder.txtSubtextRight.setText(structure.subtext_right);

        //if(structure.subtext.equals(username)) {
            //holder.txtTitle.setGravity(Gravity.RIGHT);
        //}

        return row;

    }

    static class TwoLineHolder {
        TextView txtTitle;
        TextView txtSubtext;
        TextView txtSubtextRight;
    }
}
