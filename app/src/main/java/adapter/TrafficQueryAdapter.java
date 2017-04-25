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
 * 路况查询的Adapter
 */
public class TrafficQueryAdapter extends BaseAdapter{
    private Context mContext;
    private TextView maddress;

    JSONArray jsonArray;


    public TrafficQueryAdapter(Context mContext,JSONArray jsonArray){
        this.mContext=mContext;
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
        view= LayoutInflater.from(mContext).inflate(R.layout.traffic_item,null);
        maddress= (TextView) view.findViewById(R.id.traf_textview);
        try {
            maddress.setText(jsonArray.getJSONObject(position).opt("").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
