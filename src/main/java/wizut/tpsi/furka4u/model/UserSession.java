package wizut.tpsi.furka4u.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import wizut.tpsi.furka4u.entity.User;

@Component
@SessionScope
public class UserSession {

    private Integer id;
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
