package utils;

/**
 * item的实体类
 */
public class ItemUtil {
    private String title;   //标题
    private String content; //内容
    private int imageId;    //图片id

    public ItemUtil(String title,String content,int imageId){
        this.title=title;
        this.content=content;
        this.imageId=imageId;

    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getImageId() {
        return imageId;
    }
}
