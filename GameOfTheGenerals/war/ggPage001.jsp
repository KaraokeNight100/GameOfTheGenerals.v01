<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
  </head>

  <body>
  <br>This is ggPage001.jsp.
  <br>

  <form action="/gameofthegenerals" method="post">
    <div><textarea name="ggCommand" rows="1" cols="60"></textarea></div>
    <div><textarea name="ggCommandParm1" rows="1" cols="60"></textarea></div>
    <div><input type="submit" value="Enter GG command."/> </div>
  </form>
  
  <div>
  <br>Available actions:
  <br>	- login
  <br>	- logoff
  <br>	- list
  <br>	- setupBoard
  <br>    - challenge:nickname 
  <br>    - challengeList
  <br>    - challengeAccep
  <br>    - tbd
  </div>
  
  </body>
</html>



