package com.ufrn.PW.controller;

import java.util.Optional;
import java.util.UUID;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ufrn.PW.domain.Camisa;
import com.ufrn.PW.service.CamisaService;
import com.ufrn.PW.service.FileStorageService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CamisaController {
    private final CamisaService service;
    private final FileStorageService fileStorageService;

    public CamisaController(CamisaService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/index")
    public String listarTudo(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("camisa", service.findAll());
        model.addAttribute("carrinhoSize", session.getAttribute("carrinho") != null ? ((List<?>) session.getAttribute("carrinho")).size() : 0);
        model.addAttribute("remoteUser", request.getRemoteUser());  
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("camisa", service.findAll());
        model.addAttribute("remoteUser", request.getRemoteUser());  
        return "admin";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        Camisa camisa = new Camisa();
        model.addAttribute("Camisa", camisa);
        return "cadastro";
    }

    @GetMapping("/editar/{id}")
    public String editarCamisa(@PathVariable Long id, Model model) {
        Optional<Camisa> camisa = service.findById(id);
        if (camisa.isPresent()) {
            model.addAttribute("Camisa", camisa.get());
            return "editar";
        }
		return "redirect:/admin";
    }

    @PostMapping("/salvar")
    public ModelAndView salvar(@ModelAttribute @Valid Camisa c, Errors errors, @RequestParam("file") MultipartFile file) {
        if (errors.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("/admin");
            modelAndView.addObject("msg", errors);
            return modelAndView;
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        c.setImageUri(uniqueFileName);
        fileStorageService.save(file, uniqueFileName);

        if (c.getId() == null) {
            c.setIsDeleted(null);
            service.create(c);
            ModelAndView mv = new ModelAndView("redirect:/admin");
            mv.addObject("msg", "Cadastro realizado com sucesso");
            mv.addObject("camisa", service.findAll());
            return mv;
        } else {
            c.setIsDeleted(null);
            service.update(c);
            ModelAndView mv = new ModelAndView("redirect:/admin");
            mv.addObject("msg", "camisa atualizada com sucesso");
            mv.addObject("camisa", service.findAll());
            return mv;
        }
    }

    @GetMapping("/deletar/{id}")
    public ModelAndView deletar(@PathVariable Long id) {
        Optional<Camisa> c = service.findById(id);
        if (c.isPresent()) {
            Date date = new Date();
            c.get().setIsDeleted(date);
            service.update(c.get());
        } else {
            return new ModelAndView("redirect:/admin");
        }
        ModelAndView mv = new ModelAndView("redirect:/admin");
        mv.addObject("msg", "remoção realizada com sucesso");
        return mv;
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public ModelAndView adicionarCarrinho(@PathVariable Long id, HttpSession session) {
        Optional<Camisa> c = service.findById(id);
        if (c.isPresent()) {
            List<Camisa> carrinho = (List<Camisa>) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new ArrayList<>();
            }
            carrinho.add(c.get());
            session.setAttribute("carrinho", carrinho);
        }
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/verCarrinho")
    public String verCarrinho(Model model, HttpSession session) {
        List<Camisa> carrinho = (List<Camisa>) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.isEmpty()) {
            model.addAttribute("mensagem", "Não existem itens no carrinho.");
            return "redirect:/index";
        }
        model.addAttribute("Carrinho", carrinho);
        return "verCarrinho";
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpSession session) {
        List<Camisa> carrinho = (List<Camisa>) session.getAttribute("carrinho");
        for (Camisa c : carrinho) {
            service.delete(c.getId());
		  
        }
		session.removeAttribute("carrinho");
       // session.invalidate();
        return "redirect:/index";
    }
}
