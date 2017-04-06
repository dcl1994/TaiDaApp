package widget;

/**
 * 设备实体类
 */
public class Equipment {
    private String name;    //设备昵称
    private int imageId;    //设备图片

    public Equipment(String name,int imageId){

        this.name=name;
        this.imageId=imageId;
    }
    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
