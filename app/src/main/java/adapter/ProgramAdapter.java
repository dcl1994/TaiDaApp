package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.siyann.taidaapp.R;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.List;

import utils.LogUtil;
import widget.Program;

/**
 * 电视节目的适配器
 */
public class ProgramAdapter  extends RecyclerView.Adapter<ProgramAdapter.ViewHolder>{

    private List<Program> mProgramList;

    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View ProgramView;
        ImageView ProgramImage;
        TextView ProgramName;

        public ViewHolder(View view) {
            super(view);
            ProgramView=view;

            ProgramImage= (ImageView) view.findViewById(R.id.tv_flag);
            ProgramName= (TextView) view.findViewById(R.id.tv_text);
        }
    }

    public ProgramAdapter(Context mContext,List<Program> programList){
        this.mContext=mContext;
        mProgramList=programList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        /**
         * holder中view的点击事件
         * 点击直接播放
         */
        holder.ProgramView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Program program=mProgramList.get(position);


                /**
                 * 播放直播视频
                 */
                String url=program.getLink();
                LogUtil.e("url", url);
                Toast.makeText(mContext,"开始播放",Toast.LENGTH_SHORT).show();
                if (TbsVideo.canUseTbsPlayer(mContext)){
                    TbsVideo.openVideo(mContext,url);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program program=mProgramList.get(position);

        /**
         * 获取台标
         */
        String imagepath=program.getLitpic();
        imagepath="http://121.42.32.107:8001/images/"+imagepath.replace("/_data/images/","");
        Glide.with(mContext)
                .load(imagepath)
                .placeholder(R.drawable.plugin_pictures)
                .into(holder.ProgramImage);


        /**
         * 获取电视台名称
         */
        holder.ProgramName.setText(program.getTitle());
    }

    @Override
    public int getItemCount() {
        return  mProgramList.size();
    }


}
