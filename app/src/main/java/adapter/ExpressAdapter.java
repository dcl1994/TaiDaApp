package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siyann.taidaapp.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 获取物流信息的适配器
 */
public class ExpressAdapter extends BaseAdapter {
    private Context context;
    private TextView mcontent;
    private TextView mtime;

    JSONArray jsonArray;

    public ExpressAdapter(Context context,JSONArray jsonArray){
        this.context=context;
        this.jsonArray=jsonArray;
    }




    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view= LayoutInflater.from(context).inflate(R.layout.expresscheck_item,null);
        mcontent= (TextView) view.findViewById(R.id.textview_content);
        mtime= (TextView) view.findViewById(R.id.textview_time);
        try {

            mcontent.setText(jsonArray.getJSONObject(position).opt("AcceptStation").toString());
            mtime.setText(jsonArray.getJSONObject(position).opt("AcceptTime").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
