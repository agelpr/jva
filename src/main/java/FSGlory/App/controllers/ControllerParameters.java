/*session
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.controllers;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import kong.unirest.UnirestException;
/**
 *
 * @author YCARRILLO
 */
@Controller
public class ControllerParameters {
    @Autowired
    private ComponentsQuery Query;
    @Autowired
    private ControllerMain Main;
    @Autowired
    private ComponentsHelper Helper;
    @GetMapping({"/MASTER"})
    public String MASTERPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","MASTER");
            String Actions = Helper.GetActions("PARAMETERS", "MASTER", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions" , Actions);
            return "modules/PARAMETERS/master";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/COMPANY"})
    public String COMPANYPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","COMPANY");
            JSONObject Country        = new JSONObject();
            JSONObject DocumentType   = new JSONObject();
            Country.put("table"     , "country");
            DocumentType.put("table", "document_type");
            DocumentType.put("code" , 1);
            String Actions = Helper.GetActions("PARAMETERS", "COMPANY", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions"       , Actions);
            model.addAttribute("document_type" , Query.GetData(request, DocumentType, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            model.addAttribute("country"       , Query.GetData(request, Country, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            return "modules/PARAMETERS/company";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/MEMBER"})
    public String MEMBERPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","MEMBER");
            JSONObject Country        = new JSONObject();
            JSONObject DocumentType   = new JSONObject();
            Country.put("table"     , "country");
            DocumentType.put("table", "document_type");
            DocumentType.put("code" , 1);
            String Actions = Helper.GetActions("PARAMETERS", "MEMBER", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions"       , Actions);
            model.addAttribute("document_type" , Query.GetData(request, DocumentType, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            model.addAttribute("country"       , Query.GetData(request, Country, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            return "modules/PARAMETERS/member";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/AGENCY"})
    public String AGENCYPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","AGENCY");
            JSONObject Country        = new JSONObject();
            JSONObject DocumentType   = new JSONObject();
            JSONObject Member         = new JSONObject();
            Member.put("table"      , "members");
            Country.put("table"     , "country");
            DocumentType.put("table", "document_type");
            DocumentType.put("code" , 1);
            String Actions = Helper.GetActions("PARAMETERS", "AGENCY", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions"       , Actions);
            model.addAttribute("document_type" , Query.GetData(request, DocumentType, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            model.addAttribute("country"       , Query.GetData(request, Country     , session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH"));
            model.addAttribute("member"        , Query.GetData(request, Member      , session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/PARAMETERS/agency";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/MODULE"})
    public String MODULEPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","MODULE");
            String Actions = Helper.GetActions("PARAMETERS", "MODULE", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions" , Actions);
            return "modules/PARAMETERS/module";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/SUBMODULE"})
    public String SUBMODULEPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","SUBMODULE");
            JSONObject Modules = new JSONObject();
            Modules.put("table", "modules");
            String Actions = Helper.GetActions("PARAMETERS", "SUBMODULE", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions" , Actions);
            model.addAttribute("modules" , Query.GetData(request, Modules, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/PARAMETERS/submodule";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/DEVICES"})
    public String DEVICESPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","DEVICES");
            JSONObject Member = new JSONObject();
            Member.put("table", "members");
            String Actions = Helper.GetActions("PARAMETERS", "DEVICES", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions" , Actions);
            model.addAttribute("member"  , Query.GetData(request, Member, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/PARAMETERS/devices";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @GetMapping({"/ACCOUNT"})
    public String ACCOUNTPage(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            session.setAttribute("current_page","ACCOUNT");
            JSONObject Member = new JSONObject();
            JSONObject Banks  = new JSONObject();
            Member.put("table", "members");
            Banks.put("table" , "banks");
            String Actions = Helper.GetActions("PARAMETERS", "ACCOUNT", session.getAttribute("PERMITS").toString());
            model.addAttribute("Actions" , Actions);
            model.addAttribute("member"  , Query.GetData(request, Member, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            model.addAttribute("banks"   , Query.GetData(request, Banks, session.getAttribute("TOKEN").toString(),"ADMIN", "SELECT"));
            return "modules/PARAMETERS/account";
        }catch (Exception e) { 
            return "redirect:/login";
        }
    }
    @RequestMapping(value = "/PARAMETERS/Records", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String Records(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("table", request.getParameter("table"));
            if(session.getAttribute("current_page").equals("MASTER") == true){
                Data.put("id_tb_table", request.getParameter("id_tb_table"));
                return Query.GetRequest(request, "DATATABLE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN").toString();
            }else{
                return Query.GetData(request, Data, session.getAttribute("TOKEN").toString(),"ADMIN", "SEARCH").toString();
            }
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/ChangeStatus", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String ChangeStatus(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("module"   , session.getAttribute("current_page"));
            Data.put("id"       , request.getParameter("id"));
            Data.put("status"   , request.getParameter("status"));
            Data.put("table"    , request.getParameter("table"));
            Data.put("id_users" , session.getAttribute("IDUSR"));
            JSONObject Response  = Query.GetRequest(request, "DISABLE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/FillDragAndDrop", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String FillDragAndDrop(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"       , request.getParameter("id"));
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
    @RequestMapping(value = "/PARAMETERS/StoreDrop", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreDrag(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();            
            Data.put("id"        , request.getParameter("id"));
            Data.put("table"     , request.getParameter("table"));
            Data.put("childrens" , request.getParameter("childrens"));
            JSONObject Response = Query.GetRequest(request, "STORE_DROP", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/StoreDevice", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreDevice(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();            
            Data.put("id"    , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("member", request.getParameter("member"));
            Data.put("agency", request.getParameter("agency"));
            Data.put("serial", request.getParameter("device_serial"));
            Data.put("table" , "device");
            Data.put("id_users", session.getAttribute("IDUSR"));
            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/StoreAccount", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreAccount(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false && Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();            
            Data.put("id"            , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("member"        , request.getParameter("member"));
            Data.put("agency"        , request.getParameter("agency"));
            Data.put("bank"          , "13");
            Data.put("account_number", request.getParameter("account_number"));
            Data.put("table"         , "accounts");
            Data.put("id_users"      , session.getAttribute("IDUSR"));
            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/MASTER/StoreMaintenance", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreMaintenance(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id_tb_table", request.getParameter("id_parent") != null ? request.getParameter("id_parent") : 0);
            Data.put("id"         , request.getParameter("id") != null ? request.getParameter("id") : 0);
            Data.put("description", request.getParameter("description") == null ? request.getParameter("MasterDsc") : request.getParameter("description"));
            Data.put("table"      , request.getParameter("table"));
            Data.put("id_users"   , session.getAttribute("IDUSR"));
            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/COMPANY/StoreCompany", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreCompany(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id"                  , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("company_name"        , request.getParameter("company_name"));
            Data.put("code"                , request.getParameter("code"));
            Data.put("name_fantasy"        , request.getParameter("fantasy_name"));
            Data.put("legal_representative", request.getParameter("legal_representative"));
            Data.put("document_type"       , request.getParameter("document_type"));
            Data.put("rif_number"          , request.getParameter("rif_number") + "-" + request.getParameter("rif_code"));
            Data.put("phone"               , request.getParameter("phone"));
            Data.put("email"               , request.getParameter("email"));
            Data.put("id_country"          , request.getParameter("country"));
            Data.put("id_state"            , request.getParameter("state"));
            Data.put("id_city"             , request.getParameter("city"));
            Data.put("id_municipality"     , request.getParameter("municipality"));
            Data.put("id_parish"           , request.getParameter("parish"));
            Data.put("urbanization"        , request.getParameter("urbanization"));
            Data.put("postal_code"         , request.getParameter("postal_code"));
            Data.put("floor"               , request.getParameter("floor"));
            Data.put("work_place"          , request.getParameter("work_place"));
            Data.put("id_users"            , session.getAttribute("IDUSR"));
            Data.put("table"               , "company");

            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/StoreModule", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreModule(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"        , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("code"      , request.getParameter("module_code"));
            Data.put("module"    , request.getParameter("module_name"));
            Data.put("icon"      , request.getParameter("module_icon"));
            Data.put("id_users"  , session.getAttribute("IDUSR"));
            Data.put("table"     , "modules");

            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/StoreSubModule", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreSubModule(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"        , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("module"    , request.getParameter("module_name"));
            Data.put("code"      , request.getParameter("submodule_code"));
            Data.put("submodule" , request.getParameter("submodule_name"));
            Data.put("id_users"  , session.getAttribute("IDUSR"));
            Data.put("table"     , "submodules");

            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/PARAMETERS/Delete", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String Delete(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("id"        , request.getParameter("id"));
            Data.put("id_users"  , session.getAttribute("IDUSR"));
            Data.put("table"     , request.getParameter("table"));

            JSONObject Response = Query.GetRequest(request, "DELETE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/MEMBER/StoreMember", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreMember(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id"                  , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("member_name"         , request.getParameter("member_name"));
            Data.put("code"                , request.getParameter("code"));
            Data.put("name_fantasy"        , request.getParameter("fantasy_name"));
            Data.put("legal_representative", request.getParameter("legal_representative"));
            Data.put("document_type"       , request.getParameter("document_type"));
            Data.put("rif_number"          , request.getParameter("rif_number") + "-" + request.getParameter("rif_code"));
            Data.put("phone"               , request.getParameter("phone"));
            Data.put("email"               , request.getParameter("email"));
            Data.put("id_country"          , request.getParameter("country"));
            Data.put("id_state"            , request.getParameter("state"));
            Data.put("id_city"             , request.getParameter("city"));
            Data.put("id_municipality"     , request.getParameter("municipality"));
            Data.put("id_parish"           , request.getParameter("parish"));
            Data.put("urbanization"        , request.getParameter("urbanization"));
            Data.put("postal_code"         , request.getParameter("postal_code"));
            Data.put("floor"               , request.getParameter("floor"));
            Data.put("work_place"          , request.getParameter("work_place"));
            Data.put("id_users"            , session.getAttribute("IDUSR"));
            Data.put("table"               , "member");

            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/AGENCY/StoreAgency", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String StoreAgency(HttpServletRequest request, HttpSession session, Model model) throws Exception{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data     = new JSONObject();
            Data.put("id"                  , request.getParameter("id") == null ? 0 : request.getParameter("id"));
            Data.put("agency_name"         , request.getParameter("agency_name"));
            Data.put("code"                , request.getParameter("code"));
            Data.put("legal_representative", request.getParameter("legal_representative"));
            Data.put("phone"               , request.getParameter("phone"));
            Data.put("email"               , request.getParameter("email"));
            Data.put("id_country"          , request.getParameter("country"));
            Data.put("id_state"            , request.getParameter("state"));
            Data.put("id_city"             , request.getParameter("city"));
            Data.put("id_municipality"     , request.getParameter("municipality"));
            Data.put("id_parish"           , request.getParameter("parish"));
            Data.put("urbanization"        , request.getParameter("urbanization"));
            Data.put("postal_code"         , request.getParameter("postal_code"));
            Data.put("floor"               , request.getParameter("floor"));
            Data.put("work_place"          , request.getParameter("work_place"));
            Data.put("id_member"           , request.getParameter("member"));
            Data.put("id_users"            , session.getAttribute("IDUSR"));
            Data.put("table"               , "agency");

            JSONObject Response = Query.GetRequest(request, "SAVE_MASTER", Data, session.getAttribute("TOKEN").toString(), "ADMIN");
            return  Response.getJSONObject("response").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
    @RequestMapping(value = "/COMPANY/GetMembers", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String GetMembers(HttpServletRequest request, HttpSession session, Model model) throws IOException, ParseException, UnirestException{
        String requestedWithHeader = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestedWithHeader) == false || Query.CheckSession(session, session.getAttribute("TOKEN").toString(),"ADMIN") != true){
            Main.Logout(session, request, model);
            return "redirect:/login";
        }
        try{
            JSONObject Data = new JSONObject();
            Data.put("table"        , request.getParameter("table"));
            Data.put("id_users"     , session.getAttribute("IDUSR"));
            JSONObject Response = Query.GetRequest(request, "FILTER", Data, session.getAttribute("TOKEN").toString(),"ADMIN");
            return Response.getJSONObject("data").toString();
        }catch (Exception e) { 
            JSONObject Msg  = new JSONObject();
            Msg = Msg.put("code"   , 500);
            Msg = Msg.put("message", "A ocurrido un error, por favor intente de nuevo, si el problema persiste por favor contactar al administrador del sistema");
            return Msg.toString();
        }
    }
}
