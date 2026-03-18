package cicc.project.controller;

import cicc.project.entities.Project;
import cicc.project.entities.User;
import cicc.project.repository.ProjectRepository;
import cicc.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/manage/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listProjects(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).get();
        model.addAttribute("projects", projectRepository.findByOwner(user));
        return "projects/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/form";
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute Project project, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).get();
        project.setOwner(user);
        projectRepository.save(project);
        return "redirect:/manage/projects";
    }
}