package com.example.tutorias.controller;

import com.example.tutorias.model.Carrera;
import com.example.tutorias.service.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carreras")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("carreras", carreraService.listarTodos());
        return "carrera/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("carrera", new Carrera());
        return "carrera/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Carrera carrera) {
        carreraService.guardar(carrera);
        return "redirect:/carreras";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        carreraService.buscarPorId(id).ifPresent(c -> model.addAttribute("carrera", c));
        return "carrera/formulario";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        carreraService.buscarPorId(id).ifPresent(c -> model.addAttribute("carrera", c));
        return "carrera/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        carreraService.eliminar(id);
        return "redirect:/carreras";
    }
}