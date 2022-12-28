/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.components;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;
import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class ComponentsMessage {
    
    public String MakeMessage(String Token, String program, JSONObject Data, String Origin) throws IOException {
        String msg = null;
        LocalDateTime myDateObj       = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ThreadLocalRandom rn          = ThreadLocalRandom.current();
        
        JSONObject json = new JSONObject();
        json.put("request"  , program);
        json.put("origin"   , Origin);
        json.put("timestamp", myDateObj.format(myFormatObj));
        json.put("token"    , Token);
        if (!Data.isEmpty()) {
            json.put("data" , Data);
        }
        msg = rn.nextInt(1, 5) +json.toString();

        System.err.println("----------------------------------------------------");
        System.err.println(msg);
        System.err.println("----------------------------------------------------");
        return msg;
    }
    public String MakeArrayMessage(String Token, String Program, JSONArray Data, String Origin) throws IOException {
        String msg = null;
        LocalDateTime myDateObj       = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ThreadLocalRandom rn          = ThreadLocalRandom.current();
        
        JSONObject json = new JSONObject();
        json.put("request"  ,Program);
        json.put("origin"   ,Origin);
        json.put("timestamp",myDateObj.format(myFormatObj));
        json.put("token"    ,Token);
        if (!Data.isEmpty()) {
            json.put("data" ,Data);
        }
        msg = rn.nextInt(1, 5) +json.toString();

        return msg;
    }
}
