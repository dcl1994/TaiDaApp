package widget;

    /**
    *社区新闻
    * 社区物业通知都用这个实体类
    */

public class Community {

    private Integer IDN;            //id
    private String Title;           //标题
    private String Introduction;    //新闻简介
    private String Content;         //内容
    private String litpic;          //缩略图
    private String Updated;         //新闻最终更新时间
    private String Type;            //需展示的平台大屏/微信/APP  (这个可不管)


    public Community(Integer IDN,String Title,String Introduction,
                     String Content,String litpic,String Updated,String Type){
        this.IDN=IDN;
        this.Title=Title;
        this.Introduction=Introduction;
        this.Content=Content;
        this.litpic=litpic;
        this.Updated=Updated;
        this.Type=Type;
    }


    public Integer getIDN() {
        return IDN;
    }

    public String getTitle() {
        return Title;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public String getContent() {
        return Content;
    }

        public String getLitpic() {
            return litpic;
        }

        public String getUpdated() {
        return Updated;
    }

    public String getType() {
        return Type;
    }
}
