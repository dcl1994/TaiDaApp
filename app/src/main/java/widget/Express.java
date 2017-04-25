package widget;

/**
 * 快递查询的实体类
 */
public class Express {
    private String time;
    private String name;

    public Express(String time,String name){
        this.time=time;
        this.name=name;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
