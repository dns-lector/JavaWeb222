package learning.itstep.javaweb222.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import learning.itstep.javaweb222.services.config.ConfigService;

@Singleton
public class DataAccessor {
    private final ConfigService configService;
    private final Logger logger;
    private Connection connection;
    private Driver mysqlDriver;

    @Inject
    public DataAccessor(ConfigService configService, Logger logger) {
        this.configService = configService;
        this.logger = logger;
    }
    
    public Connection getConnection() {
        if(this.connection == null) {
            String connectionString;
            try {  
                connectionString = 
                    configService.get("connectionStrings.mainDb");
            }
            catch(NoSuchFieldError err) {
                throw new RuntimeException(
                        "DataAccessor::getConnection Connection String not found 'connectionStrings.mainDb' " 
                        + err.getMessage());
            }
            try {
                mysqlDriver = new com.mysql.cj.jdbc.Driver();
                DriverManager.registerDriver(mysqlDriver);
                connection = DriverManager.getConnection(connectionString);
            }
            catch(SQLException ex) {
                throw new RuntimeException(
                        "DataAccessor::getConnection Connection not opened " 
                        + ex.getMessage());
            }
        }
        return this.connection;
    }
    
    public UUID getDbIdentity() {
        String sql = "SELECT UUID()";
        try(Statement statement = this.getConnection().createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return UUID.fromString( rs.getString(1) );
        }
        catch(SQLException ex) {
            logger.log(Level.WARNING, "DataAccessor::getDbIdentity " 
                    + ex.getMessage() + " | " + sql);
        }
        return null;
    }
    
    
}
