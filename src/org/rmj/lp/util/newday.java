package org.rmj.lp.util;

import java.util.Date;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agent.GRiderX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.lib.net.LogWrapper;

public class newday {
    public static void main (String [] args){
        LogWrapper logwrapr = new LogWrapper("newday", "RLCsftp.log");
        logwrapr.info("Process started.");
        
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
        
        String tenantid = System.getenv("RMS-TENANT-ID");
        String crmnumbr = System.getenv("RMS-CRM-No");
        
        if (tenantid.equals("null") || tenantid == null){
            logwrapr.severe("Tenant ID is not set in Environment Variable.");
            System.exit(1);
        }
        
        if (crmnumbr.equals("null") || crmnumbr == null){
            logwrapr.severe("CRM ID is not set in Environment Variable.");
            System.exit(1);
        }
        
        String lsDate1 = ""; //always -1 day of the requested date
        String lsDate2 = "";
        Date ldTemp;
        
        switch (args.length) {
            case 0:
                lsDate1 = SQLUtil.dateFormat(CommonUtils.dateAdd(instance.getServerDate(), -1), SQLUtil.FORMAT_SHORT_DATE);
                lsDate2 = SQLUtil.dateFormat(instance.getServerDate(), SQLUtil.FORMAT_SHORT_DATE);
                break;
            case 2:
                if (!CommonUtils.isDate(args[0], SQLUtil.FORMAT_SHORT_DATE) ||
                        !CommonUtils.isDate(args[1], SQLUtil.FORMAT_SHORT_DATE)){
                    logwrapr.severe("Invalid date range format.");
                    System.exit(1);
                }   
                
                ldTemp = SQLUtil.toDate(args[0], SQLUtil.FORMAT_SHORT_DATE);
                lsDate1 = SQLUtil.dateFormat(CommonUtils.dateAdd(ldTemp, -1), SQLUtil.FORMAT_SHORT_DATE);
                lsDate2 = args[1];
                break;
            case 3:
                if (!CommonUtils.isDate(args[0], SQLUtil.FORMAT_SHORT_DATE) ||
                        !CommonUtils.isDate(args[1], SQLUtil.FORMAT_SHORT_DATE)){
                    logwrapr.severe("Invalid date range format.");
                    System.exit(1);
                }   
                
                ldTemp = SQLUtil.toDate(args[0], SQLUtil.FORMAT_SHORT_DATE);
                lsDate1 = SQLUtil.dateFormat(CommonUtils.dateAdd(ldTemp, -1), SQLUtil.FORMAT_SHORT_DATE);
                lsDate2 = args[1];
                crmnumbr = args[2];
                break;
            default:
                logwrapr.severe("Invalid export date range.");
                System.exit(1);
        }
        
        RLCReading read = new RLCReading(instance, tenantid);
        if (!read.initMachine(crmnumbr)){
            logwrapr.severe(read.getMessage());
            System.exit(1);
        }
        
        Date ldDate1;
        Date ldDate2;
        
        ldDate1 = SQLUtil.toDate(lsDate1, SQLUtil.FORMAT_SHORT_DATE);
        ldDate2 = SQLUtil.toDate(lsDate2, SQLUtil.FORMAT_SHORT_DATE);
        
        while (0 != 1){
            ldDate1 = CommonUtils.dateAdd(ldDate1, 1);
            lsDate1 = SQLUtil.dateFormat(ldDate1, SQLUtil.FORMAT_SHORT_DATE);
            
            logwrapr.info("Trying to create sales file.");
            if (!read.createFile(lsDate1.replace("-", ""), 1)){
                logwrapr.severe(read.getMessage());
                System.exit(1);
            } else {
                logwrapr.info(read.getMessage());
            }
            
            if (CommonUtils.dateDiff(ldDate1, ldDate2) >= 0) break;
        }
        
        logwrapr.info("Process ended succesfully.");
    }
}
