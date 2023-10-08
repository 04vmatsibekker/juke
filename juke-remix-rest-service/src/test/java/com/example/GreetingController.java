/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.juke.framework.api.JukeFactory;
import org.juke.framework.api.JukeState;

import javax.annotation.PostConstruct;


/**
 *
 *
 */
@RestController
@Service
public class GreetingController  implements IGreetingController{

	

@Autowired
private IGreetingsService service;
	@Override
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return service.greeting(name);

	}
	@PostConstruct
	public void initialized() {
		
		this.service= (IGreetingsService) new JukeFactory<IGreetingsService>().newInstance(this.service,
			IGreetingsService.class, JukeState.JUKE);
		System.out.println("initialized");
	}
}
