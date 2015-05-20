package scarecrow.beta.mcenetwork;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;


public class ResearchActivity extends ActionBarActivity {

    String id;

    TextView title_view, link_view, abstract_view, keywords_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_view = (TextView) findViewById(R.id.research_title);
        link_view = (TextView) findViewById(R.id.link);
        abstract_view = (TextView) findViewById(R.id.r_abstract);
        keywords_view = (TextView) findViewById(R.id.keywords);

        id = getIntent().getStringExtra("id");
        String title, r_abstract, keywords, link;

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());


        try {
            JSONObject json = new JSONObject(db.getJSON());
            JSONArray research = json.getJSONArray("research");

            for(int i = 0; i < research.length(); i ++) {
                JSONObject item = research.getJSONObject(i);
                Log.d("hello", String.valueOf(item.getString("r_id").equals(id)));
                if(item.getString("r_id").equals(id)) {
                    title = item.getString("title");
                    r_abstract = item.getString("abstract");
                    link = item.getString("link");
                    keywords = item.getString("keywords");

                    title_view.setText(title);
                    keywords_view.setText("Keywords: " + keywords);
                    abstract_view.setText(r_abstract);
                    link_view.setText("Link: " + link);
                    Log.d("Hello", "Hello");
                    break;
                } else {
                    title_view.setText("Financial Engineering");
                    keywords_view.setText("Keywords: Finance, Engineering");
                    abstract_view.setText("This is an overview of my work done in Financial Engg.");
                    link_view.setText("http://dcetech.com/finance.pdf");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_research, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
