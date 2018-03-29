package service;

import com.google.inject.ImplementedBy;
import service.impl.EliteAppSignUpServiceImpl;

import java.util.Map;

/**
 * Created by MR.GANG on 2017/7/19.
 */
@ImplementedBy(EliteAppSignUpServiceImpl.class)
public interface EliteAppSignUpService {
    int EmployeeSignUp(Map<String, Object> mapParams)throws Exception;
}
