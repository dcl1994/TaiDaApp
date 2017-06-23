package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siyann.taidaapp.R;
import com.siyann.taidaapp.SurveyQuesitionActivity;

import java.util.List;

import utils.MyApplication;
import widget.Survey;

/**
 * 获取社区调查问卷
 */
public class SurveyAdapter  extends RecyclerView.Adapter<SurveyAdapter.ViewHolder>{
    List<Survey> msurveyList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View SurveyView;
        TextView mtitle;

        public ViewHolder(View view) {
            super(view);
            SurveyView=view;
            mtitle= (TextView) view.findViewById(R.id.survey_title);
        }
    }

    public SurveyAdapter(List<Survey> surveyList){
        msurveyList=surveyList;
    }

    @Override
    public SurveyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
        final ViewHolder holder=new ViewHolder(view);
        /**
         * 点击事件
         */
        holder.SurveyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Survey survey=msurveyList.get(position);
                Intent intent=new Intent(MyApplication.getContext(), SurveyQuesitionActivity.class);
                intent.putExtra("title","问卷调查题目");
                intent.putExtra("IDN",survey.getIDN());
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(SurveyAdapter.ViewHolder holder, int position) {
        Survey survey=msurveyList.get(position);
        holder.mtitle.setText(survey.getTitle());
    }
    @Override
    public int getItemCount() {
        return msurveyList.size();
    }
}
