package app;

import analytics.QueryController;
import analytics.QueryDAO;
import analytics.SalesQuery;
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

                app.get("/metrics", ctx -> {
                    var allQueries = queries.getAllQueries();
                    if (allQueries.isEmpty()) {
                        ctx.result("No sales queries found");
                        ctx.status(404);
                    } else {
                        ctx.json(allQueries);
                        ctx.status(200);
                    }
                });

                app.get("/metrics/postcode-count/{postcode}" , ctx -> {
                    int postcode = Integer.parseInt(ctx.pathParam("postcode"));
                    queryHandler.getPostcodeCount(ctx, postcode);
                });

                app.get("/metrics/property-count/{saleID}", ctx -> {
                    int saleID = Integer.parseInt(ctx.pathParam("saleID"));
                    queryHandler.getPropertyCount(ctx, saleID);
                });

                app.post("/metrics", ctx -> {
                    var json = ctx.bodyAsClass(SalesQuery.class);
                    String request_type = json.getQueryType();
                    String param = json.getParams();
                    int status = json.getStatus();
                    queryHandler.addQuery(ctx, request_type, param, status);
                });
            });
        }
}


