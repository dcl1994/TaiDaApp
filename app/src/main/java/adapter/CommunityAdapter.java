package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.siyann.taidaapp.DetailsActivity;
import com.siyann.taidaapp.R;

import java.util.List;

import utils.ItemUtil;

/**
 * 社区政务的Adapter
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private List<ItemUtil> mItemUtilList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View serviceView;
        ImageView service_image;    //图片
        TextView service_title;    //标题
        TextView service_content;   //内容

        /**
         * 获取布局中的实例，view
         */
        public ViewHolder(View view) {
            super(view);
            serviceView = view;
            service_image = (ImageView) view.findViewById(R.id.item_img);
            service_title = (TextView) view.findViewById(R.id.item_title);
            service_content = (TextView) view.findViewById(R.id.item_content);
        }
    }

    public CommunityAdapter(List<ItemUtil> itemList) {
        mItemUtilList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mainitem_style, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.serviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 这里要做成动态可维护的，点击后跳转传递地址到另一个Activity，根据传递的url地址显示相应的内容
                 */
                int position = holder.getAdapterPosition();
                switch (position) {
                    case 0:
                        Intent intent=new Intent(view.getContext(), DetailsActivity.class);
                        intent.putExtra("url","http://www.tiptimes.com/home_td/index.php/Home/Index/list_qu.html");
                        intent.putExtra("title","社区动态");
                        view.getContext().startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(view.getContext(), DetailsActivity.class);
                        intent1.putExtra("url","http://www.tiptimes.com/home_td/index.php/Home/Index/twker.html");
                        intent1.putExtra("title","社区推客");
                        view.getContext().startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(view.getContext(), DetailsActivity.class);
                        intent2.putExtra("url", "http://www.tiptimes.com/home_td/index.php/Home/Index/questionnaire.html");
                        intent2.putExtra("title","问卷调查");
                        view.getContext().startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3=new Intent(view.getContext(), DetailsActivity.class);
                        intent3.putExtra("url", "http://www.tiptimes.com/home_td/index.php/Home/Index/feedback.html");
                        intent3.putExtra("title","居民反馈");
                        view.getContext().startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4=new Intent(view.getContext(), DetailsActivity.class);
                        intent4.putExtra("url", "http://www.tiptimes.com/home_td/index.php/Home/Index/list_x.html?type=2");
                        intent4.putExtra("title","缤纷活动");
                        view.getContext().startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5=new Intent(view.getContext(), DetailsActivity.class);
                        intent5.putExtra("url", "http://www.tiptimes.com/home_td/index.php/Home/Index/personal.html");
                        intent5.putExtra("title","个人中心");
                        view.getContext().startActivity(intent5);
                        break;
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemUtil itemUtil = mItemUtilList.get(position);
        holder.service_image.setImageResource(itemUtil.getImageId());
        holder.service_title.setText(itemUtil.getTitle());
        holder.service_content.setText(itemUtil.getContent());
    }

    @Override
    public int getItemCount() {
        return mItemUtilList.size();
    }
}
