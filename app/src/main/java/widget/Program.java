package widget;

/**
 *电视台的实体类
 */
public class Program {
    private Integer IDN;    //ID
    private String  Title;    //名称
    private String Link;      //直播地址
    private String litpic;    //台标
    private String Updated;   //更新时间


    public Program(Integer IDN,String Title,String Link,String litpic ,String Updated){
        this.IDN=IDN;
        this.Title=Title;
        this.Link=Link;
        this.litpic=litpic;
        this.Updated=Updated;
    }


    public Integer getIDN() {
        return IDN;
    }

    public String getTitle() {
        return Title;
    }

    public String getLink() {
        return Link;
    }

    public String getLitpic() {
        return litpic;
    }

    public String getUpdated() {
        return Updated;
    }
}
