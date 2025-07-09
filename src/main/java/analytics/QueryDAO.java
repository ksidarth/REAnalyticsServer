package analytics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




public class QueryDAO {

   private Connection connection;


   public QueryDAO() {
    try {
        String url = "jdbc:postgresql://ep-wild-cherry-a7rmik76-pooler.ap-southeast-2.aws.neon.tech/neondb?user=neondb_owner&password=npg_7MXptljGJe0D&sslmode=require&channelBinding=require";
        String user = "neondb_owner";
        String password = "npg_HavSVn2Zy6bi";
 
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to Neon database!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Connected to Neon PostgreSQL database (JDBC).");
        } catch (Exception e) {
            System.err.println("Failed to connect to Neon PostgreSQL: " + e.getMessage());
        }
    }

    public List<SalesQuery> getAllQueries() {
       List<SalesQuery> allSales = new ArrayList<>();
       try (Statement stmt = connection.createStatement()) {
            String sqlStr = "SELECT * from sales LIMIT 100";
            ResultSet rs = stmt.executeQuery(sqlStr);
            while (rs.next()) {
                allSales.add(rsToSalesQuery(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
        return allSales;
   }


    private SalesQuery rsToSalesQuery(ResultSet rs) {
        try {
            String queryID = rs.getString("query_id");
            String queryType = rs.getString("query_type");
            String datetime = rs.getString("query_datetime");
            String params = rs.getString("query_params");
            int status = rs.getInt("status");

            return new SalesQuery(queryID, queryType, datetime, params, status);
        } catch (SQLException e) {
            System.err.println("Error converting ResultSet to SalesQuery: " + e.getMessage());
            return null; // or throw an exception based on your error handling strategy
        }
    }

    public int getPostcodeCount(int postcode) {
        String cond = "'post_code=" + postcode + "'";
        String query = "SELECT COUNT(*) from sales_query where query_params = " + cond;
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; 
    }
        return 0;
}

    public int getPropertyCount(int propertyId) {
        String cond = "'property_id=" + propertyId + "'";
        String query = "SELECT COUNT(*) FROM sales_query WHERE query_params = " + cond;
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public void addQuery(SalesQuery query) {
        String queryID = query.getQueryID();
        String queryType = query.getQueryType();
        String params = query.getParams();
        int status = query.getStatus();

        String sql = String.format(
            "INSERT INTO sales_query (query_id, query_type, query_datetime, query_params, status) VALUES ('%s', '%s', CURRENT_DATE, '%s', %d)",
            queryID, queryType, params, status
            );

        try (Statement stmt = this.connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
    }
    }

}   