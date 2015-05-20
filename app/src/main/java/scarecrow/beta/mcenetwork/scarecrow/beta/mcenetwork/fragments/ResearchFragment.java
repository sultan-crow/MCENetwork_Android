package scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import scarecrow.beta.mcenetwork.R;
import scarecrow.beta.mcenetwork.ResearchActivity;


public class ResearchFragment extends Fragment {

    ListView research_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_research, container, false);
        research_list = (ListView) rootView.findViewById(R.id.research_list);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        String[] researches;

        try {

            JSONObject json = new JSONObject(db.getJSON());
            final JSONArray research = json.getJSONArray("research");

            if(research.length() > 0) {

                researches = new String[research.length()];

                for(int i = 0; i < research.length(); i ++)
                    researches[i] = research.getJSONObject(i).getString("title");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        researches );
                research_list.setAdapter(arrayAdapter);

                research_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String research_id = "";
                        try {
                            research_id = research.getJSONObject(position).getString("r_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent research_intent = new Intent(getActivity(), ResearchActivity.class);
                        research_intent.putExtra("id", research_id);
                        startActivity(research_intent);
                    }
                });

            } else
                Toast.makeText(getActivity(),
                        "No research has been submitted yet.",
                        Toast.LENGTH_SHORT);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();

        return rootView;

    }

}
