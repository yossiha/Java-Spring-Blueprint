package horizon.time.services;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import horizon.time.WebContextHolder;
import horizon.time.persistence.repo.BusinessRepository;

import java.lang.reflect.*;

@Service
@Transactional
public class CheatsheetService {
	@Autowired
	private WebContextHolder webContextHolder;

	private void getSecurityContext() {
		SecurityContextHolder.getContext().getAuthentication();
		SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Achieve dynamic IoC through service locator pattern and reflection
	 */
	private void serviceLocatorCheatsheet() {
		long id = 1;
		String clazz = "userRepository";

		// Service pattern + Dynamic IOC
		Object repo = webContextHolder.getApplicationContext().getBean(clazz);

		System.out.println(repo.getClass());
		System.out.println(((CrudRepository<Object, Long>) repo).findById((long) id));

		Object obj = ((CrudRepository<Object, Long>) repo).findById((long) id);

		// Reflection
		try {
			System.out.println("get()");
			System.out.println(obj.getClass().getMethod("get", null).invoke(obj, null));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e2) {
			e2.printStackTrace();
		}

		try {
			obj.getClass().getMethod("isPresent", null).invoke(obj, null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e1) {
			e1.printStackTrace();
		}

		java.lang.reflect.Method method = null;
		try {
			method = obj.getClass().getMethod("isPresent", null);
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}

		try {
			System.out.println(method.invoke(obj, null));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
