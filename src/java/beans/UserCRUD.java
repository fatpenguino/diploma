
package beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.User;

/**
 *
 * @author Feisal
 */

@Stateless(mappedName = "userBean")
public class UserCRUD implements UserRemote {
    @PersistenceContext
    EntityManager em;

    @Override
    public void addUser(User u) {
        em.persist(u);
        em.flush();
    }

    @Override
    public User findUser(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public User checkUser(String login, String password) {
        return (User) em.createQuery("SELECT u FROM User u where "
                + "u.login = :login AND u.password = :password")
                .setParameter("login", login)
                .setParameter("password", password).getSingleResult();
        
    }

    @Override
    public void removeUser(User u) {
        em.merge(u);
        em.flush();
        em.remove(u);
        em.flush();
    }
    
    public void updateUser(User u) {
        em.merge(u);
        em.flush();
    }
    
    }



