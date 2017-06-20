import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.et.util.DubboInit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by shatao on 13/6/2017.
 */
public class TestDubboClient {

    /*
     *定义DirectRegisterInfoInf的变量
     */
    @Autowired
//    private DirectRegisterInfoInf directRegisterInfoInf;

    /*
     *初始化directRegisterInfoService服务
     */
    @Before
    public void setupTest() {
        DubboInit init = DubboInit.getInstance();
        init.initApplicationContext();
//        directRegisterInfoInf = (DirectRegisterInfoInf)init.getBean("directRegisterInfoService");
    }

    /*
     *测试代码
     */
    @Test
    public void runTest() {
/*        List<DirectRegisterInfo> list = new ArrayList<DirectRegisterInfo>();
        try {
            Map map = new HashMap();
            map.put("dateTime", new DateTime().toString("yyyy-MM-dd 00:00:00"));// TODO
            // map.put("dateTime","2016-01-26 00:00:00");
            map.put("hosCode", "T113411");
            map.put("cardNo", "98000001004327");
            list = directRegisterInfoInf.getTakeNoList(map);
            System.out.println("\n\n\n\n================"+list.get(0).getDeptname() + "==================\n\n\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
    */
    }

}
