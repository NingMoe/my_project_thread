package job;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import play.Logger;
import scala.concurrent.duration.Duration;
import service.EliteTestRecordService;
import service.ShopEliteService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/3 13:38
 */
public class ElitePoolTask {

    @Inject
    private ShopEliteService shopEliteService;

    @Inject
    public ElitePoolTask(ActorSystem system) {
        system.scheduler().schedule(
                Duration.create(60, TimeUnit.SECONDS),
                Duration.create(300,TimeUnit.SECONDS),
                () -> {
//                    try {
                        Logger.info("开始金鹰池预警扫描");
                        shopEliteService.taskForElite();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }, system.dispatcher());
    }
}
