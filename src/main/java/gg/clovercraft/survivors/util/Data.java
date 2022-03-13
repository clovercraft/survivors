package gg.clovercraft.survivors.util;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;

public class Data {

    private final String URL;
    private final Logger LOGGER;
    public ResultSet RESULT = null;

    public Data( Logger logger ) {
        LOGGER = logger;
        URL = "jdbc:sqlite:" + FabricLoader.getInstance().getConfigDir().resolve( "survivorsdata.db" );
        migrate();
    }

    public void query( String sql ) {
        runSql( sql );
    }

    public boolean next() {
        boolean gotNext = false;
        try {
            gotNext = RESULT.next();
        } catch ( SQLException e ) {
            LOGGER.warn( e.getMessage() );
        }
        return gotNext;
    }

    public boolean single() {
        boolean success = false;
        try {
            success = RESULT.last();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    private Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection( URL );
        if ( conn != null ) {
            LOGGER.info("Connected to DB");
        }
        return conn;
    }

    private void migrate() {
        String sql = """
                CREATE TABLE IF NOT EXISTS survivors (
                     id integer PRIMARY KEY,
                     uuid text NOT NULL,
                     name text NOT NULL,
                     lives INTEGER NOT NULL,
                     isHunter  INTEGER NOT NULL DEFAULT 0,
                     isVampire INTEGER NOT NULL DEFAULT 0,
                     isSpartan INTEGER NOT NULL DEFAULT 0
                );""";
        if( runSql( sql ) ) {
            LOGGER.info( "Successfully migrated DB" );
        }
    }

    private boolean runSql( String sql ) {
        boolean success = false;
        try {
            Connection CONN = connect();
            Statement stmt = CONN.createStatement();
            success = stmt.execute(sql);
            RESULT = stmt.getResultSet();
            CONN.close();
        } catch ( SQLException e ) {
            LOGGER.error( "Error running SQL" );
            LOGGER.error( e.getMessage() );
        }
        return success;
    }

}
