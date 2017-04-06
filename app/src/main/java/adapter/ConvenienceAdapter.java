package adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siyann.taidaapp.R;

import java.util.List;

import widget.ConveniencePhone;

/**
 *便民号码的adapter
 */
public class ConvenienceAdapter extends RecyclerView.Adapter<ConvenienceAdapter.ViewHolder> {

    private List<ConveniencePhone> mConveniencePhoneList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View ConveniencePhoneView;

        TextView CompanyName;   //公司名称

        TextView Phone;       //白天的电话

        TextView PhoneNight;  //晚上的电话

        TextView Day;       //白天
        TextView Night;     //夜晚

        public ViewHolder(View view) {
            super(view);
            ConveniencePhoneView=view;

            CompanyName= (TextView) view.findViewById(R.id.companyname);

            Phone= (TextView) view.findViewById(R.id.phonenumber);

            PhoneNight= (TextView) view.findViewById(R.id.phonenumbernight);

            Day= (TextView) view.findViewById(R.id.day);

            Night= (TextView) view.findViewById(R.id.night);
        }
    }

    public ConvenienceAdapter(List<ConveniencePhone> conveniencePhoneList){
        mConveniencePhoneList=conveniencePhoneList;
    }

    /**
     * 点击事件
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.convenience_phone_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
            holder.Phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getAdapterPosition();
                    ConveniencePhone conveniencePhone=mConveniencePhoneList.get(position);
                            /**
                             * 跳转到拨号界面
                             */
                            Uri uri= Uri.parse("tel:"+"022-"+conveniencePhone.getPhone());
                            Intent intent=new Intent(Intent.ACTION_DIAL,uri);
                            view.getContext().startActivity(intent);
                }
            });

        holder.PhoneNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                ConveniencePhone conveniencePhone=mConveniencePhoneList.get(position);
                        Uri uri2= Uri.parse("tel:"+"022-"+conveniencePhone.getPhonenight());
                        Intent intent2=new Intent(Intent.ACTION_DIAL,uri2);
                        view.getContext().startActivity(intent2);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConveniencePhone conveniencePhone=mConveniencePhoneList.get(position);
        /**
         * 设置显示的内容
         */
        holder.CompanyName.setText(conveniencePhone.getCompanyname());
        holder.Phone.setText(conveniencePhone.getPhone());
        holder.PhoneNight.setText(conveniencePhone.getPhonenight());
        holder.Day.setText(conveniencePhone.getDay());
        holder.Night.setText(conveniencePhone.getNight());
    }

    @Override
    public int getItemCount() {
        return mConveniencePhoneList.size();
    }

}
