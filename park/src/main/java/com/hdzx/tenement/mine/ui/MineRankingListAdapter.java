package com.hdzx.tenement.mine.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hdzx.tenement.R;
import com.hdzx.tenement.vo.MineRankingVo;
import com.hdzx.tenement.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MineRankingListAdapter extends BaseAdapter {

    private Context context;
    private List<MineRankingVo> mineRankingVoList;

    /**
     * imageload options
     */
    DisplayImageOptions options;

    public MineRankingListAdapter(Context context, List<MineRankingVo> mineRankingVoList) {
        this.context = context;
        this.mineRankingVoList = mineRankingVoList;
        // 异步加载图片
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.head_default)
                .showImageForEmptyUri(R.drawable.head_default)
                .showImageOnFail(R.drawable.head_default)
                .build();
    }

    @Override
    public int getCount() {
        return mineRankingVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mineRankingVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.tenement_main_mine_rankinglist_tab_list, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.textViewMineRankinglistPositopn.setText(Integer.toString(position));

        MineRankingVo mineRankingVo = (MineRankingVo) getItem(position);
        holder.textViewMineRankinglistNmae.setText(mineRankingVo.getName());
        holder.textViewMineRankinglistNum.setText(mineRankingVo.getNumber());

        ImageLoader.getInstance().displayImage(mineRankingVo.getImgurl(), holder.circleImageViewImg,
                options);

        return convertView;
    }

    class ViewHolder {
        TextView textViewMineRankinglistPositopn;
        TextView textViewMineRankinglistNmae;
        TextView textViewMineRankinglistNum;
        CircleImageView circleImageViewImg;

        public ViewHolder(View view) {
            textViewMineRankinglistPositopn = (TextView) view.findViewById(R.id.txt_main_mine_rankinglist_position);
            textViewMineRankinglistNmae = (TextView) view.findViewById(R.id.txt_main_mine_rankinglist_name);
            textViewMineRankinglistNum = (TextView) view.findViewById(R.id.txt_main_mine_rankinglist_num);
            circleImageViewImg = (CircleImageView) view.findViewById(R.id.img_main_mine_rankinglist_img);
            view.setTag(this);
        }
    }
}
