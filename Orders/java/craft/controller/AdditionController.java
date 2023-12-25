package craft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdditionController {

	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest request,HttpServletResponse response) {
		int i=Integer.parseInt(request.getParameter("t1"));
		int j=Integer.parseInt(request.getParameter("t2"));
		int k=i+j;
		ModelAndView mv=new ModelAndView();
		mv.setViewName("display");
		mv.addObject("res", k);
		return mv;
	}
}
