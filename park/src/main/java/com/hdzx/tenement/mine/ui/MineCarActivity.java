package com.hdzx.tenement.mine.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.swipemenulistview.SwipeMenu;
import com.hdzx.tenement.swipemenulistview.SwipeMenuCreator;
import com.hdzx.tenement.swipemenulistview.SwipeMenuItem;
import com.hdzx.tenement.swipemenulistview.SwipeMenuListView;
import com.hdzx.tenement.utils.DensityUtil;
import com.hdzx.tenement.vo.MineCarVo;

import java.util.ArrayList;
import java.util.List;

public class MineCarActivity extends Activity {

    private SwipeMenuListView swipeMenuListView;

    private List<MineCarVo> mineCarVoList;

    private MineCarAdapter mineCarAdapter;

    private ScrollView scrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tenement_main_mine_car);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.lv_carlist);
        scrollView = (ScrollView) findViewById(R.id.sv_main_mine_car);


        mineCarVoList = getData();
        mineCarAdapter = new MineCarAdapter(getApplicationContext(), mineCarVoList);
        swipeMenuListView.setAdapter(mineCarAdapter);
        scrollView.smoothScrollTo(0, 0);
        swipeMenuListView.setSwipeScrollView(scrollView);


        initView();

    }

    private void initView() {
        initMessageCircleList();
    }


    private void initMessageCircleList() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(DensityUtil.dip2px(getApplicationContext(), 90));
                //set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        swipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                MineCarVo item = mineCarVoList.get(position);
                switch (index) {
                    case 0:
                        //删除车辆
                        mineCarVoList.remove(item);
                        mineCarAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private List<MineCarVo> getData() {
        List<MineCarVo> mineCars = new ArrayList<>();
        MineCarVo mineCarVo = new MineCarVo();
        mineCarVo.setCarName("奔驰C200");
        mineCarVo.setLicensePlate("苏B AB123");

        mineCars.add(mineCarVo);
        mineCars.add(mineCarVo);
        mineCars.add(mineCarVo);
        return mineCars;
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_main_mine_car_back:
                finish();
                break;
            default:
                break;
        }
    }
}
