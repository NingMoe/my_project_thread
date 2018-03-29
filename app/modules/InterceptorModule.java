package modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.hht.interceptor.ExceptionInterceptor;

/**
 * 配置aop
 *
 * Guice支持AOP的条件是：
 * 类必须是public或者package (default)
 * 类不能是final类型的
 * 方法必须是public,package或者protected
 * 方法不能使final类型的
 * 实例必须通过Guice的@Inject注入或者有一个无参数的构造函数
 * @author zhujp
 * @version 1.0
 * @date 2016/11/30
 */
public class InterceptorModule implements Module {

    @Override
    public void configure(Binder binder) {

        /**
         * 配置aop
         * 使用方式：方法上添加  @Named("hhtException")
         */
        binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Names.named("hhtException")), new ExceptionInterceptor());
    }
}
