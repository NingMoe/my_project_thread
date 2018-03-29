package service;

import com.google.inject.ImplementedBy;
import com.hht.interceptor.Page;
import service.impl.PropertyInformationServiceImpl;

import java.util.Map;

/**
 * UserName:GaoQiYou
 * CreateTime:2017/11/1
 */
@ImplementedBy(PropertyInformationServiceImpl.class)
public interface PropertyInformationService  {

    Page getPropertyInformationPage(Map<String, Object> mapParams)throws Exception;

    /**
     * 测评报告展示
     * @param mapParams
     * @return
     */
    Page  getPresentationPage(Map<String, Object> mapParams)throws Exception;
}
