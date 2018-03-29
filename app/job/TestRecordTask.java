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
 * @Date: 17/7/24 19:35
 */
public class TestRecordTask {

    @Inject
    private EliteTestRecordService testRecordService;

    @Inject
    public TestRecordTask(ActorSystem system) {
        system.scheduler().schedule(
                Duration.create(60,TimeUnit.SECONDS),
                Duration.create(10,TimeUnit.MINUTES),
                () -> {
                    try {
                        Logger.info("开始获取测评完结列表");
                        testRecordService.getTestNotRecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, system.dispatcher());
    }
}
