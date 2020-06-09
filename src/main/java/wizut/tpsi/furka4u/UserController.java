package wizut.tpsi.furka4u;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wizut.tpsi.furka4u.entity.User;
import wizut.tpsi.furka4u.model.UserSession;
import wizut.tpsi.furka4u.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession userSession;

    @GetMapping("/login")
    public String loginPageAction(Model model, User user) {
        return "loginPage";
    }

    @PostMapping("/login")
    public String loginAction(Model model, User user, BindingResult bindingResult) {
        String username = user.getUserName();
        String password = user.getPassword();

        user = userService.getUserByUsernameAndPassword(username, password);

        if (user == null) {
            model.addAttribute("error", "Niepowodzenie, nazwa użytkownika lub hasło nieprawidłowe!");
            return loginPageAction(model, user);
        }

        userSession.setId(user.getId());
        userSession.setUsername(user.getUserName());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logoutAction(Model model) {

        userSession.setId(null);
        userSession.setUsername(null);

        return "redirect:/login";
    }
}
