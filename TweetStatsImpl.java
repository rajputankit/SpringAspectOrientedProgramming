package edu.sjsu.cmpe275.aop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetStatsImpl implements TweetStats {

	private static Map<String, List<String>> userTweetsMap = new HashMap<String, List<String>>();
	private static Map<String, List<String>> userFollowMap = new HashMap<String, List<String>>();
	private static Map<String, List<String>> activeUserMap = new HashMap<String, List<String>>();																																						
	private static Map<String, Integer> productiveUserMap = new HashMap<String, Integer>();	
	private static int lengthOfLongestTweet = 0;		

	
	/**
	 * This method resets all the 3 Stats being maintained by the TweetStatsImpl class for this application.
	 * activeUserMap, productiveUserMap and lengthOfLongestTweet are all reset.
	 * @author Ankit Rajput
	 */
	@Override
	public void resetStats() {		

		activeUserMap.clear();
		productiveUserMap.clear();	
		userFollowMap.clear();
		userTweetsMap.clear();
		lengthOfLongestTweet = 0;
	}

	
	/**
	 * @return This method returns length of the longest tweet message since the start of this application or the last Stats Reset
	 * If no tweet messages were successfully processed and saved, it will return 0 
	 * @author Ankit Rajput
	 */
	@Override
	public int getLengthOfLongestTweet() {		

		return lengthOfLongestTweet;
	}


	/**
	 * @return This method returns the user who tried to follow the most users irrespective of the fact whether he succeeded or not
	 * If two or more such users exist, they will be sorted and the user who is 1st in the alphabetical order will be return. 
	 * If no user attempted to follow any other user, it will return null
	 * @author Ankit Rajput
	 */
	@Override
	public String getMostActiveFollower() {

		int tempMostActive = 0;
		List<String> tempMostActiveUserList = new ArrayList<String>();	

		for (String user : activeUserMap.keySet()) {		
			if(tempMostActive < activeUserMap.get(user).size()){				
				tempMostActive = activeUserMap.get(user).size();
				tempMostActiveUserList.clear();
				tempMostActiveUserList.add(user);	
			}			
			else if(tempMostActive == activeUserMap.get(user).size()){			
				tempMostActiveUserList.add(user);
			}
		}	

		if(tempMostActiveUserList.size() == 1){
			return tempMostActiveUserList.get(0);	
		}
		else if(tempMostActiveUserList.size() > 1){
			Collections.sort(tempMostActiveUserList);
			return tempMostActiveUserList.get(0);	
		}	

		return null;
	}

	/**
	 * @return This method returns the user whose length of all the successful tweets is largest since the start of the application.
	 * If two or more such users exist, all these users will be sorted and the user 1st in the alphabetical order will be returned
	 * If no user was able to successfully tweet any message, it will return null
	 * @author Ankit Rajput
	 */
	@Override
	public String getMostProductiveUser() {

		int tempLengthOfAllMessages = 0;
		List<String> tempMostProductiveUserList = new ArrayList<String>();

		for (String user : productiveUserMap.keySet()) {

			if(tempLengthOfAllMessages < productiveUserMap.get(user)){	
				tempLengthOfAllMessages = productiveUserMap.get(user);
				tempMostProductiveUserList.clear();
				tempMostProductiveUserList.add(user);	
			}
			else if(tempLengthOfAllMessages == productiveUserMap.get(user)){
				tempMostProductiveUserList.add(user);
			}			
		}

		if(tempMostProductiveUserList.size() == 1){
			return tempMostProductiveUserList.get(0);
		}
		else if(tempMostProductiveUserList.size() > 1){
			Collections.sort(tempMostProductiveUserList);
			return tempMostProductiveUserList.get(0);	
		}

		return null;
	}


	/**
	 * This method fills the productiveUserMap. It is used to save mapping related to length of all user tweets. 
	 * @param map
	 * @param key
	 * @param value
	 */
	public void fillUserStats(Map<String, Integer> map, String key, String value){

		key = key.trim();
		value = value.trim();

		if (map.containsKey(key)) {

			Integer lengthOfSuccessfulMessages = map.get(key);
			map.put(key, lengthOfSuccessfulMessages + value.length());
		} 
		else {
			map.put(key, value.length());
		} 
	}

	
	/**
	 * This method fills the userTweetsMap and userFollowMap. userTweetsMap is used to method is used to 
	 * save all the mapping related to user tweets. userFollowMap is used to save all the mapping related user followers/followees 
	 * @param map
	 * @param key
	 * @param value
	 */
	public void fillUserMap(Map<String, List<String>> map, String key, String value){

		if (map.containsKey(key)) {

			List<String> tempList = map.get(key);
			tempList.add(value);
		} else {

			List<String> tempList = new ArrayList<String>();
			tempList.add(value);
			map.put(key, tempList);
		} 
	}

	
	/**
	 * fills activeUserMap
	 * This method fills the activeUserMap. activeUserMap keeps track of users who tried to follow other users irrespective
	 * of the fact whether they were successful or not. Follow attempt related to different users is counted. Follow attempt
	 * to same user multiple times is counted only once. 
	 */
	public void fillActiveUserMap(Map<String, List<String>> map, String key, String value){

		if (map.containsKey(key)) {	

			if (!map.get(key).contains(value)) {		
				List<String> tempList = map.get(key);
				tempList.add(value);
			}
		} else {

			List<String> tempList = new ArrayList<String>();
			tempList.add(value);
			map.put(key, tempList);
		} 
	}


	/**
	 * This method checks if a user has already attempted to follow another user or is currently following another user. If 
	 * the user has already attempted to follow, or is currently following, it returns true. It also validates if the user is trying 
	 * to follow himself. Otherwise, it returns false.
	 * @param map
	 * @param follower
	 * @param followee
	 * @return
	 */
	public boolean hasAlreadyAttemptedOrFollowing(Map<String, List<String>> map, String follower, String followee){	

		List<String> tempUserFollowList = map.get(follower);			
		
		if(follower == followee)
			return true;	
		if(tempUserFollowList != null && tempUserFollowList.contains(followee))	
			return true;
		return false;
	}


	/**
	 * This method validates a string value and return false if the string is empty or null or has length == 0
	 * @param string
	 * @return
	 */
	public boolean isValidString(String string){

		if(string == "" || string == null || string.length() == 0)
			return false;
		return true;
	}

	//********************************************************
	//ALL GETTERS AND SETTERS
	//********************************************************
	
	
	public Map<String, List<String>> getUserTweetsMap() {
		return userTweetsMap;
	}
	
	public void setUserTweetsMap(Map<String, List<String>> userTweetsMap) {
		this.userTweetsMap = userTweetsMap;
	}

	public Map<String, List<String>> getUserFollowMap() {
		return userFollowMap;
	}

	public void setUserFollowMap(Map<String, List<String>> userFollowMap) {
		this.userFollowMap = userFollowMap;
	}

	public Map<String, List<String>> getActiveUserMap() {
		return activeUserMap;
	}

	public void setActiveUserMap(Map<String, List<String>> activeUserMap) {
		this.activeUserMap = activeUserMap;
	}

	public Map<String, Integer> getProductiveUserMap() {
		return productiveUserMap;
	}

	public void setProductiveUserMap(Map<String, Integer> productiveUserMap) {
		this.productiveUserMap = productiveUserMap;
	}

	public void setLengthOfLongestTweet(int lengthOfLongestTweet) {
		this.lengthOfLongestTweet = lengthOfLongestTweet;
	}
}




