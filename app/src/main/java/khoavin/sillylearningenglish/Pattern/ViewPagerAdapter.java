package khoavin.sillylearningenglish.Pattern;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by KhoaVin on 2/15/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public String[] TabTitle;
    public FragmentPattern[] listFragment;
    public Context context;
    public ViewPagerAdapter(FragmentManager fm, String[] tabTitle, FragmentPattern[] ListFragment,Context Context) {

        super(fm);
        TabTitle = tabTitle;
        listFragment = ListFragment;
        this.context = Context;
    }
    @Override
    public Fragment getItem(int position) {
        if (listFragment.length>0)
        return listFragment[position];
        else
        return null;
    }

    @Override
    public int getCount() {
        return listFragment.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String s = "";
        if(TabTitle.length>0)
            s = TabTitle[position];
        return s;
    }
}
