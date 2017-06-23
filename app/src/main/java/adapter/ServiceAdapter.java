package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siyann.taidaapp.ConveniencePhoneActivity;
import com.siyann.taidaapp.CookingCultureActivity;
import com.siyann.taidaapp.ExpressCheckActivity;
import com.siyann.taidaapp.MapActivity;
import com.siyann.taidaapp.PropertyNoticeActivity;
import com.siyann.taidaapp.R;
import com.siyann.taidaapp.RoadLookActivity;
import com.siyann.taidaapp.SmallKnowledgeActivity;

import java.util.List;

import utils.ItemUtil;

/**
 * 便民服务的Adapter
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private List<ItemUtil> mItemUtilList;

    /**
     * 首先定义一个内部类ViewHolder
     */

    static class ViewHolder extends RecyclerView.ViewHolder {
        View serviceView;
        ImageView service_image;    //图片
        TextView  service_title;    //标题
        TextView service_content;   //内容

        /** 获取布局中的实例，view*/
        public ViewHolder(View view) {
            super(view);
            serviceView=view;
            service_image= (ImageView) view.findViewById(R.id.item_img);
            service_title= (TextView) view.findViewById(R.id.item_title);
            service_content= (TextView) view.findViewById(R.id.item_content);
        }
    }

    /**ServiceAdapter的内部构造函数
     * 把要展示的数据源传进来，并赋值给一个全局变量mItemUtilList
     * 我们后续的操作都在这个数据源上进行
     * @param itemList
     */
    public ServiceAdapter(List<ItemUtil> itemList){
        mItemUtilList=itemList;
    }
    /**
     *创建ViewHolder实例，将item中的布局加载进来，
     * 然后创建一个ViewHolder实例
     * 并把加载出来的布局传入到构造函数中
     * 最好将ViewHolder实例返回
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mainitem_style, parent, false);
        final ViewHolder holder=new ViewHolder(view);
        holder.serviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 这里要做成动态可维护的，点击后跳转传递地址到另一个Activity，根据传递的url地址显示相应的内容
                 */
                int position=holder.getAdapterPosition();
                switch (position){
                    case 0:
                        Intent intent=new Intent(view.getContext(), RoadLookActivity.class);
                        intent.putExtra("title","路况查看");
                        view.getContext().startActivity(intent);
                        break;
                    case 1:
//                        Intent intent1=new Intent(view.getContext(), DetailsActivity.class);
//                        intent1.putExtra("url","http://www.tiptimes.com/tdtv/index.php/Home/Index/login.html");
//                        intent1.putExtra("title","家居报修");
//                        view.getContext().startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(view.getContext(), ExpressCheckActivity.class);
                      //  intent2.putExtra("url","http://m.kuaidi100.com/");
                        intent2.putExtra("title","快递查询");
                        view.getContext().startActivity(intent2);
                        break;
                    case 3:
                        Intent inten3=new Intent(view.getContext(), ConveniencePhoneActivity.class);
                        inten3.putExtra("title","便民号码");
                        view.getContext().startActivity(inten3);
                        break;
                    case 4:
                        Intent intent4=new Intent(view.getContext(), MapActivity.class);
                        intent4.putExtra("title", "社区地图");
                        view.getContext().startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5=new Intent(view.getContext(), PropertyNoticeActivity.class);
                        intent5.putExtra("title","物业通知");
                        view.getContext().startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6=new Intent(view.getContext(),SmallKnowledgeActivity.class);
                        intent6.putExtra("title","养身小知识");
                        view.getContext().startActivity(intent6);
                        break;

                    case 7:
                        Intent intent7=new Intent(view.getContext(), CookingCultureActivity.class);
                        intent7.putExtra("title","饮食文化");
                        view.getContext().startActivity(intent7);
                        break;
                    default:
                        break;
                }
            }
        });

        return holder;
    }
    /**
     * onBindViewHolder方法用于对RecyclerView子项的数据进行赋值
     * 会在每个子项被滚动到屏幕内的时候执行，通过position参数得到当前项的ItemUtil实例
     * 然后再将数据设置到ViewHolder的ImageView和TextView中
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemUtil itemUtil=mItemUtilList.get(position);
        holder.service_image.setImageResource(itemUtil.getImageId());
        holder.service_title.setText(itemUtil.getTitle());
        holder.service_content.setText(itemUtil.getContent());
    }

    /**
     * 用于告诉RecyclerView一共有多少个子项,直接返回数据源的长度就行
     * @return
     */
    @Override
    public int getItemCount() {
        return mItemUtilList.size();
    }

}
