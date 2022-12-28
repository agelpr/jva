/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.controllers;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
/**
 *
 * @author YCARRILLO
 */
@Controller
public class ControllerMain {
    @Autowired
    private ComponentsQuery Query;
    public Boolean validateLogin(HttpSession session){
        return session.getAttribute("SessionActive") != null;
    }
    @GetMapping({"/lang"})
    @RequestMapping(value = "/lang", method = RequestMethod.GET, produces = "text/html")
    public String loginPage2(HttpServletRequest request,HttpSession session, Model model) {
        model.addAttribute("page", "login");
        String re = request.getParameter("lang");
        session.setAttribute("Lang",re);
        model.addAttribute("Lang",session.getAttribute("Lang"));
        if (session.getAttribute("SessionActive") != null) {
           return "redirect:/home?lang="+session.getAttribute("Lang");
        }
        return "index";
    }
    @GetMapping({"/inicio"})
    public String Homepage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        session.setAttribute("current_page","home");
        LocalDate currentdate    = LocalDate.now();
        int currentDay           = currentdate.getDayOfMonth();
        String FormatedDay       = String.valueOf(currentDay).length() < 2 ? "0"+String.valueOf(currentDay) : String.valueOf(currentDay);
        Month currentMonth       = currentdate.getMonth();
        int currentYear          = currentdate.getYear();
        try{
            JSONObject Data = new JSONObject();
            Data.put("action" , "movements_monitor");
            Data.put("member" , session.getAttribute("IDMEMBER").toString());
            JSONArray Dashboard  = Query.GetRequest(request, "SEARCH_API_AS400", Data, session.getAttribute("TOKEN").toString(), "ADMIN").getJSONArray("data");
            model.addAttribute("date"      , "Caracas, "+ FormatedDay +" "+currentMonth.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))+" "+currentYear);
            model.addAttribute("Dashboard" , Dashboard);
            return "home";
        }catch (Exception e) { 
            JSONArray Dashboard = new JSONArray();
            model.addAttribute("date"      , "Caracas, "+ FormatedDay +" "+currentMonth.getDisplayName(TextStyle.FULL, new Locale("es", "ES"))+" "+currentYear);
            model.addAttribute("Dashboard" , Dashboard);
            return "home";
        }
    }
    @GetMapping({"/home"})
    public String home(HttpSession session, HttpServletRequest request, Model model) throws IOException, UnirestException {
        if(session.getAttribute("SessionActive")== null) {
            return "redirect:/login";
        }else{
            if(session.getAttribute("current_page") == null){
                session.setAttribute("current_page","home");
            }
            model.addAttribute("current_page", session.getAttribute("current_page"));
        }
        model.addAttribute("page", "home");
        JSONArray Permits = new JSONArray(session.getAttribute("PERMITS").toString());
        // String Actions = Helper.GetActions("MAINTENANCE", "MASTER", session.getAttribute("PERMITS").toString());
        model.addAttribute("ListModules", Permits);
        return "index";
    }
    @GetMapping({"/logout"})
    public String Logout(HttpSession session, HttpServletRequest request, Model model) throws IOException, UnirestException{
        try {
            JSONObject Data = new JSONObject();
            model.addAttribute("page"         , "login");
            model.addAttribute("Lang"         , "es");
            if (session.getAttribute("SessionActive") != null) {
                Data.put("id_users", session.getAttribute("IDUSR"));
                JSONObject Response = Query.GetRequest(request, "LOGOUT", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
                if(Response.getJSONObject("response").getInt("code") == 200){
                    session.setAttribute("current_page", null);
                    session.invalidate();
                }
            }
            return "index";
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return "error/500";
        }
    }
    @GetMapping({"/","/login"})
    public String loginPage(HttpSession session, HttpServletRequest request, Model model) throws IOException, UnirestException {
        try{
            JSONObject DocumentType = new JSONObject();
            DocumentType.put("table", "document_type");
            DocumentType.put("code" , 1);
            if (session.getAttribute("SessionActive") != null) {
                return "redirect:/home?lang="+ session.getAttribute("Lang");
            }
            model.addAttribute("page", "login");
            session.setAttribute("Lang"       , "es");
            model.addAttribute("Lang", session.getAttribute("Lang"));
            return "index";
        }catch (Exception e) { 
            return "error/500";
        }
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String Login(HttpServletRequest request, HttpServletResponse httpServletResponse, HttpSession session, Model model) throws IOException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false){
            return "redirect:/login";
        }
        try{
            String email       = request.getParameter("user");
            String pass        = request.getParameter("password");
            String lang        = request.getParameter("lang");
            String FullName    = null;
            String FullSurname = null;
            JSONObject JsonResponse = new JSONObject();
            if (!email.isEmpty() && !pass.isEmpty()) {
                JSONObject Data    = new JSONObject();
                JSONObject Permits = new JSONObject();
                Data.put("email", email);
                Data.put("pass", pass);  
                JSONObject Response = Query.GetRequest(request, "LOGIN", Data, "", "ADMIN");
                JsonResponse        = Response.getJSONObject("response");
                if(JsonResponse.getInt("code") == 200){
                    JsonResponse.put("lang",""+lang+"");
                    Permits.put("id_users", Response.getJSONObject("data").getLong("id_users"));
                    FullName      = Response.getJSONObject("data").getString("second_name") != "" ? Response.getJSONObject("data").getString("first_name")+" "+Response.getJSONObject("data").getString("second_name"):Response.getJSONObject("data").getString("first_name");
                    FullSurname   = Response.getJSONObject("data").getString("second_surname") != "" ? Response.getJSONObject("data").getString("first_surname")+" "+Response.getJSONObject("data").getString("second_surname"):Response.getJSONObject("data").getString("first_surname");
                    session.setAttribute("IDDOC"        , Response.getJSONObject("data").getInt("id_document_type"));
                    session.setAttribute("DOC"          , Response.getJSONObject("data").getString("document_type").split("-")[0].trim()+"-"+Response.getJSONObject("data").getString("document").split("-")[0]);
                    session.setAttribute("RIF"          , Response.getJSONObject("data").getString("document_type").split("-")[0].trim()+"-"+Response.getJSONObject("data").getString("document"));
                    session.setAttribute("DOCUMENT"     , Response.getJSONObject("data").getString("document"));
                    session.setAttribute("IDUSR"        , Response.getJSONObject("data").getLong("id_users"));
                    session.setAttribute("FULLNAME"     , FullName+" "+FullSurname);
                    session.setAttribute("FIRSTNAME"    , Response.getJSONObject("data").getString("first_name"));
                    session.setAttribute("SECONDNAME"   , Response.getJSONObject("data").getString("second_name"));
                    session.setAttribute("FIRSTSURNAME" , Response.getJSONObject("data").getString("first_surname"));
                    session.setAttribute("SECONDSURNAME", Response.getJSONObject("data").getString("second_surname"));
                    session.setAttribute("PHONE"        , Response.getJSONObject("data").getString("phone"));
                    session.setAttribute("EMAIL"        , Response.getJSONObject("data").getString("email"));
                    session.setAttribute("CHNP"         , Response.getJSONObject("data").getBoolean("change_pass"));
                    session.setAttribute("TOKEN"        , Response.getJSONObject("data").getString("token"));
                    session.setAttribute("IDMEMBER"     , Response.getJSONObject("data").getInt("id_member"));
                    session.setAttribute("MEMBER"       , Response.getJSONObject("data").getString("member"));
                    session.setAttribute("GROUP"        , Response.getJSONObject("data").getInt("id_group"));
                    session.setAttribute("PERMITS"      , Query.GetRequest(request, "SIDEBAR", Permits, session.getAttribute("TOKEN").toString(), "ADMIN").getJSONArray("data").toString());
                    session.setAttribute("Lang"         , "es");
                    session.setAttribute("SessionActive", true);
                }
            }
            return JsonResponse.toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/DarkMode", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String SaveDarkMode(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            session.setAttribute("DarkMode",request.getParameter("DarkMode"));
            Data.put("DarkMode", request.getParameter("DarkMode"));
            return Data.toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/GetChildrens", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String GetChildrens(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            if(request.getParameter("table").equals("country") == true){
                Data.put("id_country", request.getParameter("id_country"));
            }
            if(request.getParameter("table").equals("city") == true || request.getParameter("table").equals("municipality") == true){
                Data.put("id_state", request.getParameter("id_state"));
            }
            if(request.getParameter("table").equals("parish") == true){
                Data.put("id_municipality", request.getParameter("id_municipality"));
            }
            Data.put("table",request.getParameter("table"));
            JSONObject Response = Query.GetRequest(request, "DIRECTIONS", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("data").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/FillSelect", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String FillSelect(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"   , request.getParameter("id"));
            Data.put("table", request.getParameter("table"));
            return Query.GetData(request, Data, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/SearchFile", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String SearchFile(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id_register"    , request.getParameter("id_register"));
            Data.put("id_module"      , request.getParameter("id_module"));
            Data.put("id_collections" , request.getParameter("id_collections"));
            Data.put("table"          , "files");
            JSONObject Response = Query.GetRequest(request, "FILTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return Response.toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/SearchFields", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String SearchFields(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id_users", request.getParameter("id").equals("null") == false ? request.getParameter("id") : "");
            Data.put("input"   , request.getParameter("input"));
            Data.put("table"   , request.getParameter("type").equals("1") ? "email" : "phone");
            JSONObject Response = Query.GetRequest(request, "FILTER", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/SearchPostalCode", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String SearchPostalCode(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("table" , "postal_code_c");
            Data.put("code"  , request.getParameter("postal_code") );
            JSONArray Response = Query.GetData(request, Data, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH");
            return Response.toString();
        }catch (Exception e) { 
            JSONObject Msg = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/ValidateAccountNumber", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String ValidateAccountNumber(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("account_number", request.getParameter("account_number"));
            Data.put("id_users"      , session.getAttribute("IDUSR"));
            Data.put("table"         , "account");
            JSONObject Response = Query.GetRequest(request, "FILTER", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
}