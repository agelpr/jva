/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.components;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
/**
 *
 * @author YCARRILLO
 */
@Component
public class ComponentsQuery {
    @Autowired
    private ComponentsMessage ComponentsMessage;
    @Value("${api.host}")
    private String host;
    
    public String sendMessage(String msg,String enpoint) throws UnirestException{
        HttpResponse<String> response = Unirest.post(enpoint)
          .header("key", "5556d5a7f3764b07b3dbeae3fab15ffb")
          .header("Content-Type", "text/plain")
          .body(msg)
          .asString();
        System.err.println("----------------------------------------------------");
        System.err.println(response.getBody());
        System.err.println("----------------------------------------------------");
        return response.getBody();
    }
    public JSONObject GetRequest(HttpServletRequest request, String Program, JSONObject Data, String Token, String Origin) throws IOException, UnirestException{
        String enpoint = null;
        if (Token.equals("") == true) {
            enpoint=host+"/api/general";
        }else{
            enpoint=host+"/api";
        }
        String Requests = ComponentsMessage.MakeMessage(
                Token,
                Program,
                Data,
                Origin
        );
        JSONObject response = new JSONObject(sendMessage(Requests,enpoint));
       
        return response;
    }
    // public JSONObject GetArrayRequest(HttpServletRequest request, String program, JSONArray Data, String Token, String Origin) throws IOException, UnirestException{
    //     String Requests = ComponentsMessage.MakeArrayMessage(
    //             Token,
    //             program,
    //             Data, 
    //             Origin
    //     );
    //     JSONObject response = new JSONObject(sendMessage(Requests,host+"/api"));
       
    //     return response;
    // }
    public JSONArray GetData(HttpServletRequest request,JSONObject Data, String Token, String Origin, String Program) throws IOException, UnirestException{
        String enpoint = null;
        if (Token.equals("") == true) {
            enpoint=host+"/api/general";
        }else{
            enpoint=host+"/api";
        }
        String Select = ComponentsMessage.MakeMessage(
                Token,
                Program,
                Data,
                Origin
        );
        JSONObject response = new JSONObject(sendMessage(Select,enpoint));
        return response.getJSONArray("data");
    }
    public Boolean CheckSession(HttpSession session, String Token, String Origin) throws IOException, UnirestException{
        JSONObject Data = new JSONObject();
        Data.put("id_users", session.getAttribute("IDUSR"));
        Data.put("table"   , "check_session");
        String enpoint = null;
        if (Token.equals("") == true) {
            enpoint=host+"/api/general";
        }else{
            enpoint=host+"/api";
        }
        String Requests = ComponentsMessage.MakeMessage(
                Token,
                "FILTER",
                Data,
                Origin
        );
        JSONObject Response = new JSONObject(sendMessage(Requests,enpoint));
        return Response.getJSONObject("response").getInt("code") == 200 ? true : false;
    }
}
