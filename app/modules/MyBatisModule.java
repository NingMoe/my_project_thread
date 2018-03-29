package modules;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.hht.interceptor.GenCreateInterceptor;
import com.hht.interceptor.PageInterceptor;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import play.db.Database;

import javax.sql.DataSource;

public class MyBatisModule extends org.mybatis.guice.MyBatisModule {

    @Override
    protected void initialize() {
        environmentId("development");
        bindConstant().annotatedWith(
                Names.named("mybatis.configuration.failFast")).
                to(true);
        bindDataSourceProviderType(PlayDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        // 注入Mapper包中的所有Mapper类
        addMapperClasses("mapper");

        // 添加MyBatis分页拦截器
        addInterceptorClass(PageInterceptor.class);

        // 添加注解生成拦截器
        addInterceptorClass(GenCreateInterceptor.class);

    }
    // private static final Injector injector =

    /* Provides a {@link DataSource} from the {@link Database} which can be injected from Play. */
    @Singleton
    public static class PlayDataSourceProvider implements Provider<DataSource> {
        final Database db;

        @Inject
        public PlayDataSourceProvider(final Database db) {
            this.db = db;
        }


        @Override
        public DataSource get() {
            return db.getDataSource();
        }
    }

}