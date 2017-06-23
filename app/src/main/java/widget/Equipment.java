package widget;

import org.litepal.crud.DataSupport;

/**
 * 设备实体类
 */
public class Equipment  extends DataSupport {
    private String nickname;    //设备昵称
    private int equipid;        //设备ID
    private String password;    //设备密码
    private String imagepath;   //图片截图地址

    public Equipment(){}

    public Equipment(String nickname,int equipid,String password,String imagepath){
        this.nickname=nickname;
        this.equipid=equipid;
        this.password=password;
        this.imagepath=imagepath;
    }

    public String getNickname() {
        return nickname;
    }

    public int getEquipid() {
        return equipid;
    }

    public String getPassword() {
        return password;
    }

    public String getImagepath() {
        return imagepath;
    }
}
