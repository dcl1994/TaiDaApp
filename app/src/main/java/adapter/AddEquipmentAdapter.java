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
import com.siyann.taidaapp.R;

import java.util.List;

import widget.Equipment;

/**
 *添加设备的adapter
 */
public class AddEquipmentAdapter extends RecyclerView.Adapter<AddEquipmentAdapter.ViewHolder> {

    private List<Equipment> mEquipmentList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View equipmentView;
        ImageView equipmentImage;
        TextView equipmentName;
        ImageView iclockimg;
        public ViewHolder(View view) {
            super(view);
            equipmentView=view;
            equipmentImage= (ImageView) view.findViewById(R.id.header_img);
            equipmentName= (TextView) view.findViewById(R.id.equipment_name);
            iclockimg= (ImageView) view.findViewById(R.id.ic_lockimg);
        }
    }

    public AddEquipmentAdapter(List<Equipment> equipmentList){
        mEquipmentList=equipmentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.equipmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                switch (position){
                    case 0:
                        Intent intent=new Intent(v.getContext(), MonitoerActivity.class);
                        intent.putExtra("title","视频监控");
                        view.getContext().startActivity(intent);
                        break;
                    default:
                        break;

                }
            }
        });

        holder.iclockimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"iclockimg被点击了",Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Equipment equipment=mEquipmentList.get(position);
        holder.equipmentImage.setImageResource(equipment.getImageId());
        holder.equipmentName.setText(equipment.getName());;

    }

    @Override
    public int getItemCount() {
        return mEquipmentList.size();
    }

}
