package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.MineRankingVo;

import java.util.ArrayList;
import java.util.List;

public class MineRankingListActivity extends Activity {

    ViewPager pager = null;
    ArrayList<View> viewContainter = new ArrayList<View>();

    /**
     * tab title
     */
    private RadioButton radioButtonAll, radioButtonSend, radioButtonReceive;

    private ListView listViewMineRankList;

    private List<MineRankingVo> listMineRankingList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_main_mine_rankinglist);
        pager = (ViewPager) findViewById(R.id.viewpager_main_mine_rankinglist);

        View viewAll = LayoutInflater.from(this).inflate(R.layout.tenement_main_mine_rankinglist_tab, null);
        View viewSend = LayoutInflater.from(this).inflate(R.layout.tenement_main_mine_rankinglist_tab, null);
        View viewReceive = LayoutInflater.from(this).inflate(R.layout.tenement_main_mine_rankinglist_tab, null);
        radioButtonAll = (RadioButton) findViewById(R.id.rb_main_mine_rankinglist_all);
        radioButtonSend = (RadioButton) findViewById(R.id.rb_main_mine_rankinglist_send);
        radioButtonReceive = (RadioButton) findViewById(R.id.rb_main_mine_rankinglist_receive);
        listViewMineRankList = (ListView) viewAll.findViewById(R.id.lv_main_mine_rankinglist_list);

        //viewpager开始添加view
        viewContainter.add(viewAll);
        viewContainter.add(viewSend);
        viewContainter.add(viewReceive);

        initView();
    }

    private void initView() {
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

        });

        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(new PageTabOnPageChangeListener());

        radioButtonAll.setOnClickListener(new PageTabTitleOnClickListener(0));
        radioButtonSend.setOnClickListener(new PageTabTitleOnClickListener(1));
        radioButtonReceive.setOnClickListener(new PageTabTitleOnClickListener(2));

        listMineRankingList = getData();
        MineRankingListAdapter mineRankingListAdapter = new MineRankingListAdapter(getApplicationContext(), listMineRankingList);
        listViewMineRankList.setAdapter(mineRankingListAdapter);
    }

    /**
     * 状态选择
     *
     * @param i
     */
    private void select(int i) {
        switch (i) {
            case 0:
                radioButtonAll.setChecked(true);
                radioButtonSend.setChecked(false);
                radioButtonReceive.setChecked(false);
                radioButtonAll.setTextColor(getApplicationContext().getResources().getColor(R.color.pink));
                radioButtonSend.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonReceive.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                break;
            case 1:
                radioButtonAll.setChecked(false);
                radioButtonSend.setChecked(true);
                radioButtonReceive.setChecked(false);
                radioButtonAll.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonSend.setTextColor(getApplicationContext().getResources().getColor(R.color.pink));
                radioButtonReceive.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                break;
            case 2:
                radioButtonAll.setChecked(false);
                radioButtonSend.setChecked(false);
                radioButtonReceive.setChecked(true);
                radioButtonAll.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonSend.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonReceive.setTextColor(getApplicationContext().getResources().getColor(R.color.pink));
                break;
            default:
                break;
        }
    }

    /**
     * title 点击事件
     */
    private class PageTabTitleOnClickListener implements View.OnClickListener {
        private int index = 0;

        public PageTabTitleOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            pager.setCurrentItem(index);
            select(index);
        }
    }

    /**
     * page切换事件
     */
    public class PageTabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            select(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }


    private List<MineRankingVo> getData() {
        List<MineRankingVo> mineRankingVoList = new ArrayList<>();

        MineRankingVo mineRankingVo = new MineRankingVo();
        mineRankingVo.setName("我是一");
        mineRankingVo.setNumber("100");
        mineRankingVo.setImgurl("http://www.easyicon.net/api/resizeApi.php?id=510234&size=32");

        mineRankingVoList.add(mineRankingVo);
        mineRankingVoList.add(mineRankingVo);
        mineRankingVoList.add(mineRankingVo);
        mineRankingVoList.add(mineRankingVo);

        return mineRankingVoList;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_main_mine_rankinglist_back:
                finish();
                break;
            default:
                break;
        }
    }
}
