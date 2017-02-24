package bean;

/**
 * 错误信息
 */
public class ROConsult {
    /**
     * 响应信息
     */
    public String response = "";

    /**
     * 错误信息
     */
    public ResError error=new ResError();

    /**
     * 协商内容
     */
    public ConsultContest consult = new ConsultContest();

}
