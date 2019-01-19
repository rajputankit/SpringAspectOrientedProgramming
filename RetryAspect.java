package edu.sjsu.cmpe275.aop.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;

@Aspect
public class RetryAspect {
			
	@Autowired	
	TweetStatsImpl tweetStatsInstance;	
	
	int MAX_RETRIES = 3;
		
	/**
	 * 	Tweet Advice which uses Aspect Oriented Programming (around advice) to execute the tweet() method of TweetServiceImpl class 
	 * and performs retry twice (in addition to the original attempt to tweet) in case there is a Network failure (catches the IOException). 
	 * It also catches the IllegalArgumentException if the length of the tweeted message if more than 140 characters 
	 * @param proceedingJoinPoint
	 * @param user
	 * @param message
	 * @author Ankit Rajput	 *  
	 */
	@Around("execution(* edu.sjsu.cmpe275.aop.TweetServiceImpl.tweet(..)) && args(user, message)")
	public void tweetAdvice(ProceedingJoinPoint proceedingJoinPoint, String user, String message){
		
		if(tweetStatsInstance.isValidString(user) && tweetStatsInstance.isValidString(message)){
		
			for (int i = 0; i < MAX_RETRIES; ) {			
				try {	
					proceedingJoinPoint.proceed();
					i = MAX_RETRIES;
				} 
				catch (IOException e) {
					i++;				
				} 	
				catch (IllegalArgumentException e){	
					i = MAX_RETRIES;			
				}
				catch(Exception e){
					e.printStackTrace();
				}			
				catch (Throwable e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
			
	/**
	 * 
	 * Follow Advice which uses Aspect Oriented Programming (around advice) to execute the follow() method of the TweetServiceImpl class 
	 * and performs retry twice (in addition to the original attempt to tweet) in case of Network Failure (catches the IOException) 
	 * @param proceedingJoinPoint
	 * @param follower
	 * @param followee
	 * @author Ankit Rajput
	 * 
	 */
	@Around("execution(* edu.sjsu.cmpe275.aop.TweetServiceImpl.follow(..)) && args(follower, followee)")	
	public void followAdvice(ProceedingJoinPoint proceedingJoinPoint, String follower, String followee){

		if(tweetStatsInstance.isValidString(follower) && tweetStatsInstance.isValidString(followee)){
			
			for (int i = 0; i < MAX_RETRIES;) {	
				try {
					proceedingJoinPoint.proceed();
					i = MAX_RETRIES;
				} catch (IOException e) {
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			countAttemptOnIOFailure(follower, followee);
		}						
	}
	
	
	/**
	 * Method which fill the stats for the activeUser if the attempt to follow fails during the follow() method 
	 * @param follower
	 * @param followee
	 */
	public void countAttemptOnIOFailure(String follower, String followee){	
		
		follower = follower.trim();
		followee = followee.trim();
		if(tweetStatsInstance.isValidString(follower) && tweetStatsInstance.isValidString(followee)){
			
			if (!tweetStatsInstance.hasAlreadyAttemptedOrFollowing(tweetStatsInstance.getActiveUserMap(), follower, followee)) {	
				tweetStatsInstance.fillActiveUserMap(tweetStatsInstance.getActiveUserMap(), follower, followee);
			}	
		}	
	}	
}








	






















