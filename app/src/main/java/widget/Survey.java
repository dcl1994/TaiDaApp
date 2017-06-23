package widget;

/**
 * 后台问卷调查题目
 */
public class Survey {
    private String IDN;     //id
    private String Title;   //标题
    private String  Introduction;  //简介
    private String Updated;     //更新时间
    private String Way;         //问卷类型
    private String Type;        //显示平台


    public Survey(String IDN,String Title,String Introduction ,String Updated,String Way,String Type){
        this.IDN=IDN;
        this.Title=Title;
        this.Introduction=Introduction;
        this.Updated=Updated;
        this.Way=Way;
        this.Type=Type;
    }

    public String getIDN() {
        return IDN;
    }

    public String getTitle() {
        return Title;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public String getUpdated() {
        return Updated;
    }

    public String getWay() {
        return Way;
    }

    public String getType() {
        return Type;
    }
}
