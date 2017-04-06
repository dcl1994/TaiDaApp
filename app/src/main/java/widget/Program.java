package widget;

/**
 *电视台的实体类
 */
public class Program {
    private String name;    //电视节目名称
    private int imageId;    //图片的ID

    public  Program(String name,int imageId){
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
