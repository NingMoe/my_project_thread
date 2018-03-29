package service;

import com.google.inject.ImplementedBy;
import service.impl.CertificateServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/7 17:19
 */
@ImplementedBy(CertificateServiceImpl.class)
public interface CertificateService {

    List<Map<String,Object>> queryListByCode(String employeeCode);

}
