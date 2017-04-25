package widget;

/**
 * 路况的实体类
 */
public class Traffic {

    private int imageId;
    private String name;

    public Traffic(int imageId,String name){
        this.imageId=imageId;
        this.name=name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}
