package widget;

/**
 * 调查问卷题目
 */
public class SurveyQuesition {
    private String SurveyD_ID;  //ID
    private String SurveyD_PID; //关联的主表ID
    private String SurveyD_QID; //题号
    private String SurveyD_Topic; //题目内容
    private String SurveyD_Seq;   //选项序列号
    private String SurveyD_Choice; //选项内容

    /**
     * 构造函数
     * @param SurveyD_ID
     * @param SurveyD_PID
     * @param SurveyD_QID
     * @param SurveyD_Topic
     * @param SurveyD_Seq
     * @param SurveyD_Choice
     */
    public SurveyQuesition(String SurveyD_ID,String SurveyD_PID,String SurveyD_QID,String SurveyD_Topic
    ,String SurveyD_Seq ,String SurveyD_Choice){
        this.SurveyD_ID=SurveyD_ID;
        this.SurveyD_PID=SurveyD_PID;
        this.SurveyD_QID=SurveyD_QID;
        this.SurveyD_Topic=SurveyD_Topic;
        this.SurveyD_Seq=SurveyD_Seq;
        this.SurveyD_Choice=SurveyD_Choice;
    }

    public String getSurveyD_ID() {
        return SurveyD_ID;
    }

    public String getSurveyD_PID() {
        return SurveyD_PID;
    }

    public String getSurveyD_QID() {
        return SurveyD_QID;
    }

    public String getSurveyD_Topic() {
        return SurveyD_Topic;
    }

    public String getSurveyD_Seq() {
        return SurveyD_Seq;
    }

    public String getSurveyD_Choice() {
        return SurveyD_Choice;
    }
}
