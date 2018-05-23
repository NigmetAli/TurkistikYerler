package nmt.turkistikyerler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by NigmetAli on 4.05.2016.
 */
public class tabGecisAdapter extends FragmentPagerAdapter {

    public tabGecisAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                return new FragmentListe();
            case 1:
                return new FragmentIcerik();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
