package widget;

/**
 * 设置界面的实体类
 * 每个子item
 */
public class Setting {
    private String name;
    private int imageId;


    public Setting(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageid() {
        return imageId;
    }
}
