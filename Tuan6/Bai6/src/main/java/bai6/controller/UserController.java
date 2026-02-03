package bai6.controller;

import bai6.entity.User;
import bai6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping
    public String view(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", service.findAll());
        return "user";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user) {
        service.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("user", service.findById(id));
        model.addAttribute("users", service.findAll());
        return "user";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin/users";
    }
}
