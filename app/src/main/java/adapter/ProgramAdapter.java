package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.siyann.taidaapp.R;

import java.util.List;

import widget.Program;

/**
 * 电视节目的适配器
 */
public class ProgramAdapter  extends RecyclerView.Adapter<ProgramAdapter.ViewHolder>{

    private List<Program> mProgramList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View ProgramView;
        ImageView ProgramImage;
        TextView ProgramName;

        public ViewHolder(View view) {
            super(view);
            ProgramView=view;

            ProgramImage= (ImageView) view.findViewById(R.id.tv_flag);
            ProgramName= (TextView) view.findViewById(R.id.tv_text);
        }
    }

    public ProgramAdapter(List<Program> programList){
        mProgramList=programList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        /**
         * holder中view的点击事件
         */
        holder.ProgramView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Program program=mProgramList.get(position);
                Toast.makeText(v.getContext(),"你点击了"+program.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program program=mProgramList.get(position);
        holder.ProgramImage.setImageResource(program.getImageId());
        holder.ProgramName.setText(program.getName());
    }

    @Override
    public int getItemCount() {
        return  mProgramList.size();
    }


}
