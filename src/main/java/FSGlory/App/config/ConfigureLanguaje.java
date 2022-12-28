/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FSGlory.App.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import java.util.Properties;
import org.springframework.context.annotation.Configuration;


@PropertySources({
    @PropertySource("classpath:/lang/lang.properties"),
    @PropertySource("classpath:/lang/lang_es.properties"),
    @PropertySource("classpath:/lang/lang_en.properties")
})
/**
 *
 * @author Laura
 */
@Configuration
public class ConfigureLanguaje {
    static String lang;
    public String change_lang(String value) throws FileNotFoundException, IOException {
        lang = value;
        return lang;
    }
    public String translate(String value) throws FileNotFoundException, IOException {
        Properties attribute = new Properties();
        attribute.load(new FileReader(getClass().getResource("/lang/lang_"+lang+".properties").getFile()));
        value=value.toUpperCase();
        value = attribute.getProperty(value.replace(" ", "_"));
        if(value==null){
            value =" ";
        }
        return value;
    }
}
