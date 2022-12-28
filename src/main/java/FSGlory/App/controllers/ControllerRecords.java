/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import FSGlory.App.components.ComponentsQuery;
import kong.unirest.UnirestException;

@Controller
public class ControllerRecords {
    @Autowired
    private ComponentsQuery Query;
    @Autowired
    private ControllerMain Main;

    @GetMapping({"/MOVEMENTS"})
    public String MOVEMENTSpage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","MOVEMENTS");
            JSONObject Device = new JSONObject();
            Device.put("table" , "devices");
            Device.put("member", session.getAttribute("IDMEMBER"));
            model.addAttribute("device", Query.GetData(request, Device, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/RECORDS/movements";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @RequestMapping(value = "/RECORDS/Search", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String Search(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            String DateIn   = request.getParameter("date_range").split("-")[0].trim();
            String DateOut  = request.getParameter("date_range").split("-")[1].trim();
            Data.put("date_in"    , DateIn.split("/")[2]+DateIn.split("/")[1]+DateIn.split("/")[0]);
            Data.put("date_out"   , DateOut.split("/")[2]+DateOut.split("/")[1]+DateOut.split("/")[0]);
            Data.put("device"     , request.getParameter("device")     == null ? ""  : request.getParameter("device"));
            Data.put("operation"  , request.getParameter("operation")  == null ? ""  : request.getParameter("operation"));
            Data.put("member"     , session.getAttribute("IDMEMBER").toString());
            Data.put("action"     , "movements_records");
            JSONArray Response  = Query.GetRequest(request, "SEARCH_API_AS400", Data, session.getAttribute("TOKEN").toString(), "ADMIN").getJSONArray("data");
            return Response.toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
}