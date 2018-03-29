package modules;

import com.google.inject.AbstractModule;
import job.ElitePoolTask;
import job.TestEndTask;
import job.TestRecordTask;
import job.TestTask;
import play.Logger;
import play.libs.akka.AkkaGuiceSupport;

/**
 * Created by qi_grui on 2016/12/2.
 */
public class SchedulerModule extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {
        Logger.info("启动定时任务加载执行器...");
        bind(TestRecordTask.class).asEagerSingleton();
        bind(TestEndTask.class).asEagerSingleton();
        bind(TestTask.class).asEagerSingleton();
        bind(ElitePoolTask.class).asEagerSingleton();
//        bind(SynchDatatTask.class).asEagerSingleton();
        // 注入一个继承了  UntypedActor 的 Actor 类
//         bindActor(ServiceActor.class,"serviceActor");
    }
}
