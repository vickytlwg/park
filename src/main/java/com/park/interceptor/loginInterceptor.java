package com.park.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class loginInterceptor implements HandlerInterceptor{

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
		// TODO Auto-generated method stub
		String url = request.getRequestURI();
		
		if(url.contains("login")){
			//如果进行登陆提交，放行
			return true;
		}
		HttpSession session  = request.getSession();
		//从session中取出用户身份信息
		String username = (String) session.getAttribute("username");
		
		if(username != null){
			//身份存在，放行
			return true;
		}
		request.getRequestDispatcher("/login").forward(request, response);
		return false;
	}

}
