package securestore.api;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import securestore.service.LoginData;

@Controller
public class LoginTableController {
  @GetMapping("/loginTable")
  public String loginTable(Model model) throws IOException {
    securestore.service.UserDatabase database = new securestore.service.UserDatabase();
    model.addAttribute("table", database.getLoginData(getActiveUser()));
    model.addAttribute("username", getActiveUser());
    return "loginTable.html";
  }

  private String getActiveUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }

  @GetMapping("/generate")
  public String getGenerate(Model model) {
    model.addAttribute("loginData", new LoginData());
    return "addData.html";
  }

  @PostMapping("/generate")
  public String generate(@RequestParam(value = "action") String param, @ModelAttribute LoginData data) throws IOException {
    if (param.equals("Add")) {
      securestore.service.UserDatabase database = new securestore.service.UserDatabase();
      database.storeNewLoginData(getActiveUser(), data);
      return "redirect:/loginTable";
    } else
      return "redirect:/loginTable";
  }
}