package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.siyann.taidaapp.R;

import java.util.List;

import utils.CheckBoxOnclick;
import utils.LogUtil;
import widget.SurveyQuesition;

/**
 * 调查问卷题目的适配器
 */
public class SurveyQuesitionAdapter extends RecyclerView.Adapter<SurveyQuesitionAdapter.ViewHolder>{
    private List<SurveyQuesition> msurveyQuesitionList;
    private CheckBoxOnclick mcheckBoxOnclick;

    private String sum="";
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView surveyquesition_title;

        LinearLayout seq1_linear;
        LinearLayout seq2_linear;
        LinearLayout seq3_linear;
        LinearLayout seq4_linear;

        CheckBox checkBox01;
        CheckBox checkBox02;
        CheckBox checkBox03;
        CheckBox checkBox04;

        public ViewHolder(View view) {
            super(view);
            surveyquesition_title= (TextView) view.findViewById(R.id.surveyquesition_title);
            seq1_linear= (LinearLayout) view.findViewById(R.id.seq1_linear);
            seq2_linear= (LinearLayout) view.findViewById(R.id.seq2_linear);
            seq3_linear= (LinearLayout) view.findViewById(R.id.seq3_linear);
            seq4_linear= (LinearLayout) view.findViewById(R.id.seq4_linear);
            checkBox01= (CheckBox) view.findViewById(R.id.checkbox01);
            checkBox02= (CheckBox) view.findViewById(R.id.checkbox02);
            checkBox03= (CheckBox) view.findViewById(R.id.checkbox03);
            checkBox04= (CheckBox) view.findViewById(R.id.checkbox04);
        }
    }

    public SurveyQuesitionAdapter(List<SurveyQuesition> surveyQuesitionList){
        msurveyQuesitionList=surveyQuesitionList;
    }


    public void setCheckBox(CheckBoxOnclick checkBox){
        this.mcheckBoxOnclick=checkBox;
    }

    @Override
    public SurveyQuesitionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.surveyquesition_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        /**
         * Linearlayout点击事件
         */
        holder.seq1_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                LogUtil.e("position",position+"");
                LogUtil.e("quesitionid","1");
                holder.checkBox01.setChecked(true);
                holder.checkBox02.setChecked(false);
                holder.checkBox03.setChecked(false);
                holder.checkBox04.setChecked(false);
                mcheckBoxOnclick.onclick();



            }
        });


        holder.seq2_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                LogUtil.e("position",position+"");
                LogUtil.e("quesitionid","2");
                holder.checkBox02.setChecked(true);
                holder.checkBox01.setChecked(false);
                holder.checkBox03.setChecked(false);
                holder.checkBox04.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });


        holder.seq3_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                LogUtil.e("position",position+"");
                LogUtil.e("quesitionid","3");
                holder.checkBox03.setChecked(true);
                holder.checkBox01.setChecked(false);
                holder.checkBox02.setChecked(false);
                holder.checkBox04.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });


        holder.seq4_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                LogUtil.e("position",position+"");
                LogUtil.e("quesitionid","4");
                holder.checkBox04.setChecked(true);
                holder.checkBox01.setChecked(false);
                holder.checkBox02.setChecked(false);
                holder.checkBox03.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });


        /**
         * CheckBox点击事件
         */
        holder.checkBox01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                SurveyQuesition surveyQuesition = msurveyQuesitionList.get(position);
                LogUtil.e("SurveyD_QID", surveyQuesition.getSurveyD_QID());
                holder.checkBox01.setChecked(true);
                holder.checkBox02.setChecked(false);
                holder.checkBox03.setChecked(false);
                holder.checkBox04.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });


        holder.checkBox02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                SurveyQuesition surveyQuesition=msurveyQuesitionList.get(position);
                LogUtil.e("SurveyD_QID",surveyQuesition.getSurveyD_QID());
                holder.checkBox02.setChecked(true);
                holder.checkBox01.setChecked(false);
                holder.checkBox03.setChecked(false);
                holder.checkBox04.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });


        holder.checkBox03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                SurveyQuesition surveyQuesition=msurveyQuesitionList.get(position);
                LogUtil.e("SurveyD_QID",surveyQuesition.getSurveyD_QID());
                holder.checkBox03.setChecked(true);
                holder.checkBox01.setChecked(false);
                holder.checkBox02.setChecked(false);
                holder.checkBox04.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });


        holder.checkBox04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                SurveyQuesition surveyQuesition=msurveyQuesitionList.get(position);
                LogUtil.e("SurveyD_QID",surveyQuesition.getSurveyD_QID());
                holder.checkBox04.setChecked(true);
                holder.checkBox01.setChecked(false);
                holder.checkBox02.setChecked(false);
                holder.checkBox03.setChecked(false);
                mcheckBoxOnclick.onclick();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SurveyQuesitionAdapter.ViewHolder holder, int position) {
        SurveyQuesition quesition=msurveyQuesitionList.get(position);
        holder.surveyquesition_title.setText(quesition.getSurveyD_Topic()+"?");
    }

    @Override
    public int getItemCount() {
        return msurveyQuesitionList.size();
    }
}
