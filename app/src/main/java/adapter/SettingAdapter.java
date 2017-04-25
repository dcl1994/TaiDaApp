package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siyann.taidaapp.AddSensorActivity;
import com.siyann.taidaapp.AlarmActivity;
import com.siyann.taidaapp.NicknameActivity;
import com.siyann.taidaapp.R;

import java.util.List;

import widget.Setting;

/**
 * Created by szjdj on 2017-04-12.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    private List<Setting> mSettingList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View settingview;
        ImageView settingImage;    //图片
        TextView settingName;     //名称

        public ViewHolder(View view) {
            super(view);
            settingview=view;
            settingImage = (ImageView) view.findViewById(R.id.setting_img);
            settingName = (TextView) view.findViewById(R.id.setting_textname);
        }
    }

    public SettingAdapter(List<Setting> settingList) {
        mSettingList = settingList;
    }

    /**
     * 写点击事件的地方
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.settingview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion=holder.getAdapterPosition();
                switch (postion){
                    case 0:
                        Intent intent=new Intent(v.getContext(), NicknameActivity.class);
                        intent.putExtra("title","修改昵称");
                        v.getContext().startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(v.getContext(),"网络设置",Toast.LENGTH_SHORT).show();
                        break;
                    case 2: //跳转到报警设置界面
                        Intent alarmint=new Intent(v.getContext(),AlarmActivity.class);
                        alarmint.putExtra("title","报警设置");
                        v.getContext().startActivity(alarmint);
                        break;
                    case 3:
                        Toast.makeText(v.getContext(),"录像设置",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Intent intent4=new Intent(v.getContext(), AddSensorActivity.class);
                        intent4.putExtra("title","添加传感器");
                        v.getContext().startActivity(intent4);
                        break;
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Setting setting = mSettingList.get(position);
        holder.settingName.setText(setting.getName());
        holder.settingImage.setImageResource(setting.getImageid());
    }

    @Override
    public int getItemCount() {
        return mSettingList.size();
    }

}
