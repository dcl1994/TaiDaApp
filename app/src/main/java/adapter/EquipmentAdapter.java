package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.siyann.taidaapp.MonitoerActivity;
import com.siyann.taidaapp.NicknameActivity;
import com.siyann.taidaapp.R;
import com.siyann.taidaapp.SettingActivity;

import java.util.List;

import utils.LogUtil;
import widget.Equipment;

/**
 *设备列表的adapter
 */
public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private List<Equipment> mEquipmentList;

    private Context mContext;


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
            /**
             * 直播界面，获取直播结束后的图片地址存缓存，使用glide加载出来
             */
            equipmentImage= (ImageView) view.findViewById(R.id.header_img);
            //昵称
            equipmentName= (TextView) view.findViewById(R.id.equipment_name);
            //设置
            setting= (ImageView) view.findViewById(R.id.setting_img);
            //视频播放
            video= (ImageView) view.findViewById(R.id.videocan_img);
            //修改昵称
            nickname= (ImageView) view.findViewById(R.id.broder_img);
        }
    }


    /**
     *构造器
     * @param mContext
     * @param equipmentList
     */
    public EquipmentAdapter(Context mContext,List<Equipment> equipmentList){
        this.mContext=mContext;
        mEquipmentList=equipmentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        /**
         *item的点击事件
         * 将设备的设备的ID，密码传递过去
         */
        holder.equipmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Equipment equipment=mEquipmentList.get(position);
                Intent intent=new Intent(mContext, MonitoerActivity.class);
                intent.putExtra("id",""+equipment.getEquipid());
                intent.putExtra("pwd",equipment.getPassword());
                intent.putExtra("nickname",equipment.getNickname());
                LogUtil.e("itemid", equipment.getEquipid() + "");
                LogUtil.e("itempwd", equipment.getPassword());
                mContext.startActivity(intent);
            }
        });

        /**
         * 摄像头界面
         */
        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"视频",Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 修改设备信息列表界面
         */
        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Equipment equipment=mEquipmentList.get(position);
                Intent intent=new Intent(mContext, NicknameActivity.class);
                intent.putExtra("equipid",equipment.getEquipid());
                intent.putExtra("title","修改设备名称");

                LogUtil.e("equipid", equipment.getEquipid() + "");

                mContext.startActivity(intent);
            }
        });

        /**
         * 跳转到设备页面
         */
        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Equipment equipment=mEquipmentList.get(position);


                Intent intent=new Intent(mContext, SettingActivity.class);
                intent.putExtra("title","设置");
                int equipid=equipment.getEquipid();
                String password=equipment.getPassword();
                /**
                 * 将ID存缓存
                 */
                SharedPreferences.Editor editor=mContext.getSharedPreferences("equipment",Context.MODE_PRIVATE).edit();
                editor.putString("equipid",equipid+"");
                editor.putString("password",password);
                editor.commit();
                LogUtil.e("equipid", equipid + "");
                LogUtil.e("password", password);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }



    /**
     *点击事件
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Equipment equipment=mEquipmentList.get(position);

        /**
         * 如果图片地址不为空就加载出来
         */
        LogUtil.e("imagepath", "" + equipment.getImagepath());
//
//        Glide基本可以load任何可以拿到的媒体资源，如：
//        load SD卡资源：load("file://" + Environment.getExternalStorageDirectory().getPath() + "/test.jpg");

        if (!TextUtils.isEmpty(equipment.getImagepath())){
            Glide.with(mContext)
                    .load("file://"+equipment.getImagepath())
                    .asBitmap()
                    .placeholder(R.drawable.plugin_nopicture)
                    .into(holder.equipmentImage);
        }

        holder.equipmentName.setText(equipment.getNickname());
    }

    @Override
    public int getItemCount() {
        return mEquipmentList.size();
    }

}
