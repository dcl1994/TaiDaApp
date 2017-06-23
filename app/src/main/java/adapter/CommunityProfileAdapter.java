package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siyann.taidaapp.R;

import java.util.List;

import widget.Community;

/**
 *社区简介的adapter
 */
public class CommunityProfileAdapter  extends RecyclerView.Adapter<CommunityProfileAdapter.ViewHolder>{
    List<Community> mcommunityList;
    private Context mContext;
    String content="";
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView profile_title;
        TextView profile_content;

        public ViewHolder(View view) {
            super(view);
            profile_title= (TextView) view.findViewById(R.id.profile_title);
            profile_content= (TextView) view.findViewById(R.id.profile_content);
        }
    }

    public CommunityProfileAdapter(Context mContext,List<Community> communityList){
        this.mContext=mContext;
        mcommunityList=communityList;
    }


    @Override
    public CommunityProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommunityProfileAdapter.ViewHolder holder, int position) {
       Community community=mcommunityList.get(position);
        holder.profile_title.setText(community.getTitle());
        content=community.getContent();
        content = content.replace("&lt;p&gt;", "")
                .replace("&lt;/p&gt", "")
                .replace("&amp;ldquo","")
                .replace("&amp;rdquo","")
                .replace("&amp;nbsp;;","");

        holder.profile_content.setText(content);
    }

    @Override
    public int getItemCount() {
        return mcommunityList.size();
    }
}
