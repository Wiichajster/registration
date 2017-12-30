package ejb;

import javax.ejb.EJBException;

import exceptions.UserAlreadyExistException;
import model.User;

public interface LoginRepository {
	public boolean add(User user) throws EJBException, UserAlreadyExistException;
	public User get(String username);
}
