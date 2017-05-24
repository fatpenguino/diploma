package entities;

import entities.Comment;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-24T03:12:50")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Boolean> Boss;
    public static volatile ListAttribute<User, Comment> comments;
    public static volatile SingularAttribute<User, String> surname;
    public static volatile SingularAttribute<User, String> Groups;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> login;
    public static volatile SingularAttribute<User, String> email;

}