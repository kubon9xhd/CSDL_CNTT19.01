package db;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

import java.io.File;

public class DBConnection {
    private static ObjectContainer db;
    
    // Phương thức khởi tạo kết nối
    public static void openConnection(String dbFilePath, Integer activationDepth, Integer updateDepth) {

        File dbFile = new File(dbFilePath);
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Existing database file deleted: " + dbFilePath);
            } else {
                System.out.println("Failed to delete existing database file: " + dbFilePath);
                return;
            }
        }


        if (db == null) {

            EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
            // Cấu hình cho phép truy cập với độ sâu = activationDepth
            if(activationDepth != null) {
                config.common().activationDepth(activationDepth);
            }
            // Cấu hình cho phép cập nhật với độ sâu = updateDepth
            if(updateDepth != null) {
            	config.common().updateDepth(updateDepth);
            }

            db = Db4oEmbedded.openFile(config, dbFilePath);
            System.out.println("The connection to the db4o database has been opened.");
        } else {
            System.out.println("The connection has been opened.");
        }
    }

    // Phương thức để lấy kết nối
    public static ObjectContainer updateConnection(Integer activationDepth, Integer updateDepth) {
    	if(activationDepth != null) db.ext().configure().activationDepth(activationDepth);
    	
    	if(updateDepth != null) db.ext().configure().activationDepth(updateDepth);
    	
        return db;
    }
    
    public static ObjectContainer getConnection() {
        return db;
    }

    // Phương thức đóng kết nối
    public static void closeConnection() {
        if (db != null) {
            db.close();
            db = null;
            System.out.println("The connection to the db4o database has been closed.");
        } else {
            System.out.println("The connection does not exist.");
        }
    }
}
