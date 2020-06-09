package wizut.tpsi.furka4u.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wizut.tpsi.furka4u.entity.User;

import javax.persistence.EntityManager;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private EntityManager em;

    public User getUserById(Integer id) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.userName = :userName AND u.password = hash('SHA256', stringtoutf8(:password), 1)", User.class)
                    .setParameter("userName", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
