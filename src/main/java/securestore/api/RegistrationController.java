package securestore.api;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import securestore.service.RegistrationData;

@Controller
public class RegistrationController {
  @SuppressWarnings("unused")
  private File usersFile = new File("users.json");

  @GetMapping("/register")
  public String registerForm(Model model) {
    model.addAttribute("regData", new RegistrationData());
    return "register.html";
  }

  @PostMapping("/register")
  public String registerSubmit(@ModelAttribute RegistrationData regData, BindingResult result) throws IOException {

    securestore.service.UserDatabase database = new securestore.service.UserDatabase();
    if (database.contains(regData.getUsername())) {
      return "redirect:/register";
    }

    database.storeUser(regData.getUsername(), regData.getPassword());

    return "redirect:/login";
  }
}
