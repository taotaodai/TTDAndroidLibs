package hzst.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.List;
/**
 * {@link ViewPager 适配器}
 * @author wt
 *
 */
public class MyFragmentAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragmentList;
    private List<CharSequence> titleList;
    private FragmentManager fm;
    public boolean[] fragmentsUpdateFlag;
    
    public MyFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
        fragmentsUpdateFlag = new boolean[fragmentList.size()];
    }
    
    public MyFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList,List<CharSequence> titleList) {
    	super(fm);
        this.fm = fm;
    	this.fragmentList = fragmentList;
    	this.titleList = titleList;
        fragmentsUpdateFlag = new boolean[fragmentList.size()];
	}

    public void setFragmentList(List<Fragment> fragmentList) {
        if(this.fragmentList != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragmentList){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {
    	return (fragmentList == null || fragmentList.size() == 0) ? null
				: fragmentList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	if(titleList != null){
    		if(titleList.size() > 0){
    			return titleList.get(position);
    		}
    	}
        return null;
    }

    @Override
    public int getCount() {
    	return fragmentList == null ? 0 : fragmentList.size();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;//这个返回值
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();

        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
            //如果这个fragment需要更新
        		
            FragmentTransaction ft = fm.beginTransaction();
            //移除旧的fragment
            ft.remove(fragment);
            //换成新的fragment
            fragment = fragmentList.get(position % fragmentList.size());
            //添加新fragment时必须用前面获得的tag，这点很重要
            ft.add(container.getId(), fragment, fragmentTag);
//            ft.attach(fragment);
            ft.commit();

            //复位更新标志
            fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
        }
        return fragment;
    }
}
