/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.components;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.json.JSONArray;

/**
 *
 * @author YCARRILLO
 */
@Component
public class ComponentsHelper {

    public String GetActions(String CodeM, String CodeSM, String Permits) throws IOException{
        JSONArray Array_Modules = new JSONArray(Permits);
        String Actions = "";
        for (int i = 0; i < Array_Modules.length(); i++) {
            if(Array_Modules.getJSONObject(i).getString("code").equals(CodeM)){
                JSONArray Array_Submodules = new JSONArray(Array_Modules.getJSONObject(i).getJSONArray("submodules"));
                for (int j = 0; j < Array_Submodules.length(); j++) {
                    if(Array_Submodules.getJSONObject(j).getString("code").equals(CodeSM)){
                        Actions = Array_Submodules.getJSONObject(j).getString("actions");
                    }
                }
            }
        }
        return Actions;
    }
}
