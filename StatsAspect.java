package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.TweetStatsImpl;
	
@Aspect	
public class StatsAspect {

	@Autowired
	TweetStatsImpl tweetStatsInstance;		
			
	/**
	 * Advice which which uses Aspect Oriented Programming (AfterReturning) to save the Stats related to the user tweets
	 * after the tweet() method has successfully executed 
	 * @param user	
	 * @param message
	 * @param returnString
	 * @author Ankit Rajput
	 */		
	@AfterReturning(pointcut="execution(* edu.sjsu.cmpe275.aop.TweetServiceImpl.tweet(..)) && args(user, message)", returning="returnString")
	public void fillUserTweetsMap(String user, String message, Object returnString){
				
		user = user.trim();	
		message = message.trim();
		if(tweetStatsInstance.isValidString(user) && tweetStatsInstance.isValidString(message)){	
			tweetStatsInstance.fillUserMap(tweetStatsInstance.getUserTweetsMap(), user, message);
			tweetStatsInstance.fillUserStats(tweetStatsInstance.getProductiveUserMap(), user, message);
			if (tweetStatsInstance.getLengthOfLongestTweet() < message.length())
				tweetStatsInstance.setLengthOfLongestTweet(message.length());
		} 
	}
		
	
	/**
	 * Advice which which uses Aspect Oriented Programming (AfterReturning) to save Stats related to user followers / followees
	 * after the successful execution of the follow() method 
	 * @param follower
	 * @param followee
	 * @param returnString
	 * @author Ankit Rajput
	 */
	@AfterReturning(pointcut="execution(* edu.sjsu.cmpe275.aop.TweetServiceImpl.follow(..)) && args(..) && args(follower, followee)", returning="returnString")		
	public void fillUserFollowMap(String follower, String followee, Object returnString){	
				
		follower = follower.trim();
		followee = followee.trim();
		if(tweetStatsInstance.isValidString(follower) && tweetStatsInstance.isValidString(followee)){	
				
			if (!tweetStatsInstance.hasAlreadyAttemptedOrFollowing(tweetStatsInstance.getUserFollowMap(), follower, followee)){ 			
				tweetStatsInstance.fillUserMap(tweetStatsInstance.getUserFollowMap(), follower, followee);								
				tweetStatsInstance.fillActiveUserMap(tweetStatsInstance.getActiveUserMap(), follower, followee);			
			}
		}		
	}	
}





















