package edu.politienda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminController {

    @GetMapping({"/", "/principal"})
    public String verPrincipal() {
       
        return "dahsboard"; 
    }
}