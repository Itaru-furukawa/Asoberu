package jp.ac.titech.itpro.sdl.asoberu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();

    //コンストラクタ
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        mFragmentList.add(new Fragment1());
        mFragmentList.add(new Fragment2());
    }

    //ViewPagerの数を返す
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //ViewPagerのフラグメントを返す
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    //タイトルを返す
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "メンバー情報の入力";
            case 1:
                return "集計結果";
            default:
                return "ページ" + (position + 1);
        }

    }

}