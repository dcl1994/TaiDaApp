package widget;

/**
 * 路况
 */
public class Road {
    private String IDN;         //ID
    private String Title;       //标题
    private String Link;        //链接
    private String Updated;     //更新时间

    public Road(String IDN,String Title,String Link,String Updated){
        this.IDN=IDN;
        this.Title=Title;
        this.Link=Link;
        this.Updated=Updated;
    }

    public String getIDN() {
        return IDN;
    }

    public String getTitle() {
        return Title;
    }

    public String getLink() {
        return Link;
    }

    public String getUpdated() {
        return Updated;
    }
}
