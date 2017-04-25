package widget;

import com.libhttp.entity.HttpResult;

/**
 * 获取手机验证码的时候返回的泛型
 */
public class GetAccountByPhoneNOResult extends HttpResult {
    private String ID;
    private String VKey;
    private String CountryCode;
    private String PhoneNO;


    public void setID(String ID) {
        this.ID = ID;
    }


    public void setVKey(String vkey) {
        VKey=vkey;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public void setPhoneNO(String phoneNO) {
        PhoneNO = phoneNO;
    }

    public String getID() {
        return ID;
    }


    /**
     * 生成一个随机数
     * @return
     */
    public String getVKey() {
        String usId;
        try {
            usId = "0" + String.valueOf((Integer.parseInt(VKey) & 0x7fffffff));
            return usId;
        } catch (NumberFormatException e) {
            return VKey;
        }
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public String getPhoneNO() {
        return PhoneNO;
    }
}
