/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Comment;
import entities.Uploads;
import java.util.List;
import javax.ejb.Remote;
import entities.User;

/**
 *
 * @author fatpenguino
 */
@Remote
public interface UserRemote {
    void addUser(User u);
    User findUser(Long id);
    List<User> getUsers();
    User checkUser(String login, String password);
    void removeUser(User u);
    void updateUser(User u);
    User getUserByLogin(String login);
    List<Comment> getComments(String taskId);
    void addComment(Comment c);
    void addUploads(Uploads u);
    List<Uploads> getUploads(String processId);
    
}
