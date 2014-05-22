package gameOfTheGenerals;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")

/* http://127.0.0.1:8888/gameofthegenerals */
	
/*  2014-03-04 : 
 *   I am the game controller.  Everyone passes through me.
 *     - Users
 *     - Arbiter
 *     - Board
 *     - Soldiers
 *     
 *      Here's the plan:
 *      1.  I'm an HttpServlet.
 *      2.  I accept traffic from users.
 *      3.  Do depending on the user event.  (Is there any other event not triggered by users?)
 *           3.1  Setup board. Request incoming parameters are the array (String) of pieces with their positions.
 *                  Beginning from left to right squares, from 1st row to 3rd row.
 *                  Total of 27 characters. 1st row is 1st 9 chars. 2nd row is char 10 to 18. 3rd row is char 19 to 27.
 *                  Example : 0P00F00P054321CLMNABSYYPPPP ->  
 *                                       row 1 : blank Private blank blank FlagP blank blank Private blank
 *                                       row 2 : 5star 4star 3star 2star 1star Colonel LtCol Major Captain
 *                                       row 3 : 1stLt 2ndLt Sergeant Spy Spy Private Private Private Private
 *           3.2  Move a piece
 *           3.3  tbd
 *      4.  
 *      
 *      Note:
 *      1. Have doPost call doGet
 */
public class GameOfTheGeneralsServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Inside doGet");

		/*
		 *  Get the player's command, and do as player requested.
		 *   1.   Login
		 *   2.  Setup Board
		 *   3.  Move a piece
		 *   4.  Quit  
		 *   But first, need to force the user to login first.
		 *   
		 */
		
		switch (req.getParameter("ggCommand")) {
		
		   case "setupBoard": 
			    this.setupBoard(req.getParameter("ggCommand"), resp);
			    break;
			    
		   case "login": 
			    this.login(req, resp);
			    break;
	    
		   case "list": 
			    this.gamersList(req, resp);
			    break;

		   case "logoff": 
			try {
				this.logoff(req, resp);
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			    break;

		   case "challenge": 
			    this.challenge(req, resp);
			    break;
			    
		   case "challengeList": 
			    this.challengeList(req, resp);
			    break;
			    
		   case "challengeAccept": 
	            this.challengeAccept(req, resp);
	            break;
			    			    
		   default:  
			    resp.getWriter().println("switch default");
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");
		resp.getWriter().println("Inside doPost()");
		
		this.doGet(req, resp);  /* Let the doGet() method handle the doPost() method. */
	}
	
	private void setupBoard(String formation, HttpServletResponse resp1) 
	      throws IOException {
		/* formation = the soldier's places */
		/* Store the formation into DataStore */

		resp1.setContentType("text/plain");
		resp1.getWriter().println("Inside setupBoard()");
		resp1.getWriter().println(formation);
		
	}

	private void login(HttpServletRequest req1, HttpServletResponse resp1) 
		      throws IOException {
			/* formation = the soldier's places */
			/* Store the formation into DataStore */

			resp1.setContentType("text/plain");
			resp1.getWriter().println("<br>login()<br>");
		
    		/* Key aKey = KeyFactory.createKey("Gamer4", user.getNickname()); */
            /* Entity aGamer = new Entity("Gamer4", aKey); */
 
			User user = this.getUser(req1, resp1);
			
            Entity aGamer = new Entity("Gamer4", user.getNickname());
            aGamer.setProperty("nickName", user.getNickname());
            
            DatastoreService aDS = DatastoreServiceFactory.getDatastoreService();
			aDS.put(aGamer);		
			resp1.getWriter().println("Gamer added into datastore.");
		}
	

	private void logoff(HttpServletRequest req1, HttpServletResponse resp1) 
		      throws IOException, EntityNotFoundException {

			/* Delete user from DataStore */

			resp1.setContentType("text/plain");
			resp1.getWriter().println("<br>logoff()<br>");

			User user = this.getUser(req1, resp1);
			
			/* At this point, user is valid. Store user in database for persistence. */
			resp1.getWriter().println("Hello, " + user.getNickname());
			resp1.getWriter().println("You are about to logoff");
			
/*            Entity aGamer = new Entity("Gamer4");  */
           
            DatastoreService aDS = DatastoreServiceFactory.getDatastoreService();

            Key aKey = KeyFactory.createKey("Gamer4", user.getNickname());  
 /*         aGamer = aDS.get(aKey);  */
            aDS.delete(aKey);		
			resp1.getWriter().println("Gamer deleted from datastore.");
		}
	
	private void gamersList(HttpServletRequest req1, HttpServletResponse resp1) 
		      throws IOException {
			/* Return the list of gamers in the Datastore Gamer entity. */

			resp1.setContentType("text/plain");

			resp1.getWriter().println(" ");
			resp1.getWriter().println("Available gamers:");
			
            DatastoreService aDS = DatastoreServiceFactory.getDatastoreService();		
		    Query aQuery = new Query("Gamer4");
		    
		    /*
		    List<Entity> gamersList = aDS.prepare(aQuery).asList(FetchOptions.Builder.withLimit(5));
		    resp1.getWriter().println(gamersList);
		    */
		     
		    PreparedQuery aPreparedQuery = aDS.prepare(aQuery);
		    
		    for (Entity gamerslist : aPreparedQuery.asIterable()) {
		    	  String aNickName = (String) gamerslist.getProperty("nickName");
		    	  resp1.getWriter().println(" - " + aNickName);
		    	}
		    
		    resp1.getWriter().println(" ");
		    	
		}
	
	private void challenge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		/*  1.  For current user (aka challenger, aka invitor), create a new record in Challenge datastore
		 *  2.  For the invitee, create a new record in Invitation datastore.
        */
		
		String aUserNickname = req.getParameter("ggCommandParm1");	
		User user = this.getUser(req, resp);
		
		/* At this point, user is valid. Store user in database for persistence. */
		resp.getWriter().println("Challenger = " + user.getNickname());
		resp.getWriter().println("Challenges = " + aUserNickname);
		
	    /* xxxxx Get the gamer4 entity, using user.getNickname to query.  Then use getKey() when creating Challenge, making it a child of Gamer4 xxxx 
	     *
	     *xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	     *
	 
	     * */
		
		DatastoreService aDS = DatastoreServiceFactory.getDatastoreService();
		
		/* Get the key of current user */
      Query aQuery = new Query("Gamer4");
      Filter aFilter = new FilterPredicate("nickName", Query.FilterOperator.EQUAL, user.getNickname());
      aQuery.setFilter(aFilter);   
      PreparedQuery pq = aDS.prepare(aQuery);
      Entity aResult = pq.asSingleEntity();
	   resp.getWriter().println(aResult);
	   Key aKey = aResult.getKey();
		
	   Entity aChallenge = new Entity("Challenge", aKey);	    /* xxxxx Create this using the key of the current user */
	    aChallenge.setProperty("challenger", user.getNickname());
	    aChallenge.setProperty("opponent", aUserNickname);
	    aDS.put(aChallenge);		
		resp.getWriter().println("A challenge added into datastore.");

	}

	private void challengeList(HttpServletRequest req1, HttpServletResponse resp1) 
		      throws IOException {
			/* Return the list of gamers in the Datastore Gamer entity. */

			resp1.setContentType("text/plain");

			resp1.getWriter().println(" ");
			resp1.getWriter().println("Current challenges:");
			
            DatastoreService aDS = DatastoreServiceFactory.getDatastoreService();		
		    Query aQuery = new Query("Challenge");
		    
		    PreparedQuery aPreparedQuery = aDS.prepare(aQuery);
		    
		    for (Entity challengelist : aPreparedQuery.asIterable()) {
		    	  String aChallenger = (String) challengelist.getProperty("challenger");
		    	  String aOpponent = (String) challengelist.getProperty("opponent");
		    	  resp1.getWriter().println(aChallenger + " challenges " + aOpponent);
		    	}
		    
		    resp1.getWriter().println(" ");
		    	
		}

	private void challengeAccept(HttpServletRequest req1, HttpServletResponse resp1) 
	      throws IOException {
		/* User accepted a challege.  
		 *   - Check the challenge in Datastore Challenge entity.
		 *   - Create a new game in Datastore Game entity.
		 *   - Set the game status to "setup"  (later, it becomes ready/start,  then live, then finish.)
		 *   - Delete the challenge.
		 * */
	
		resp1.setContentType("text/plain");
	
		resp1.getWriter().println(" ");
		resp1.getWriter().println("Challenge accepted. ");
		
		String aUserNickname = req1.getParameter("ggCommandParm1");
		
		resp1.getWriter().println(aUserNickname);
			
	    DatastoreService aDS = DatastoreServiceFactory.getDatastoreService();		

	      Query aQuery = new Query("Challenge");
	      Filter aFilter = new FilterPredicate("opponent", Query.FilterOperator.EQUAL, aUserNickname);
	      aQuery.setFilter(aFilter);
	      
        PreparedQuery pq = aDS.prepare(aQuery);
        Entity aResult = pq.asSingleEntity();
	    
	    resp1.getWriter().println(aResult);
	    	
	}

	private User getUser(HttpServletRequest req1, HttpServletResponse resp1) 
			      throws IOException {
				/* Return the current user.
				 * If user not logged in, redirect.
				 *  */
				resp1.setContentType("text/plain");
				resp1.getWriter().println(" ");
				resp1.getWriter().println("Get user.");
				resp1.getWriter().println(" ");
				
				 UserService userService = UserServiceFactory.getUserService();
				 User user = userService.getCurrentUser();
				 
				if (user == null) { 
						/* Redirect user to login screen.  Then do over. */
						resp1.getWriter().println("user is null for now");
						resp1.sendRedirect(userService.createLoginURL("/ggPage001.jsp"));	
				}
				
				/* At this point, user is valid.  Return the user object */
				resp1.getWriter().println("Returning " + user.getNickname());
				return user;
		}
	
	
	/*  Persist following data:
	 *    1.   User data
	 *           1.1  Unique IDs : keyUserId; 2 UIDs needed, 1 for each player.
	 *    2.  Game data
	 *            2.1   Unique ID : keyGameId
	 *            2.2  Board
	 *                      2.2.1  Soldier positions : String of 72 characters.  (e.g.  1-9 is the first row, 10-18 is second row, etc.)
	 *    3.   
	 * Copy these lines to do following:
	 *   1.  Create a key
	
	   String guestbookName = req.getParameter("guestbookName");
	   Key guestbookKey = KeyFactory.createKey("Guestbook",  guestbookName);
	   
	   String content = req.getParameter("content");
	   Date date = new Date();
	   
	   Entity greeting = new Entity("Greeting", guestbookKey);
	   greeting.setProperty("user", user);
	   greeting.setProperty("date", date);
	   greeting.setProperty("content", content);
	   
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(greeting);
	*/
}
