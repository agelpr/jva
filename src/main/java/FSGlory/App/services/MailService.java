package FSGlory.App.services;



import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void SendMail(String[] to, String subject, String[] msg, String type) throws Exception{
       
        MimeMessage mime = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true);
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlText = "";
        
        switch(type)
        {
            case "ResetPassword":
                if(msg[0] == null || msg[0].equals("")){
                    htmlText = "<div style='background-color: #f3f2ef;font-family: arial, sans-Serif;margin: 0;padding: 50px 0;position: relative;font-size:15px;color:#404040;'><div style='margin: auto;position: relative;width: 500px; height: auto;background-color: #fff;'>"+
                    "<img id='bancamiga_logo' src=\"https://i.imgur.com/z1IDTgA.jpg\" alt='logo'>"+
                    "<div style='padding:20px;border-bottom:1px solid #ddd;'>"+
                    "<div style='padding:20px 20px 0 20px; background-color: #fff; border-radius: 100%;margin-top: -100px; margin-left: auto; margin-right: auto; position: relative;width: 70px;'>"+
                    "<img id='bancamiga_icon' src=\"https://i.imgur.com/s2diVYl.jpg\" alt='icon' style='width: 70px;'></div>"+
                    "<center><h2 style='color:#004987;'>Restablecimiento de Contraseña</h2></center></div>"+
                    "<div style='padding:20px 30px;border-bottom:1px solid #ddd;'>"+
                    "<p>Estimados <strong>"+msg[3]+"</strong>.</p>"+
                    "<p>Su contraseña fue restablecida exitosamente.</p>"+
                    "<p>Su nueva contraseña es: <strong>"+msg[2]+"</strong></p>"+
                    "</div>"+
                    "<div style='padding:10px 30px;text-align: center;background-color: #004987; color:#ffffff;'>www.<strong>bancamiga</strong>.com</div>"+
                    "</div></div>";
                }else{
                    htmlText = "<div style='background-color: #f3f2ef; font-family: arial, sans-Serif; margin: 0; padding: 50px 0;position: relative;font-size:15px;color:#404040;'><div style='margin: auto;position: relative;width: 500px; height: auto;background-color: #fff;'>"+
                    "<img id='bancamiga_logo' src=\"https://i.imgur.com/z1IDTgA.jpg\" alt='logo'>"+
                    "<div style='padding:20px; border-bottom:1px solid #ddd;'>"+
                    "<div style='padding:20px 20px 0 20px; background-color: #fff; border-radius: 100%; margin-top: -100px; margin-left: auto; margin-right: auto; position: relative; width: 70px;'>"+
                    "<img id='bancamiga_icon' src=\"https://i.imgur.com/s2diVYl.jpg\" alt='icon' style='width: 70px;'></div>"+
                    "<center><h2 style='color:#004987;'>Restablecimiento de Contraseña</h2></center></div>"+
                    "<div style='padding:20px 30px;border-bottom:1px solid #ddd;'>"+
                    "<p>Sr./Sra.<strong>"+msg[0]+' '+msg[1]+"</strong>.</p>"+
                    "<p>Su contraseña fue restablecida exitosamente.</p>"+
                    "<p>Su nueva contraseña es: <strong>"+msg[2]+"</strong></p>"+
                    "</div>"+
                    "<div style='padding:10px 30px;text-align: center;background-color: #004987; color:#ffffff;'>www.<strong>bancamiga</strong>.com</div>"+
                    "</div></div>";
                }
                break;
                default:
                    if(msg[0] == null){
                        htmlText = "<div style='background-color: #f3f2ef; font-family: arial, sans-Serif; margin: 0; padding: 50px 0;position: relative;font-size:15px;color:#404040;'><div style='margin: auto;position: relative;width: 500px; height: auto;background-color: #fff;'>"+
                        "<img id='bancamiga_logo' src=\"https://i.imgur.com/z1IDTgA.jpg\" alt='logo'>"+
                        "<div style='padding:20px;border-bottom:1px solid #ddd;'>"+
                        "<div style='padding:20px 20px 0 20px;background-color: #fff;border-radius: 100%;margin-top: -50px;margin-left: auto; margin-right: auto; position: relative;width: 70px;'>"+
                        "<img id='bancamiga_icon' src=\"https://i.imgur.com/8dcjVuO.jpg\" alt='icon' style='width: 70px;'></div>"+
                        "<center><h2 style='color:#004987;'>Le damos la bienvenida</h2></center></div>"+
                        "<div style='padding:20px 30px;border-bottom:1px solid #ddd;'>"+
                        "<p>Estimados <strong>"+msg[3]+"</strong>.</p>"+
                        "<p>Gracias por registrarte en <strong>Glory</strong>.</p>"+
                        "<p>Su contraseña es: <strong>"+msg[2]+"</strong></p>"+
                        "</div>"+
                        "<div style='padding:10px 30px;text-align: center;background-color: #004987; color:#ffffff;'>www.<strong>bancamiga</strong>.com</div>"+
                        "</div></div>";
                    }else{
                        htmlText = "<div style='background-color: #f3f2ef;font-family: arial, sans-Serif;margin: 0;padding: 50px 0;position: relative;font-size:15px;color:#404040;'><div style='margin: auto;position: relative;width: 500px; height: auto;background-color: #fff;'>"+
                        "<img id='bancamiga_logo' src=\"https://i.imgur.com/z1IDTgA.jpg\" alt='logo'>"+
                        "<div style='padding:20px;border-bottom:1px solid #ddd;'>"+
                        "<div style='padding:20px 20px 0 20px;background-color: #fff;border-radius: 100%;margin-top: -50px;margin-left: auto; margin-right: auto; position: relative;width: 70px;'>"+
                        "<img id='bancamiga_icon' src=\"https://i.imgur.com/8dcjVuO.jpg\" alt='icon' style='width: 70px;'></div>"+
                        "<center><h2 style='color:#004987;'>Le damos la bienvenida</h2></center></div>"+
                        "<div style='padding:20px 30px;border-bottom:1px solid #ddd;'>"+
                        "<p>Sr./Sra. <strong>"+msg[0]+' '+msg[1]+"</strong>.</p>"+
                        "<p>Gracias por registrarte en <strong>Glory</strong>.</p>"+
                        "<p>Su contraseña es: <strong>"+msg[2]+"</strong></p>"+
                        "</div>"+
                        "<div style='padding:10px 30px;text-align: center;background-color: #004987; color:#ffffff;'>www.<strong>bancamiga</strong>.com</div>"+
                        "</div></div>";
                    }
                break;
        }        
        helper.setText(htmlText,true);
        this.javaMailSender.send(mime);
    }
}

