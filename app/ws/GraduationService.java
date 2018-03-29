package ws;

import com.google.inject.ImplementedBy;
import models.ProEliteClass;
import models.ProEliteClassPerson;
import org.dom4j.DocumentException;
import vo.ProEliteClassPersonVo;
import ws.Impl.GraduationServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/3 09:54
 */

@ImplementedBy(GraduationServiceImpl.class)
public interface GraduationService {

    void sendGraduationResult(List<ProEliteClassPerson> list, ProEliteClass eliteClass) throws IOException, DocumentException;
}
