package minurl.controllers;

import minurl.domain.entities.ShortUrl;
import minurl.domain.models.ShortUrlDTO;
import minurl.domain.service.ShortUrlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;

    public HomeController(ShortUrlService shortUrlService){
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/")
    public String home(Model model){
        List<ShortUrlDTO> shortUrls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", "localhost:8080");
        return "index";
    }

}
