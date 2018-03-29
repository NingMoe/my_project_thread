package modules;

import akka.stream.Materializer;
import com.google.inject.Inject;
import play.Logger;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class AccessLogFilter extends Filter {
    @Inject
    public AccessLogFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {

        if(!"/status".equals(requestHeader.uri())) {
            Logger.info("收到请求: {} {} ", requestHeader.method(), requestHeader.uri());
        }

        long startTime = System.currentTimeMillis();

        return nextFilter.apply(requestHeader).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;
            if(!"/status".equals(requestHeader.uri())) {
                Logger.info("返回请求：{} {} took {}ms and returned {}",
                        requestHeader.method(), requestHeader.uri(),
                        requestTime, result.status());
            }
            return result.withHeader("Request-Time", Long.toString(requestTime)).withHeader("Cache-Control", "no-cache");
        });
    }

}