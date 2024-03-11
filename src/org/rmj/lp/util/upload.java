package org.rmj.lp.util;

import org.rmj.appdriver.agent.GRiderX;
import org.rmj.lib.net.LogWrapper;

public class upload {
    public static void main(String [] args){
        LogWrapper logwrapr = new LogWrapper("upload", "RLCsftp.log");
                
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        
        GRiderX instance = new GRiderX("gRider");
        
        if (!instance.logUser("gRider", "M001111122")){
            logwrapr.severe(instance.getMessage() + instance.getErrMsg());
            System.exit(1);
        }
        
        RLCReading read = new RLCReading(instance);
        
        if (!read.uploadFile()){
            logwrapr.severe(read.getMessage());
            System.exit(1);
        }
        
        System.exit(0);
    }
}
