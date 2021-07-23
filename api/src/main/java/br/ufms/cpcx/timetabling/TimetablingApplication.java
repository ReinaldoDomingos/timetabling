package br.ufms.cpcx.timetabling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TimetablingApplication {

    @Autowired
//    @Qualifier("nomeconfiguracao")
    @Value("${application.name}")
    private String varComValorConfiguracao;

    public static void main(String[] args) {
        SpringApplication.run(TimetablingApplication.class, args);
    }

    @GetMapping("/profile")
    public String hello() {
        return "Olá " + varComValorConfiguracao;
    }
}
