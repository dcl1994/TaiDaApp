package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siyann.taidaapp.CommunityDetailActivity;
import com.siyann.taidaapp.R;

import java.util.List;

import utils.LogUtil;
import widget.Community;

/**
 *社区物业通知的adapter
 */
public class PropertyNoticeAdapter extends RecyclerView.Adapter<PropertyNoticeAdapter.ViewHolder>{
    List<Community> mcommunityList;
    Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView introduction;
        private View communityview;
        private LinearLayout linearlayout;
        public ViewHolder(View view) {
            super(view);
            communityview=view;
            title= (TextView) view.findViewById(R.id.community_title);
            introduction= (TextView) view.findViewById(R.id.community_introduction);
            linearlayout= (LinearLayout) view.findViewById(R.id.community_line);
        }
    }
    /**
     * 构造
     * @param mContext
     * @param communityList
     */
    public PropertyNoticeAdapter(Context mContext,List<Community> communityList){
        this.mContext=mContext;
        mcommunityList=communityList;
    }

    @Override
    public PropertyNoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        /**
         * item的点击事件
         * 获取内容传递给详细界面
         */
        holder.communityview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Community community=mcommunityList.get(position);
                LogUtil.e("position", community.getContent());

                Intent intent=new Intent(mContext, CommunityDetailActivity.class);
                intent.putExtra("content",community.getContent());
                intent.putExtra("title","阅读全文");

                /**
                 * 设置背景颜色
                 */
                holder.linearlayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(PropertyNoticeAdapter.ViewHolder holder, int position) {
        Community community=mcommunityList.get(position);
        holder.title.setText(community.getTitle());
        holder.introduction.setText(community.getIntroduction());
    }
    @Override
    public int getItemCount() {
        return mcommunityList.size();
    }
}
