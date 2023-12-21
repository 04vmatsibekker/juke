package com.example;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements IGreetingsService {
	private static final String template = "Hello, %s!";

	private final AtomicLong counter = new AtomicLong();
	@Override
	public Greeting greeting(String name) {
		// TODO Auto-generated method stub

		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

}
