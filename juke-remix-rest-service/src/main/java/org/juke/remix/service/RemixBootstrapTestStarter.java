package org.juke.remix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.ConfigurableEnvironment;


@SpringBootApplication()
@EnableCaching
@EnableAspectJAutoProxy
@Profile("dev")
public class RemixBootstrapTestStarter implements CommandLineRunner {

	@Autowired
	private ConfigurableEnvironment env;
	
	private String appname="remix";
	public static void main(String[] args) {
		SpringApplication.run(RemixBootstrapTestStarter.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		String[] profiles = env.getActiveProfiles();
		
	}

}
