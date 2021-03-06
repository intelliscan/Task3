package securestore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("login");
    registry.addViewController("/loginTable").setViewName("loginTable");
    registry.addViewController("/login").setViewName("login");
    registry.addViewController("/register").setViewName("register");
    registry.addViewController("/generate").setViewName("addData");
  }

}
