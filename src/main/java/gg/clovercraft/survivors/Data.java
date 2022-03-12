package gg.clovercraft.survivors;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.sql.*;

import org.slf4j.Logger;

public class Data {

    private Path URL = null;
    private Connection CONN = null;
    private Logger LOGGER = null;
    public ResultSet RESULT = null;

    public Data( Logger logger ) {
        LOGGER = logger;
        URL = FabricLoader.getInstance().getConfigDir().resolve( "survivorsdata.db" );
        Boolean connected = connect();
        if( connected ) {
            migrate();
        }
    }

    public boolean query( String sql ) {
        return runSql( sql );
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

    private boolean connect() {
        try( Connection CONN = DriverManager.getConnection( URL.toString() ) ) {
            if ( CONN != null ) {
                LOGGER.info( "Connected to DB" );
                migrate();
            }
        } catch ( SQLException e ) {
            LOGGER.error( "Failed to connect to DB" );
            LOGGER.error( e.toString() );
            return false;
        }
        return true;
    }

    private void migrate() {
        String sql = "CREATE TABLE IF NOT EXISTS player (\n"
                + "     id integer PRIMARY KEY, \n"
                + "     uuid text NOT NULL,\n"
                + "     name text NOT NULL,\n"
                + "     lives INTEGER NOT NULL, \n"
                + "     isHunter  INTEGER NOT NULL DEFAULT 0, \n"
                + "     isVampire INTEGER NOT NULL DEFAULT 0, \n"
                + "     isSpartan INTEGER NOT NULL DEFAULT 0, \n"
                + ");";
        if( runSql( sql ) ) {
            LOGGER.info( "Successfully migrated DB" );
        }
    }

    private boolean runSql( String sql ) {
        boolean success = false;
        try (Statement stmt = CONN.createStatement()) {
            success = stmt.execute(sql);
            RESULT = stmt.getResultSet();
        } catch ( SQLException e ) {
            LOGGER.error( "DB Error: " );
            LOGGER.error( e.getMessage() );
        }
        return success;
    }

}
