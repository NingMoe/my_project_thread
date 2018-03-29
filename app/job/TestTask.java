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
 * @Date: 2017/8/2 10:41
 */
public class TestTask {

    @Inject
    private EliteTestRecordService testRecordService;

    @Inject
    public TestTask(ActorSystem system) {
        system.scheduler().schedule(
                Duration.create(60, TimeUnit.SECONDS),
                Duration.create(5,TimeUnit.HOURS),
                () -> {
                    try {
                        Logger.info("开始获取未收到测评结果列表");
                        testRecordService.getTestNotUrl();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, system.dispatcher());
    }

}
