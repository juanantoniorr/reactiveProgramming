package com.proyecto.springboot.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.proyecto.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Cuando hay uno vacio salta una excepcion y no continua el flujo
		Flux <Usuario> nombres = Flux.just("Andres","Raul","Juan", "Diego")
				.map(nombre -> new Usuario(nombre.toUpperCase(), null))
		.doOnNext(usuario -> {
			if (usuario ==null) {
				throw new RuntimeException("Nombres no pueden estar vacíos");
				}
			System.out.println(usuario);
			
			
		})
		.map(usuario -> {
			String nombre = usuario.getNombre().toLowerCase();
			usuario.setNombre(nombre);
			return usuario;
		});
		//te pide un consumidor Consumer de la clase que sea 
		nombres.subscribe(e -> logger.info(e.getNombre()),
				//el error viene de la clase throwable 
				error -> logger.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						logger.info("Ha finalizado la ejecucion del observable");
						
					}
				}
				);
	}

}
