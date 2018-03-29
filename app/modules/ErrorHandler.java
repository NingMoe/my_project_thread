package modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.http.HttpErrorHandler;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

@Singleton
public class ErrorHandler implements HttpErrorHandler {
    private static Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(
                Results.status(statusCode, "A client error occurred: " + message)
        );
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        exception.printStackTrace();
//        log.error();
        return CompletableFuture.completedFuture(
                Results.internalServerError("A server error occurred: " + exception.getMessage())
        );
    }
}