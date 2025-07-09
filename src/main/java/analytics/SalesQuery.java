package analytics;

import java.util.UUID;

public class SalesQuery {
    private String queryID;
    private String queryType;
    private String params;
    private String datetime;
    private int status;

    public SalesQuery() {
        // Default constructor
    }

    public SalesQuery(String queryType, String params, int status) {
        this.queryID = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        this.queryType = queryType;
        this.params = params;
        this.status = status;
    }

    public SalesQuery(String queryID, String queryType, String datetime, String params, int status) {
        this.queryID = queryID;
        this.queryType = queryType;
        this.datetime = datetime;
        this.params = params;
        this.status = status;
    }

    public String getQueryID() {
        return queryID;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getParams() {
        return params;
    }

    public int getStatus() {
        return status;
    }


}
