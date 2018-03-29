package job;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import play.Logger;
import scala.concurrent.duration.Duration;
import service.EliteTestRecordService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/10 10:57
 */
public class TestEndTask {

    @Inject
    private EliteTestRecordService testRecordService;

    @Inject
    public TestEndTask(ActorSystem system) {
        system.scheduler().schedule(
                Duration.create(60, TimeUnit.SECONDS),
                Duration.create(3600,TimeUnit.SECONDS),
                () -> {
                    try {
                        Logger.info("开始获取测评完结列表");
                        testRecordService.getTestEnd();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, system.dispatcher());
    }
}
