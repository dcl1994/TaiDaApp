package com.siyann.taidaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
/**
 * 购物车的fragment
 */
public class ShoppingCartFragment extends Fragment  implements View.OnClickListener{
    private Button mybutton;    //去逛逛

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.page_03,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mybutton= (Button) getActivity().findViewById(R.id.goshoping);
        mybutton.setOnClickListener(this);

    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goshoping:
                Intent intent=new Intent(getActivity(),GardenActivity.class);
                intent.putExtra("title","一米菜园");
                getActivity().startActivity(intent);
                getActivity().finish();
                break;

            default:
                break;

        }
    }
}
