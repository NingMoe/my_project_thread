package modules;

/**
 * Created by mada on 2017/1/10.
 */

import play.filters.cors.CORSFilter;
import play.filters.gzip.GzipFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {
    @Inject
//    public Filters(GzipFilter gzip, AccessLogFilter logging) {
//        super(gzip, logging);
//    }
    public Filters(GzipFilter gzip, AccessLogFilter logging, CORSFilter corsFilter) {
        super(gzip, logging,corsFilter);
    }
}
