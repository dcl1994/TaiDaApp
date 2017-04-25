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

import java.util.List;

import widget.Shop;

/**
 *蔬菜的adapter适配器
 */
public class CabbageAdapter extends RecyclerView.Adapter<CabbageAdapter.ViewHolder>{
    private List<Shop> mShopList;
    private Context mContext;
    private LayoutInflater inflater;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View  CabbageView;
        ImageView imageView;    //图片ID
        TextView  tv_name;      //商品名称
        TextView  tv_coment;    //商品详情
        TextView  tv_price;     //商品价格
        TextView  tv_number_sold;  //商品销售数量

        ImageView shoping_img;  //购物车


        public ViewHolder(View view) {
            super(view);
            CabbageView=view;
            imageView= (ImageView) view.findViewById(R.id.cabbage_img);
            tv_name= (TextView) view.findViewById(R.id.cabbage_name);
            tv_coment= (TextView) view.findViewById(R.id.cabbage_coment);
            tv_price= (TextView) view.findViewById(R.id.cabbage_price);
            tv_number_sold= (TextView) view.findViewById(R.id.cabbage_numbersold);

            shoping_img= (ImageView) view.findViewById(R.id.shoping_cart);
        }
    }

    public CabbageAdapter(Context mContext,List<Shop> ShopList){
        this.mShopList=ShopList;
        this.mContext=mContext;
        inflater=LayoutInflater.from(mContext);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cabbage_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        /**
         * 购物车的点击事件
         */
        holder.shoping_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"购物车",Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shop shop=mShopList.get(position);

        Glide.with(mContext)
                .load(shop.getImageUrl())
                .placeholder(R.drawable.plugin_nopicture)    //占位图
                .into(holder.imageView);
        //holder.imageView.setImageResource(shop.getImageId());
        holder.tv_name.setText(shop.getName());
        holder.tv_coment.setText(shop.getComent());
        holder.tv_price.setText(shop.getPrice());
        holder.tv_number_sold.setText(shop.getNumber_sold());
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }




}
