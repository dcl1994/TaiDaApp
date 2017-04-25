package com.siyann.taidaapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import utils.GlideImageLoader;

/**
 * 首页的fragment
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private Banner banner; //图片轮播控件
    private ImageView ameter_email; //意见反馈
    private ImageView ameter_search; //搜索


    private LinearLayout ll_group_buy;  //天天团购
    private LinearLayout ll_price;      //今日特价
    private LinearLayout ll_gift;       //积分抽奖
    private LinearLayout ll_new;        //每周推荐

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.page_01,container,false);
        return view;
    }
    /**
     * 控件初始化代码
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
              banner= (Banner) getActivity().findViewById(R.id.ameter_banner);
        /**
         * 加载网络图片
         */
        //本地图片数据（资源文件）
        List<String> list = new ArrayList<>();
        list.add("http://a1.qpic.cn/psb?/V14EE5y82Myi8s/yxbjy19SWngp2ZKzJ4H.eZoMlbRijiTgIxcUL3UYPU0!/m/dIQBAAAAAAAAnull&bo=kgJxAQAAAAADB8I!&rf=photolist&t=5");
        list.add("http://a4.qpic.cn/psb?/V14EE5y82Myi8s/sXuJxb7LwfyyO8RguMASn1kSJYwkCW4PncnU83y08r0!/m/dIMBAAAAAAAAnull&bo=kgJxAQAAAAADB8I!&rf=photolist&t=5");
        list.add("http://a2.qpic.cn/psb?/V14EE5y82Myi8s/mPVLtFS0UAmOKJeesAdIfP*389JZqWIXbJ50iPFnlf4!/m/dIUBAAAAAAAAnull&bo=kgJxAQAAAAADB8I!&rf=photolist&t=5");
        list.add("http://a4.qpic.cn/psb?/V14EE5y82Myi8s/lUxIHAOwGaPpf1kCo0uaIJYO1ajCFlR3R9kHl*da.Wo!/m/dIMBAAAAAAAAnull&bo=kgJxAQAAAAADB8I!&rf=photolist&t=5");
        /**
         * 标题
         */
        List<String> titlelist = new ArrayList<>();
        titlelist.add("");
        titlelist.add("");
        titlelist.add("");
        titlelist.add("");
        banner.setImages(list)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(2500)
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setBannerTitles(titlelist)
                        //设置标题集合（当banner样式有显示title时）
                .setBannerAnimation(Transformer.DepthPage)
                .start();

        ameter_email= (ImageView) getActivity().findViewById(R.id.ameter_email);
        ameter_email.setOnClickListener(this);

        ameter_search= (ImageView) getActivity().findViewById(R.id.ameter_search);
        ameter_search.setOnClickListener(this);


        ll_group_buy= (LinearLayout) getActivity().findViewById(R.id.ll_group_buy);
        ll_price= (LinearLayout) getActivity().findViewById(R.id.ll_price);
        ll_gift= (LinearLayout) getActivity().findViewById(R.id.ll_gift);
        ll_new= (LinearLayout) getActivity().findViewById(R.id.ll_new);

        ll_group_buy.setOnClickListener(this);
        ll_price.setOnClickListener(this);
        ll_gift.setOnClickListener(this);
        ll_new.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ameter_email:
                Toast.makeText(getActivity(),"意见反馈",Toast.LENGTH_SHORT).show();
            break;

            case R.id.ameter_search:
                Toast.makeText(getActivity(),"搜索",Toast.LENGTH_SHORT).show();
                break;

            case R.id.ll_group_buy:  //天天团
                Toast.makeText(getActivity(),"天天团",Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_price:     //今日特价
                Toast.makeText(getActivity(),"今日特价",Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_gift:      //积分抽奖
                Toast.makeText(getActivity(),"积分抽奖",Toast.LENGTH_SHORT).show();

                break;

            case R.id.ll_new:       //每周推荐
                Toast.makeText(getActivity(),"每周推荐",Toast.LENGTH_SHORT).show();

                break;

            default:
                break;
        }
    }
}
