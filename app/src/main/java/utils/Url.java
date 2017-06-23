package utils;

/**
 * 存放所有接口地址
 */
public class Url {

    /**
     * 天气查询接口（阿凡达天气查询）
     *
     */
    public static final String ProcessUrl="http://api.avatardata.cn/Weather/Query?key=d7e035e378034cf58372e67ff41dec75";


    /**
     * 用户接口
     */
    public static String BaseUrl="http://www.tedacatv.net/community/api.php?_R=Modules&_M=JDI&_C=User&_A=";


    /**
     * 注册
     * "username":"","password":"","open_id":"","nickname":""
     */
    public static final String register=BaseUrl+"register";



    /**
     * 登录
     * 参数："username":"","password":"","open_id":""
     */
    public static final String login=BaseUrl+"login";


    /**
     * 绑定微信
     * 参数："username":"","open_id":""
     */
    public static final String Bind_WeChat=BaseUrl+"Bind_WeChat";


    /**
     * 修改密码
     * "username":"","uid":"","password":""
     */
    public static final String updatePassword=BaseUrl+"updatePassword";

    //--------------------------------------以下是微信端的接口-------------------------------------------------------

    /**
     *微信端获取后台社区新闻数据信息
     */
    public static final String CommunityNews="http://121.42.32.107:8001/News.asmx/NewsTo?UserID=FHMS&Password=FHMS2016";

    /**
     * 微信端获取后台物业通知数据信息
     */
    public static final String PropertyNotice="http://121.42.32.107:8001/Notice.asmx/NoticeTo?UserID=FHMS&Password=FHMS2016";


    /**
     * 微信端获取后台社区简介数据信息
     */
    public static final String CommunityProfile="http://121.42.32.107:8001/Introduction.asmx/IntroductionTo?UserID=FHMS&Password=FHMS2016";



    /**
     *微信端获取后台问卷调查题目信息
     */
    public static final String Survey="http://121.42.32.107:8001/Survey.asmx/SurveyTo?UserID=FHMS&Password=FHMS2016";


    /**
     *微信端获取后台缤纷活动数据信息
     */
    public static final String ColorfulActivity="http://121.42.32.107:8001/Activity.asmx/ActivityTo?UserID=FHMS&Password=FHMS2016";


    /**
     *微信端获取后台养生小知识数据信息
     */
    public static final String SmallKnowledge="http://121.42.32.107:8001/Health.asmx/HealthTo?UserID=FHMS&Password=FHMS2016";


    /**
     * 微信端获取后台饮食文化数据信息
     */
    public static final String  CookingCulture="http://121.42.32.107:8001/Culture.asmx/CultureTo?UserID=FHMS&Password=FHMS2016";

    /**
     * 微信端获取后台路况
     */
    public static  final String RoadLook="http://121.42.32.107:8001/Roadcheck.asmx/RoadcheckTo?UserID=FHMS&Password=FHMS2016";

}
