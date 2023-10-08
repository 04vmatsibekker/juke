package org.juke.remix.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.juke.framework.api.JukeState;
import org.juke.framework.exception.JukeAccessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Controller
@RequestMapping("/service")
public class RemixWebController {
	private static Logger log = LoggerFactory.getLogger(RemixWebController.class);
	
	@Autowired
	RemixService remixService;
	
	@RequestMapping(value="/replay/disable", method = {RequestMethod.GET}, produces= {"application/json"})
	public @ResponseBody
	ResponseEntity<String> disable(){
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		String response=null;
		try{
			response = remixService.disable();
		}catch (JukeAccessException e) {
			e.printStackTrace();
		}
		
		if (RemixUtil.OK.equalsIgnoreCase(response)) 
			return new ResponseEntity<String>("juke Mocking is now disabled.",HttpStatus.OK);
		else
			return new ResponseEntity<String>("Failing to disable juke.",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@RequestMapping(value="/replay/enable", method = {RequestMethod.GET}, produces= {"application/json"})
	public @ResponseBody
	ResponseEntity<String> enable(){
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		String response=null;
		try{
			response = remixService.enable();
		}catch (JukeAccessException e) {
			e.printStackTrace();
		}
		
		if (RemixUtil.OK.equalsIgnoreCase(response)) 
			return new ResponseEntity<String>("juke Mocking is now enabled.",HttpStatus.OK);
		else
			return new ResponseEntity<String>("Failing to enable juke.",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@RequestMapping(value="/replay/start", method = {RequestMethod.GET}, produces= {"application/json"})
	public @ResponseBody
	ResponseEntity<String> beginReplay(@RequestParam("track") String track){
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		if (track == null || track.trim().length() == 0)
			track = "track";
		
	
		String response=null;
		try{
			response = remixService.beginReplay(track);
		}catch (JukeAccessException e) {
			e.printStackTrace();
		}
		
		if (RemixUtil.OK.equalsIgnoreCase(response)) 
			return new ResponseEntity<String>("Starting Repla Test Run: "+track,HttpStatus.OK);
		else
			return new ResponseEntity<String>("Ending Repla Test Run "+track,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@RequestMapping(value="/record/start", method = {RequestMethod.GET}, produces= {"application/json"})
	public @ResponseBody
	ResponseEntity<String> beginRecord(@RequestParam("track") String track){
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		if (track == null || track.trim().length() == 0)
			track = "track";
		
	
		String response=null;
		try{
			response = remixService.beginjuke(track);
		}catch (JukeAccessException e) {
			e.printStackTrace();
		}
		
		if (RemixUtil.OK.equalsIgnoreCase(response)) 
			return new ResponseEntity<String>("Starting Test Run: "+track,HttpStatus.OK);
		else
			return new ResponseEntity<String>("Ending Test Run "+track,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value="/record/end", method = {RequestMethod.GET}, produces= {"application/json"})
	public @ResponseBody
	ResponseEntity<String> endRecord(HttpServletResponse response){
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		String name = System.getProperty("juke.zip")!= null? System.getProperty("juke.zip"):"juke.zip";
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition", "attachment; filename=\""+name+".zip\"");
		if (JukeState.getGlobaljuke()==null) {
			return ResponseEntity.badRequest().build();
		}
		try {
			remixService.endJuke();
			try {
				RemixUtil.write(remixService.path(),response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ResponseEntity.status(500).build();
			}
			
		}catch (JukeAccessException e) {
			ResponseEntity.status(500).build();
		}
		return ResponseEntity.ok().build();
		
	}
	
	//http://<host>/service/remix/delaySchedule?classAndMethodSequence=com.example.IGreetingsService.$greeting.1&waitTimeInMS=10000
	@RequestMapping(value="/remix/delaySchedule", method = {RequestMethod.GET}, produces= {"application/json"})
	public ResponseEntity<String> remixDelaySchedule(@RequestParam("classAndMethodSequence") String classAndMethodSequence,
														@RequestParam("waitTimeInMS") long waitTimeInMS){
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		remixService.remixDelaySchedule(classAndMethodSequence, waitTimeInMS);
		return new ResponseEntity<String> ("adding delay in processing for"+classAndMethodSequence+" = "+ waitTimeInMS, HttpStatus.OK);
		
	
}
	//http://localhost:8080/service/remix/exceptionSchedule?classAndMethodSequence=com.example.IGreetingsService.$greeting.3&exception=IOException&exceptionMessage=IOException
	@RequestMapping(value="/remix/exceptionSchedule", method = {RequestMethod.GET}, produces= {"application/json"})
	public ResponseEntity<String> remixExceptionScheduleSchedule(@RequestParam("classAndMethodSequence") String classAndMethodSequence,
														@RequestParam("exception") String exception,	@RequestParam("exceptionMessage") String exceptionMessage ) throws Exception{
		if (JukeState.getGlobaljuke() == null) {
			return new ResponseEntity<String>("Unavailable Service", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		remixService.remixExceptionSchedule(classAndMethodSequence, exception,exceptionMessage);
		return new ResponseEntity<String> ("adding exception in processing for"+classAndMethodSequence+" = "+ exception, HttpStatus.OK);
		
	
}
	
	
	
}
