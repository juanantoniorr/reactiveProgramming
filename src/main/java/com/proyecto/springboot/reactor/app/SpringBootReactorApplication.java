package com.proyecto.springboot.reactor.app;

import java.util.ArrayList;
import java.util.List;

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
		List <String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add("Raul Lopez");
		usuariosList.add("Juan Rosas");
		usuariosList.add("Diego Lopez");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		//Cuando hay uno vacio salta una excepcion y no continua el flujo
		//Cada flujo es diferente y hay que subscribirse a ellos
		Flux <String> nombres = Flux.fromIterable(usuariosList); //Flux.just("Andres Guzman","Raul Lopez","Juan Rosas", "Diego Lopez", "Bruce Lee", "Bruce Willis");
		Flux <Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase() ))
				.filter(usuario -> usuario.getNombre().equals("BRUCE"))
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
		usuarios.subscribe(e -> logger.info(e.getNombre()),
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
