package widget;

/**
 * 便民号码的实体类
 */
public class ConveniencePhone {

    private String companyname; //公司名称

    private String phone;       //电话号码

    private String phonenight;    //电话白天

    private String day;         //白天

    private String night;       //晚上


    public  ConveniencePhone(String companyname,String phone,String phonenight,String day,String night){
        this.companyname=companyname;
        this.phone=phone;
        this.phonenight=phonenight;
        this.day=day;
        this.night=night;
    }
    public String getCompanyname() {
        return companyname;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhonenight() {
        return phonenight;
    }

    public String getDay() {
        return day;
    }

    public String getNight() {
        return night;
    }
}
