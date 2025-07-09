package app;

import analytics.QueryController;
import analytics.QueryDAO;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

public class REAnalyticsServer {
        public static void main(String[] args) {

            // in memory test data store
            var queries = new QueryDAO();

            // API implementation
            QueryController queryHandler = new QueryController(queries);

            // start Javalin on port 7070
            var app = Javalin.create()
                    .get("/", ctx -> ctx.result("Real Estate Analytics Server is running"))
                    .start(8002);

            // configure endpoint handlers to process HTTP requests
            JavalinConfig config = new JavalinConfig();
            config.router.apiBuilder(() -> {
                // Sales records are immutable hence no PUT and DELETE

                app.get("/metrics/postcode-count/{postcode}" , ctx -> {
                    int postcode = Integer.parseInt(ctx.pathParam("postcode"));
                    queryHandler.getPostcodeCount(ctx, postcode);
                });

                app.get("/metrics/property-count/{saleID}", ctx -> {
                    int saleID = Integer.parseInt(ctx.pathParam("saleID"));
                    queryHandler.getPropertyCount(ctx, saleID);
                });
            });
        }
}


