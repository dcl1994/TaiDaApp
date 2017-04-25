package widget;

/**
 * 设备实体类
 */
public class Equipment {
    private String name;    //设备昵称
    private String equipmentId ;    //设备ID
    private String pwd;     //设备连接密码


    public Equipment(String name,String equipmentId,String pwd){
        this.name=name;
        this.equipmentId=equipmentId;
        this.pwd=pwd;
    }
    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getEquipmentId() {
        return equipmentId;
    }
}
