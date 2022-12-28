package FSGlory.App;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class AppApplication implements WebMvcConfigurer{
    public static void main(String[] args) {
            SpringApplication.run(AppApplication.class, args);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/static/**")) {
           registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        if (!registry.hasMappingForPattern("/lang/**")) {
           registry.addResourceHandler("/lang/**").addResourceLocations("classpath:/lang/");
        }
        if (!registry.hasMappingForPattern("/static/img/**")) {
           registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:/static/img/");
        }
    }
    /**
     *    Multilanguage
     *    @author lsionchez and daltuve
     */
    @Bean(name = "localeResolver")
    public CookieLocaleResolver  localeResolver() {
        CookieLocaleResolver slr= new CookieLocaleResolver();
        slr.setCookieDomain("myAppLocaleCookie");
        slr.setCookieMaxAge(60*60);
        return slr;
    }
    /**
     *    Multilanguage
     *    @author lsionchez and daltuve
     */
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource resource= new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:lang/lang");
        resource.setDefaultEncoding("UTF-8");
        return resource;
    }
    /**
     *    Multilanguage
     *    @author lsionchez and daltuve
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        registry.addInterceptor(localeInterceptor).addPathPatterns("/*");
    }
    @Bean  
    public TomcatServletWebServerFactory tomcatEmbedded() {  

		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();  

        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {  
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {  
                //-1 means unlimited  
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);  
                
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSavePostSize(-1);
            }  
        });  
        return tomcat;  
    }
}