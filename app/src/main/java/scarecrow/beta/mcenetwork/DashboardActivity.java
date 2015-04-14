package scarecrow.beta.mcenetwork;

//import android.support.v7;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import library.DatabaseHandler;
import library.TabsPagerAdapter;
import library.UserFunctions;


public class DashboardActivity extends ActionBarActivity implements ActionBar.TabListener {

    UserFunctions userFunctions;
    Button btnLogout;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;

    int role;

    private String[] tabs = {"Posts", "Classmates", "Faculty", "Profile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFunctions = new UserFunctions();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        role = db.getRole();

        db.close();

        if(userFunctions.isUserLoggedIn(getApplicationContext())) {
            setContentView(R.layout.activity_dashboard);

            viewPager = (ViewPager) findViewById(R.id.pager);
            final ActionBar actionBar = getSupportActionBar();

            mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
            mAdapter.assignRole(role);

            if(role == 1) {

                tabs[0] = "Profile";
                tabs[1] = "Faculty";
                tabs[2] = "Research";
                tabs[3] = "Posts";

            }

            viewPager.setAdapter(mAdapter);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            for (String tabName : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tabName)
                .setTabListener(this));
            }

            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo ni = cm.getActiveNetworkInfo();

            if(ni == null) {

                Toast.makeText(getApplicationContext(), "Can't Connect to the Internet", Toast.LENGTH_LONG).show();
                //prepareNotices();

            } else {

                //new LoadNotices().execute();

            }

        } else {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(login);

            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    private void logout() {
        userFunctions.logoutUser(getApplicationContext());
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(login, 0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
