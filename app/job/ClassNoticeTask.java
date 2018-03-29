package job;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import play.Logger;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * <b></b></br>
 *
 * @Author: @Mr.wang
 * @Date: 2017/8/14 17:19
 */
public class ClassNoticeTask {

    @Inject
    public ClassNoticeTask(ActorSystem system) {
        system.scheduler().schedule(
                Duration.create(60, TimeUnit.SECONDS),
                Duration.create(300,TimeUnit.SECONDS),
                () -> {
//                    try {
                    Logger.info("开始金鹰池预警扫描");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }, system.dispatcher());
    }
}
