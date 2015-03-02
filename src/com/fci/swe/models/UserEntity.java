package com.FCI.SWE.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity {
	private String name;
	private String email;
	private String password;
	private ArrayList <String> friends;
	private ArrayList <String> requests;
	private static UserEntity currentUser;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		friends=new ArrayList<String>();
		requests=new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}
	public void setFriends(ArrayList<String> friends){
		this.friends=friends;
	}
	public void setRequests(ArrayList<String> requests){
		this.requests=requests;
	}
	
	public ArrayList<String> getFriends(){
		return this.friends;
	}
	public ArrayList<String> getRequests(){
		return this.requests;
	}
	public static void setUser(UserEntity user){
		currentUser=user;
	}
	public static UserEntity getUser() {
		return currentUser;
	}
	public void addFriend(String email)
	{
		this.friends.add(email);
	}
	public void addRequest(String email)
	{
		this.requests.add(email);
	}
	public void removeRequest(String email)
	{
		this.requests.remove(email);
	}
	
	/**
	 * 
	 * This static method will form UserEntity class using json format contains
	 * user data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	/*
	public static UserEntity getUser(String json) {

		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			return new UserEntity(object.get("name").toString(), object.get(
					"email").toString(), object.get("password").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
    
	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				if(entity.getProperty("friends")!=null){
					returnedUser.setFriends((ArrayList<String>)entity.getProperty("friends"));
				}
				if(entity.getProperty("requests")!=null){
					returnedUser.setRequests((ArrayList<String>)entity.getProperty("requests"));
				}
				currentUser=returnedUser;
				return returnedUser;
			}
		}

		return null;
	}
	public static UserEntity getUser(String email) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			System.out.println(entity.getProperty("email").toString()+" "+email);
			
			if (entity.getProperty("email").toString().equals(email)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				if(entity.getProperty("requests")!=null){
					returnedUser.setRequests((ArrayList<String>)entity.getProperty("requests"));
				}
				return returnedUser;
			}
		}

		return null;
	}
	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity employee = new Entity("users", list.size() + 1);

		employee.setProperty("name", this.name);
		employee.setProperty("email", this.email);
		employee.setProperty("password", this.password);
		employee.setProperty("friends",new ArrayList<String>());
		employee.setProperty("requests",new ArrayList<String>());
		datastore.put(employee);
		System.out.println(employee.getKey());
		
		return true;

	}
	public static boolean updateFriends(UserEntity user){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(user.getEmail())) {
				
				entity.setProperty("friends",user.getFriends());
				datastore.put(entity);
				System.out.println("here2");
				return true;
			}
		}
		System.out.println("here2");
		return false;
	}
	public static boolean updateRequests(UserEntity user){
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(user.getEmail())) {
				entity.setProperty("requests",user.getRequests());
				datastore.put(entity);
				System.out.println("here2");
				return true;
			}
		}
		System.out.println("here2");
		return false;
	}
}
