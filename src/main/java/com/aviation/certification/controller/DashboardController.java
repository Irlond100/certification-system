import com.aviation.certification.model.User;
import com.aviation.certification.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {
	private final UserService userService;
	
	public DashboardController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);
		
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			// Проверяем роли пользователя
			boolean isAdmin = user.getRoles().stream()
					.anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
			
			boolean isInstructor = user.getRoles().stream()
					.anyMatch(role -> role.getName().equals("ROLE_INSTRUCTOR"));
			
			boolean isCandidate = user.getRoles().stream()
					.anyMatch(role -> role.getName().equals("ROLE_CANDIDATE"));
			
			model.addAttribute("user", user);
			model.addAttribute("isAdmin", isAdmin);
			model.addAttribute("isInstructor", isInstructor);
			model.addAttribute("isCandidate", isCandidate);
			
			// Перенаправляем на соответствующую панель
			if (isAdmin) {
				return "admin/dashboard";
			} else if (isInstructor) {
				return "instructor/dashboard";
			} else if (isCandidate) {
				return "candidate/dashboard";
			}
		}
		
		return "redirect:/home?error=access_denied";
	}
}
