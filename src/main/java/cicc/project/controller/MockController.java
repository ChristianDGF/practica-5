package cicc.project.controller;

import cicc.project.entities.MockEndpoint;
import cicc.project.entities.Project;
import cicc.project.entities.User;
import cicc.project.entities.Header;
import cicc.project.repository.MockEndpointRepository;
import cicc.project.repository.ProjectRepository;
import cicc.project.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manage/mocks")
public class MockController {

    @Autowired
    private MockEndpointRepository mockRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listMocks(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).get();
        // El administrador puede ver todos los registros, el usuario no [cite: 25]
        if (user.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("mocks", mockRepository.findAll());
        } else {
            model.addAttribute("mocks", mockRepository.findByProjectOwner(user));
        }
        return "mocks/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).get();
        model.addAttribute("mock", new MockEndpoint());
        model.addAttribute("projects", projectRepository.findByOwner(user));
        return "mocks/form";
    }

    @PostMapping("/save")
    public String saveMock(@Valid @ModelAttribute("mock") MockEndpoint mock, 
                           BindingResult result, 
                           @RequestParam Long projectId,
                           @RequestParam(required = false) List<String> headerKeys,
                           @RequestParam(required = false) List<String> headerValues,
                           Principal principal,
                           Model model) {
        
        if (result.hasErrors() || projectId == null) {
        User user = userRepository.findByUsername(principal.getName()).get();
        model.addAttribute("projects", projectRepository.findByOwner(user));
        model.addAttribute("error", "Debe seleccionar un proyecto válido.");
        return "mocks/form";
    }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        mock.setProject(project);

        if (headerKeys != null && headerValues != null) {
        List<Header> headersList = new ArrayList<>();
        for (int i = 0; i < Math.min(headerKeys.size(), headerValues.size()); i++) {
            String key = headerKeys.get(i);
            String value = headerValues.get(i);
            if (key != null && !key.trim().isEmpty()) {
                Header h = new Header();
                h.setHeaderKey(key);
                h.setHeaderValue(value);
                headersList.add(h);
            }
        }
        mock.setHeaders(headersList);
    }
        if (mock.getExpirationDate() == null) {
            mock.setExpirationDate(java.time.LocalDateTime.now().plusYears(1));
        }

        mockRepository.save(mock);
        return "redirect:/manage/mocks";
    }
}