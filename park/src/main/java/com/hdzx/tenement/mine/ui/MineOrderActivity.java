package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.MineOrderVo;

import java.util.ArrayList;
import java.util.List;

public class MineOrderActivity extends Activity {

    ViewPager pager = null;
    ArrayList<View> viewContainter = new ArrayList<View>();
    /**
     * tab title
     */
    private RadioButton radioButtonAll, radioButtonComplete, radioButtonNofinish;

    private View viewAll, viewComplete, viewNofinish;

    private List<MineOrderVo> mineOrderVoList;

    private ListView listViewOrder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_main_mine_order);
        pager = (ViewPager) findViewById(R.id.viewpager_main_mine_order);

        viewAll = LayoutInflater.from(this).inflate(R.layout.tenement_main_mine_order_tab, null);
        viewComplete = LayoutInflater.from(this).inflate(R.layout.tenement_main_mine_order_tab, null);
        viewNofinish = LayoutInflater.from(this).inflate(R.layout.tenement_main_mine_order_tab, null);
        radioButtonAll = (RadioButton) findViewById(R.id.rb_main_mine_order_bangbang_all);
        radioButtonComplete = (RadioButton) findViewById(R.id.rb_main_mine_order_bangbang_complete);
        radioButtonNofinish = (RadioButton) findViewById(R.id.rb_main_mine_order_bangbang_nofinish);

        listViewOrder = (ListView) viewAll.findViewById(R.id.lv_main_mine_orderall_list);


        //viewpager开始添加view
        viewContainter.add(viewAll);
        viewContainter.add(viewComplete);
        viewContainter.add(viewNofinish);

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
        radioButtonComplete.setOnClickListener(new PageTabTitleOnClickListener(1));
        radioButtonNofinish.setOnClickListener(new PageTabTitleOnClickListener(2));


        mineOrderVoList = getData();
        MineOrderAdapter mineOrderAdapter = new MineOrderAdapter(getApplicationContext(), mineOrderVoList);
        listViewOrder.setAdapter(mineOrderAdapter);
        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MineOrderDetailActivity.class);
                startActivity(intent);
            }
        });

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
                radioButtonComplete.setChecked(false);
                radioButtonNofinish.setChecked(false);
                radioButtonAll.setTextColor(getApplicationContext().getResources().getColor(R.color.pink));
                radioButtonComplete.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonNofinish.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                break;
            case 1:
                radioButtonAll.setChecked(false);
                radioButtonComplete.setChecked(true);
                radioButtonNofinish.setChecked(false);
                radioButtonAll.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonComplete.setTextColor(getApplicationContext().getResources().getColor(R.color.pink));
                radioButtonNofinish.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                break;
            case 2:
                radioButtonAll.setChecked(false);
                radioButtonComplete.setChecked(false);
                radioButtonNofinish.setChecked(true);
                radioButtonAll.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonComplete.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                radioButtonNofinish.setTextColor(getApplicationContext().getResources().getColor(R.color.pink));
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


    /**
     * 测试数据
     *
     * @return
     */
    private List<MineOrderVo> getData() {
        List<MineOrderVo> mineOrderVoList1 = new ArrayList<>();

        MineOrderVo mineOrderVo = new MineOrderVo();
        mineOrderVo.setCost("1");
        mineOrderVo.setStatus("等待中");
        mineOrderVo.setStatusInfo("等待付款");
        mineOrderVo.setOrdertype("快递收发");
        mineOrderVo.setOrdertypeStatus("送件");

        MineOrderVo mineOrderVo2 = new MineOrderVo();
        mineOrderVo2.setCost("2");
        mineOrderVo2.setStatus("等待中");
        mineOrderVo2.setStatusInfo("等待抢单");
        mineOrderVo2.setOrdertype("快递收发");
        mineOrderVo2.setOrdertypeStatus("送件");

        MineOrderVo mineOrderVo3 = new MineOrderVo();
        mineOrderVo3.setCost("3");
        mineOrderVo3.setStatus("等待中");
        mineOrderVo3.setStatusInfo("等待抢单");
        mineOrderVo3.setOrdertype("快递收发");
        mineOrderVo3.setOrdertypeStatus("寄件");

        mineOrderVoList1.add(mineOrderVo2);
        mineOrderVoList1.add(mineOrderVo);
        mineOrderVoList1.add(mineOrderVo3);

        return mineOrderVoList1;

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_main_mine_order_back:
                finish();
                break;
            default:
                break;
        }
    }
}
