package es.ucm.fdi.tusnoficias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan
public class IwBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(IwBaseApplication.class, args);
	}
}
