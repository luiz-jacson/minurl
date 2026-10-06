package minurl.web.controllers;

import jakarta.validation.Valid;
import minurl.ApplicationProperties;
import minurl.domain.exceptions.ShortUrlNotFoundException;
import minurl.domain.models.CreateShortUrlCmd;
import minurl.domain.models.ShortUrlDTO;
import minurl.domain.service.ShortUrlService;
import minurl.web.dto.CreateShortUrlForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties properties;

    public HomeController(ShortUrlService shortUrlService, ApplicationProperties properties){
        this.shortUrlService = shortUrlService;
        this.properties = properties;
    }

    @GetMapping("/")
    public String home(Model model){
        List<ShortUrlDTO> shortUrls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm(""));
        return "index";
    }

    @PostMapping("/short-urls")
    public String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm form,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if(bindingResult.hasErrors()) {
            List<ShortUrlDTO> shortUrls = shortUrlService.findAllPublicShortUrls();
            model.addAttribute("shortUrls", shortUrls);
            model.addAttribute("baseUrl", properties.baseUrl());
            return "index";
        }

        try {
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(form.originalUrl());
            var shortUrlDTO = shortUrlService.createShortUrl(cmd);
            redirectAttributes.addFlashAttribute("successMessage", "Short URL created successfully "+
                    properties.baseUrl()+"/s/"+shortUrlDTO.shortKey());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create short URL");

        }
        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    public String redirectToOriginalUrl(@PathVariable String shortKey){
        Optional<ShortUrlDTO> shortUrlDtoOptional = shortUrlService.findByShortKey(shortKey);
        if(shortUrlDtoOptional.isEmpty()) {
            throw new ShortUrlNotFoundException("Invalid short key: "+ shortKey);
        }
        ShortUrlDTO shortUrlDto = shortUrlDtoOptional.get();
        return "redirect:"+shortUrlDto.originalUrl();

    }


}
