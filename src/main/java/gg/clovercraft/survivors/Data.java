package gg.clovercraft.survivors;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.sql.*;

import org.slf4j.Logger;

public class Data {

    private final Path URL;
    private Connection CONN = null;
    private final Logger LOGGER;
    public ResultSet RESULT = null;

    public Data( Logger logger ) {
        LOGGER = logger;
        URL = FabricLoader.getInstance().getConfigDir().resolve( "survivorsdata.db" );
        boolean connected = connect();
        if( connected ) {
            migrate();
        }
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

    private boolean connect() {
        try( Connection conn = DriverManager.getConnection( URL.toString() ) ) {
            CONN = conn;
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
        String sql = """
                CREATE TABLE IF NOT EXISTS player (
                     id integer PRIMARY KEY,\s
                     uuid text NOT NULL,
                     name text NOT NULL,
                     lives INTEGER NOT NULL,\s
                     isHunter  INTEGER NOT NULL DEFAULT 0,\s
                     isVampire INTEGER NOT NULL DEFAULT 0,\s
                     isSpartan INTEGER NOT NULL DEFAULT 0,\s
                );""";
        if( runSql( sql ) ) {
            LOGGER.info( "Successfully migrated DB" );
        }
    }

    private boolean runSql( String sql ) {
        boolean success = false;
        try {
            assert CONN != null;
            try (Statement stmt = CONN.createStatement()) {
                success = stmt.execute(sql);
                RESULT = stmt.getResultSet();
            }
        } catch ( SQLException e ) {
            LOGGER.error( "DB Error: " );
            LOGGER.error( e.getMessage() );
        }
        return success;
    }

}
