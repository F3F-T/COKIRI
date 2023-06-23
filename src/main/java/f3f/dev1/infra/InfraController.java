package f3f.dev1.infra;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfraController {
    @GetMapping("/")
    public String main() {
        return "index";
    }

}
