
import java.util.Date;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agent.GRiderX;
import org.rmj.appdriver.agentfx.CommonUtils;

public class testDateDiff {
    public static void main (String [] args){
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
            System.exit(1);
        }
        
        String lsDate1 = "2023-01-21";
        String lsDate2 = "2023-03-24";
        
        Date ldDate1 = SQLUtil.toDate(lsDate1, SQLUtil.FORMAT_SHORT_DATE);
        Date ldDate2 = SQLUtil.toDate(lsDate2, SQLUtil.FORMAT_SHORT_DATE);
        
        System.err.println(CommonUtils.dateDiff(instance.getServerDate(), ldDate1));
    }
}
