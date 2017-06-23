package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siyann.taidaapp.CommunityDetailActivity;
import com.siyann.taidaapp.R;

import java.util.List;

import utils.LogUtil;
import widget.Community;

/**
 * 缤纷活动adapter
 */
public class ColorFulAdapter extends RecyclerView.Adapter<ColorFulAdapter.ViewHolder> {

    List<Community> mcommunityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView introduction;
        private View colorfulview;
        public ViewHolder(View view) {
            super(view);
            colorfulview=view;
            title = (TextView) view.findViewById(R.id.community_title);
            introduction = (TextView) view.findViewById(R.id.community_introduction);
        }
    }

    public ColorFulAdapter(List<Community> communityList) {
        mcommunityList = communityList;
    }

    @Override
    public ColorFulAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        /**
         * 点击事件
         */
        holder.colorfulview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Community community=mcommunityList.get(position);
                LogUtil.e("position", community.getContent());

                Intent intent=new Intent(parent.getContext(), CommunityDetailActivity.class);
                intent.putExtra("content",community.getContent());
                intent.putExtra("title","详情");
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ColorFulAdapter.ViewHolder holder, int position) {
        Community community = mcommunityList.get(position);
        holder.title.setText(community.getTitle());
        holder.introduction.setText(community.getIntroduction());
    }

    @Override
    public int getItemCount() {
        return mcommunityList.size();
    }
}
