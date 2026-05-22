package com.example.tutorias.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }

    @GetMapping("/dashboard/redirect")
    public String redirigirPorRol(Authentication auth) {
        if (auth == null) return "redirect:/login";

        for (var authority : auth.getAuthorities()) {
            String rol = authority.getAuthority().trim();
            switch (rol) {
                case "ROLE_ADMIN":        return "redirect:/dashboard/admin";
                case "ROLE_TUTOR":        return "redirect:/dashboard/tutor";
                case "ROLE_TUTORADO":     return "redirect:/dashboard/tutorado";
                case "ROLE_COORDINADOR":  return "redirect:/dashboard/coordinador";
                case "ROLE_JEFE":         return "redirect:/dashboard/jefe";
                case "ROLE_SUBDIRECTOR":  return "redirect:/dashboard/subdirector";
                default:
            }
        }
        return "redirect:/login?error=true";
    }
}
