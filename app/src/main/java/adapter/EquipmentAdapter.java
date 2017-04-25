package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siyann.taidaapp.MonitoerActivity;
import com.siyann.taidaapp.NicknameActivity;
import com.siyann.taidaapp.R;
import com.siyann.taidaapp.SettingActivity;

import java.util.List;

import widget.Equipment;

/**
 *设备列表的adapter
 */
public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private boolean ischecked=false;    //静音的开关键

    private List<Equipment> mEquipmentList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View equipmentView;
        ImageView equipmentImage;
        TextView equipmentName;
        ImageView setting;  //设置
        ImageView video;    //监控视频
        ImageView nickname; //昵称


        public ViewHolder(View view) {
            super(view);
            equipmentView=view;
            equipmentImage= (ImageView) view.findViewById(R.id.header_img);
            equipmentName= (TextView) view.findViewById(R.id.equipment_name);
            setting= (ImageView) view.findViewById(R.id.setting_img);
            video= (ImageView) view.findViewById(R.id.videocan_img);
            nickname= (ImageView) view.findViewById(R.id.broder_img);
        }
    }

    public EquipmentAdapter(List<Equipment> equipmentList){
        mEquipmentList=equipmentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        /**
         *item的点击事件
         */
        holder.equipmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                switch (position){
                    case 0:
                        Intent intent=new Intent(v.getContext(), MonitoerActivity.class);
                        if (ischecked){
                            intent.putExtra("ischecked","布防");
                        }else{
                            intent.putExtra("ischecked","撤防");
                          }
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        /**
         * 摄像头界面
         */
        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"视频",Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 修改昵称界面
         */
        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(v.getContext(), NicknameActivity.class);
               intent.putExtra("title","修改昵称");
               v.getContext().startActivity(intent);
            }
        });

        /**
         * 跳转到设备页面
         */
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), SettingActivity.class);
                intent.putExtra("title","设置");
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Equipment equipment=mEquipmentList.get(position);
        holder.equipmentName.setText(equipment.getName());
    }

    @Override
    public int getItemCount() {
        return mEquipmentList.size();
    }

}
