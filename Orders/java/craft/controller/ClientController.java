package craft.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import craft.service.ClientDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/client")
public class ClientController {
	ModelAndView mv =new ModelAndView();
	ClientDAO cliDao=new ClientDAO();

	@RequestMapping("/")
	public ModelAndView listclients(HttpServletRequest req,HttpServletResponse res) {
		ModelAndView mv =new ModelAndView();
		mv.setViewName("../ViewClient/list_cli");
		mv.addObject("data",cliDao.afficherClients() );
		return mv;
	}
	@RequestMapping("/Add")
	public ModelAndView addclient(HttpServletRequest req,HttpServletResponse res) {
		mv.setViewName("../ViewClient/Client");
		return mv;
	}
	
	@RequestMapping("/Update/{id}")
	public ModelAndView updateclient(HttpServletRequest req,HttpServletResponse res,@PathVariable int id) {
		mv.setViewName("../../ViewClient/Client");
		mv.addObject("op", "mod");
		mv.addObject("data",cliDao.afficherClients());
		return mv;
	}
	@RequestMapping("/Delete/{id}")
	public ModelAndView deleteclient(HttpServletRequest req,HttpServletResponse res,@PathVariable int id) {
		mv.setViewName("../../ViewClient/list_cli");
		if(cliDao.supprimeClient(id)) {
			mv.addObject("data",cliDao.afficherClientsAvecId(id));
		}
		return mv;
	}
	@PostMapping("/Add")
	public ModelAndView postclient(HttpServletRequest req) {
		mv.setViewName("../../ViewClient/list_cli");
		String op=req.getParameter("op");
		System.out.println(op);
		return mv;		
	}
}

