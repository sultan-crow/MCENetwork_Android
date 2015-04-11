package library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.ClassmatesFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.FacultyFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.PostsFragment;
import scarecrow.beta.mcenetwork.scarecrow.beta.mcenetwork.fragments.ProfileFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {


    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return new PostsFragment();
            case 1: return new ClassmatesFragment();
            case 2: return new FacultyFragment();
            case 3: return new ProfileFragment();
        }

        return null;

    }

    @Override
    public int getCount() {
        return 4;
    }
}
