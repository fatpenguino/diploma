
package beans;

import entities.Comment;
import entities.Uploads;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.User;
import java.util.Arrays;
import javax.naming.InitialContext;
import javax.persistence.TypedQuery;

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
    public void addComment(Comment c) {
        em.persist(c);
        em.flush();
    }

    @Override
    public void addUploads(Uploads u) {
        em.persist(u);
        em.flush();
    }

    @Override
    public User findUser(Long id) {
        return em.find(User.class, id);
      
        }

    @Override
    public User checkUser(String login, String password) {
        User user= new User();
        try {
        user= (User)em.createQuery("SELECT u FROM User u where "
                + "u.login = :login AND u.password = :password")
                .setParameter("login", login)
                .setParameter("password", password).getSingleResult();
        }
        catch (Exception e)
        {
            return null;
        }
        return user;
    }

    @Override
    public User getUserByLogin(String login) {
        User user= new User();
        try {
        user= (User)em.createQuery("SELECT u FROM User u where "
                + "u.login = :login")
                .setParameter("login", login).getSingleResult();
        }
        catch (Exception e)
        {
            return null;
        }
        return user;
    }

    @Override
    public void removeUser(User u) {
        em.merge(u);
        em.flush();
        em.remove(u);
        em.flush();
    }
    
    @Override
    public void updateUser(User u) {
        em.merge(u);
        em.flush();
    }

    @Override
    public List<User> getUsers() {
        
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u",User.class);
        List<User> users = query.getResultList();
       
        return users;
    }

    @Override
    public List<Comment> getComments(String taskId) {
        List<Comment> comments=new ArrayList<>();
        try {
        TypedQuery<Comment> query = (TypedQuery<Comment>) em.createQuery("SELECT c FROM Comment c where "
                + "c.taskId = :taskId").setParameter("taskId", Long.parseLong(taskId));
        comments = query.getResultList();
        }
        catch(NumberFormatException e){
        comments=null;    
        }
        return comments;
    }

    @Override
    public List<Uploads> getUploads(String processId) {
        List<Uploads> uploads=new ArrayList<>();
        try {
        TypedQuery<Uploads> query = (TypedQuery<Uploads>) em.createQuery("SELECT u FROM Uploads u where "
                + "u.processId = :taskId").setParameter("taskId", Long.parseLong(processId));
        uploads = query.getResultList();
        }
        catch(NumberFormatException e){
        uploads=null;    
        }
        return uploads;
    }
  }





