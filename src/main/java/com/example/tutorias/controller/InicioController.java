package com.example.tutorias.controller;

import com.example.tutorias.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @Autowired
    private SemestreRepository semestreRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("semestreActivo",
                semestreRepository.findFirstByActivoTrue().orElse(null));
        return "inicio";
    }
}