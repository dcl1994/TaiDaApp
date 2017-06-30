package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.siyann.taidaapp.CommunityDetailActivity;
import com.siyann.taidaapp.R;

import java.util.List;

import utils.LogUtil;
import widget.Community;

/**
 * 社区新闻
 * 社区物业通知
 * 的适配器
 *
 */
public class CommunityNewsAdapter  extends RecyclerView.Adapter<CommunityNewsAdapter.ViewHolder>{
    List<Community> mcommunityList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView introduction;
        private ImageView content_img;
        private View communityview;
        private LinearLayout mylinearlayout;
        private TextView texttime;
        public ViewHolder(View view) {
            super(view);
            communityview=view;
            title= (TextView) view.findViewById(R.id.community_title);
            introduction= (TextView) view.findViewById(R.id.community_introduction);
            content_img= (ImageView) view.findViewById(R.id.content_img);
            mylinearlayout= (LinearLayout) view.findViewById(R.id.community_line);
            texttime= (TextView) view.findViewById(R.id.text_time);
        }
    }

    public CommunityNewsAdapter(Context mContext,List<Community> communityList){
        this.mContext=mContext;
        mcommunityList=communityList;
    }

    /**
     * 点击事件的处理
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CommunityNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
                 * 设置背景
                 */
                holder.mylinearlayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.gray));

                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CommunityNewsAdapter.ViewHolder holder, int position) {
     Community community=mcommunityList.get(position);


     holder.content_img.setVisibility(View.VISIBLE);
     holder.title.setText(community.getTitle());
        holder.introduction.setText(community.getIntroduction());
        holder.texttime.setText(community.getUpdated());
        /**
         *替换原有的图片
         */
     String imagepath=community.getLitpic();
     imagepath="http://121.42.32.107:8001/images/"+imagepath.replace("/_data/images/","");
     Glide.with(mContext)
             .load(imagepath)
             .placeholder(R.drawable.plugin_pictures)
             .into(holder.content_img);
    }
    @Override
    public int getItemCount() {
        return mcommunityList.size();
    }
}
