/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javax.ejb.Remote;
import entities.User;

/**
 *
 * @author Serik
 */
@Remote
public interface UserRemote {
    void addUser(User u);
    User findUser(Long id);
    User checkUser(String login, String password);
    void removeUser(User u);
    void updateUser(User u);
}
