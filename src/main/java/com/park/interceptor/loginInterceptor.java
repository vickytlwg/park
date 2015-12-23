package com.park.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.park.service.TokenService;

public class loginInterceptor implements HandlerInterceptor{
	@Autowired
	TokenService tokenService;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		//String url = request.getRequestURI();
		
		/*String token = request.getHeader("token");
		if(token != null && tokenService.validToken(token))
			return true;
		HttpSession session  = request.getSession();
		Object username = session.getAttribute("username");
		if(username != null)
			return true;
		
		request.getRequestDispatcher("/login").forward(request, response);
		return false;
		*/
		return true;
		
	}

}
