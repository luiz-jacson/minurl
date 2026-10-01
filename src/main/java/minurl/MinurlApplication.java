package minurl;

import minurl.service.ConsumoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinurlApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MinurlApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        ConsumoApi req = new ConsumoApi();

        System.out.println(req.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c"));

    }
}
