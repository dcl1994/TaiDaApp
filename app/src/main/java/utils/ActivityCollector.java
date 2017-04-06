package utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用一个专门的类来管理所有的活动
 */
public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();

    /**
     * 向list中添加活动
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * 从list中移除一个活动
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity:activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }


}
