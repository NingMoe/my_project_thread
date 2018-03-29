package ws;

import com.google.inject.ImplementedBy;
import models.ProEliteClass;
import models.ProEliteClassPerson;
import models.ProEliteTrainCost;
import models.ProShopElite;
import org.dom4j.DocumentException;
import ws.Impl.WsCostServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/8 15:20
 */

@ImplementedBy(WsCostServiceImpl.class)
public interface WsCostService {

    void sendCostResult(List<ProEliteClassPerson> list, ProEliteClass eliteClass, String costNode) throws IOException, DocumentException;

    void sendTestCostResult(List<ProShopElite> list) throws IOException, DocumentException;
}
