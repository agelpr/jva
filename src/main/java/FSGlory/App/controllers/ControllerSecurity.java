/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.controllers;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
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

import FSGlory.App.components.ComponentsHelper;
import FSGlory.App.components.ComponentsQuery;
import FSGlory.App.services.MailService;
import kong.unirest.UnirestException;

@Controller
public class ControllerSecurity {
    @Autowired
    private MailService MailService;
    @Autowired
    private ComponentsQuery Query;
    @Autowired
    private ControllerMain Main;
    @Autowired
    private ComponentsHelper Helper;
    @GetMapping({"/GROUPS"})
    public String GROUPSPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","GROUPS");
            String Actions = Helper.GetActions("SECURITY", "GROUPS", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions", Actions);
            return "modules/SECURITY/groups";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/USERS"})
    public String USERSpage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","USERS");
            JSONObject DocumentType = new JSONObject();
            JSONObject UserGroup    = new JSONObject();
            JSONObject Member       = new JSONObject();
            DocumentType.put("table", "document_type");
            DocumentType.put("code" , 1);
            UserGroup.put("table"   , "groups");
            Member.put("table"     , "members");

            String Actions = Helper.GetActions("SECURITY", "USERS", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions"        , Actions);
            model.addAttribute("document_type"  , Query.GetData(request, DocumentType, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            model.addAttribute("user_group"     , Query.GetData(request, UserGroup , session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            model.addAttribute("member"         , Query.GetData(request, Member    , session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/SECURITY/users";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/SESSIONS"})
    public String SESSIONSpage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","SESSIONS");
            String Actions = Helper.GetActions("SECURITY", "SESSIONS", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions", Actions);
            return "modules/SECURITY/sessions";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/AUTH"})
    public String AUTHpage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","AUTH");
            JSONObject Users      = new JSONObject();
            JSONObject Groups     = new JSONObject();
            JSONObject ActionType = new JSONObject();
            Users.put("table"   , "users");
            Groups.put("table"  , "groups");
            ActionType.put("table", "action_type");
            ActionType.put("code" , 6);            
            model.addAttribute("users"      , Query.GetData(request, Users, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            model.addAttribute("groups"     , Query.GetData(request, Groups, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            model.addAttribute("action_type", Query.GetData(request, ActionType, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/SECURITY/audit";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @RequestMapping(value = "/USERS/Records", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String Records(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("table"   , request.getParameter("table"));
            Data.put("id_users", session.getAttribute("IDUSR"));
            return Query.GetData(request, Data, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/AUTH/Records", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String AUTHRecords(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            String DateIn  = request.getParameter("date_range").split("-")[0].trim();
            String DateOut = request.getParameter("date_range").split("-")[1].trim();
            Data.put("id_users"      , request.getParameter("user") == null ? "" : request.getParameter("user"));
            Data.put("groups"        , request.getParameter("groups") == null ? "" : request.getParameter("groups"));
            Data.put("action_type"   , request.getParameter("action_type") == null ? "" : request.getParameter("action_type"));
            Data.put("date_in"       , DateIn.split("/")[2]+"-"+DateIn.split("/")[1]+"-"+DateIn.split("/")[0]);
            Data.put("date_out"      , DateOut.split("/")[2]+"-"+DateOut.split("/")[1]+"-"+DateOut.split("/")[0]);
            Data.put("table"         , "audit_filter");
            JSONArray Response  = Query.GetData(request, Data, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH");
            return Response.toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/GetPermits", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String GetPermits(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"       , request.getParameter("id"));
            Data.put("id_group" , request.getParameter("id_group"));
            Data.put("table"    , request.getParameter("table"));
            Data.put("id_users" , session.getAttribute("IDUSR"));
            JSONObject Response = Query.GetRequest(request, "DRAG_DROP", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return Response.getJSONObject("data").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/GROUPS/StoreGroup", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreGroup(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id"         , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("group_name" , request.getParameter("group_name"));
            Data.put("table"      , "groups");
            Data.put("id_users"   , session.getAttribute("IDUSR"));
            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/StoreUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreUser(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            Data.put("id"              , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("password"        , request.getParameter("id") == null ? randomString : "");
            Data.put("company"         , "0");
            Data.put("member"          , request.getParameter("member"));
            Data.put("agency"          , request.getParameter("agency"));
            Data.put("document_type"   , request.getParameter("document_type"));
            Data.put("document"        , request.getParameter("document"));
            Data.put("first_name"      , request.getParameter("first_name"));
            Data.put("second_name"     , request.getParameter("second_name"));
            Data.put("first_surname"   , request.getParameter("first_surname"));
            Data.put("second_surname"  , request.getParameter("second_surname"));
            Data.put("email"           , request.getParameter("email"));
            Data.put("phone"           , request.getParameter("phone"));
            Data.put("user_group"      , request.getParameter("user_group"));
            Data.put("id_users"        , session.getAttribute("IDUSR"));
            Data.put("table"           , "user");

            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            if(Response.getJSONObject("response").getInt("code") == 200 && request.getParameter("id") == null){
                MailService.SendMail(
                    new String[]{request.getParameter("email")},
                    "FirstSwitch - Creación de usuario",
                    new String[]{request.getParameter("first_name"), request.getParameter("first_surname"), randomString},
                    "StoreUser"
                );
            }
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/StorePermits", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StorePermits(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id"      , request.getParameter("id"));
            Data.put("id_users", session.getAttribute("IDUSR"));
            Data.put("permits" , request.getParameter("permits"));
            Data.put("table"   , session.getAttribute("current_page").equals("USERS") == false ? "permission_groups" : "permission_users");
            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/ResetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String ResetPassword(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            String randomString = RandomStringUtils.randomAlphanumeric(10);
            Data.put("id_users" , session.getAttribute("IDUSR"));
            Data.put("email"    , request.getParameter("email"));
            Data.put("password" , randomString);
            JSONObject Response = Query.GetRequest(request, "FORGOTPASSWORD", Data, "", "ADMIN");    
            if(Response.getJSONObject("response").getInt("code") == 200){
                MailService.SendMail(
                    new String[]{request.getParameter("email")},
                    "FirstSwitch - Reseteo de contraseña",
                    new String[]{Response.getJSONObject("data").getString("name"), Response.getJSONObject("data").getString("surname"), randomString},
                    "ResetPassword"
                );
            }
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/StorePassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StorePassword(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id_users"         , session.getAttribute("IDUSR"));
            Data.put("PASS_NEW"         , request.getParameter("PASS_NEW"));
            Data.put("CONFIRM_PASSWORD" , request.getParameter("CONFIRM_PASSWORD"));
            JSONObject Response = Query.GetRequest(request, "CHANGEPASS", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            if(Response.getJSONObject("response").getInt("code") == 200){
                session.setAttribute("CHNP", "false");
            }
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/ChangeStatusUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String ChangeStatusUser(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"       , request.getParameter("id"));
            Data.put("status"   , request.getParameter("status"));
            Data.put("id_users" , session.getAttribute("IDUSR"));
            JSONObject Response  = Query.GetRequest(request, "DISABLE_USERS", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/USERS/KillSession", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String KillSession(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id_users", request.getParameter("id"));
            JSONObject Response = Query.GetRequest(request, "LOGOUT", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
}
