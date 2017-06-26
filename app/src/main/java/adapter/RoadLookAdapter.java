package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.siyann.taidaapp.R;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.List;

import utils.LogUtil;
import widget.Road;

/**
 * 路况查看
 */
public class RoadLookAdapter extends RecyclerView.Adapter<RoadLookAdapter.ViewHolder>{
    List<Road> mroadList;
    Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View RoadView;
        TextView address_title;
        public ViewHolder(View view) {
            super(view);
            RoadView=view;
            address_title= (TextView) view.findViewById(R.id.address_title);
        }
    }

    public RoadLookAdapter(Context mContext,List<Road> roadList){
        this.mContext=mContext;
        mroadList=roadList;
    }

    @Override
    public RoadLookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.road_look_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);


        /**
         * item的点击事件
         */
        holder.RoadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Road road=mroadList.get(position);

                String url=road.getLink();
                LogUtil.e("url",url);
                Toast.makeText(mContext,"开始播放",Toast.LENGTH_SHORT).show();
                if (TbsVideo.canUseTbsPlayer(mContext)){
                    TbsVideo.openVideo(mContext,url);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RoadLookAdapter.ViewHolder holder, int position) {
       Road road=mroadList.get(position);
       holder.address_title.setText(road.getTitle());
    }

    @Override
    public int getItemCount() {
        return mroadList.size();
    }
}
