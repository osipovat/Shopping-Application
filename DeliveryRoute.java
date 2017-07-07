package application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * This class represents a Delivery Route
 * @author group_0549
 *
 */
public class DeliveryRoute {
	public static final int DEF_MAX = 99;
	public static final int DEF_CAPACITY = 30;
	public static final int NULL_EDGE = 0;
	public static final int DEFAULT_WEIGHT = 1;
	private int numCities;
	private int maxCities;
	private City[] cities;
	private int[][] edges;
	private boolean[] marks; // marks[i] is mark for vertices[i]
	public int cost;
	private String pathname;

	/**
	 * This method constructs a DeliveryRoute instance and sets the maxCities, cities, marks, and edges to the default
	 * 
	 */
	public DeliveryRoute() {
		numCities = 0;
		maxCities = DEF_CAPACITY;
		cities = new City[DEF_CAPACITY];
		marks = new boolean[DEF_CAPACITY];
		edges = new int[DEF_CAPACITY][DEF_CAPACITY];
		this.pathname = "src/DataFiles/";
		this.loadCityFile();
	}
	
	
	
	
	public void loadCityFile(){		
		Path filename = FileSystems.getDefault().getPath(pathname+"City.csv");
		List<String> fileContent;
		try {
			fileContent = new ArrayList<>(Files.readAllLines(filename, StandardCharsets.UTF_8));
			for (int i = 0; i<fileContent.size(); i++) {
				this.cities[i] = new City(fileContent.get(i), 0);
				numCities++;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	
	
	
	
	
	
	/**
	 * This method finds and returns an index of the vertex if not found returns -1
	 * @param vertex -> the name of the city that is the vertex to be found
	 * @return -> the index of the vertex if it exists, otherwise returns -1
	 */
	public int helper (String vertex){
		for(int i=0; i<this.numCities; i++){
			if(vertex.equals(this.cities[i].getCity())==true){
				return i;
			}
		}
		return -1;
	}

	/**
	 * This method returns a list of all the cities
	 * @return -> list of all cities
	 */
	public City[] getCities() {
		return cities;
	}
	
	/**
	 * This method sets cities
	 * @param cityList -> list of all cities
	 */
	public void setCities(ArrayList<City> cityList) {
		for (int i = 0; i<cityList.size(); i++)
			cities[i] = cityList.get(i);		
	}
	

	/**
	 * This method returns a list of all the cities
	 * @return -> list of all cities
	 */
	public int getNumCities() {
		return this.numCities;
	}
	
	
	/**
	 * This method gets and returns the City using the name of the city
	 * @param city -> name of the city to be found
	 * @return -> City with the same name of the city
	 */
	public City getCity(String city) {
		for(City c :this.cities){
			if(c==null)break;
			if(c.getCity().equals(city))
				return c;
		}
		return null;
	}

	/**
	 * This method checks if the city list is empty
	 * @return -> true if it is empty, otherwise returns false
	 */
	public boolean isEmpty() {
		return (numCities == 0);
	}

	/**
	 * This method checks if the city list is full
	 * @return -> true if it is full, otherwise returns false
	 */
	public boolean isFull() {
		return (numCities == maxCities);
	}

	/**
	 * This method adds a city to the graph
	 * @param vertex -> the name of the City to be added
	 */
	public void addCity(String vertex)  {
		if(vertex==null) return;
		if(isEmpty()==true){ // if graph is empty
			City c = new City(vertex, 0);
			this.cities[0]=c;
			numCities+=1;
		}
		else if(helper(vertex)==-1){ // if graph is not empty and vertex is not in graph add to array of vertices
			City c = new City(vertex, 0);
			this.cities[this.numCities]=c;	
			numCities+=1;
		}
	}

	/**
	 * This method adds the route
	 * @param fromCity -> the name of the city shipping from 
	 * @param toCity -> the name of the city shipping towards
	 * @param distance -> the distance between the two cities (toCity and fromCity)
	 */
	public void addRoute(String fromCity , String toCity, int distance) {
		if(helper(fromCity)>=0 && helper(toCity)>=0){ // when both vertices are in array add edge
			if(	this.edges[helper(fromCity)][helper(toCity)]==0){
				this.edges[helper(fromCity)][helper(toCity)]=distance; // mark weight as distance
				this.edges[helper(toCity)][helper(fromCity)]=distance;
				
			}
		}
	}
	
	/**
	 * This method gets the Queue of Cities to the specified City
	 * @param vertex -> name of the City shipping towards
	 * @return -> the Queue of the the Cities going towards the city vertex
	 */
	public Queue<City> getToVertices(City vertex) {
		PriorityQueue<City> Q = new PriorityQueue<City>();
		for(int i=0; i<this.numCities; i++){ 
		  if(this.edges[helper(vertex.getCity())][i]>0){ //if there is a distance
			  Q.add(this.cities[i]);
		  }	
		}	
		return Q;
	}

	
	/**
	 * This method gets the distance from one city to another
	 * @param fromCity -> the name of the City moving from
	 * @param toCity -> the name of the City moving towards
	 * @return -> the distance between the two cities
	 */
	public int getDistance(City fromCity , City toCity) {
	
		return  this.edges[helper(fromCity.getCity())][helper(toCity.getCity())];
	}
	
	/**
	 * This method marks the cities in numCities as false
	 */
	public void clearMarks() {
		for (int i = 0; i < numCities; i++) 
			marks[i] = false;
	}

	/**
	 * This method marks the specified City
	 * @param vertex -> name of the City to be marked
	 */
	public void markCity(City vertex) {
		marks[helper(vertex.getCity())]=true;  // make vertex is marked
	}
	
	/**
	 * This method checks whether the specified City is marked
	 * @param vertex -> name of the City to be checked
	 * @return -> true when the vertex is marked, otherwise the vertex is not marked and returns false
	 */
	public boolean isMarked(City vertex)
	{	
		return marks[helper(vertex.getCity())]; // return true when vertex is marked
	}

	/**
	 * This method calculates the shortest route between two cities using the Dijkstra algorithm
	 * @param startCity -> the name of the start city 
	 * @param destCity -> the name of the destination city
	 * @return -> the ArrayList of the shortest route betweent the two cities
	 */
	public  ArrayList<String> Dijkstra(City startCity, City destCity)  {

		 PriorityQueue<City> Q = new PriorityQueue<City>();
		ArrayList<String> result= new ArrayList<String>();
		
		this.markCity(startCity);
		    startCity.setCost(0);
		    startCity.parent=startCity;
		    for(City city: cities){ 
		    	
		    	if (city==null)break;			    
		        if(!city.equals(startCity)){
		        	city.setCost(99);
		        }
		        Q.add(city);
		    }
		   

		    while(!Q.isEmpty()){
		        City v = (City) Q.remove();
		        this.markCity(v);
		        for(City s : this.getToVertices(v)){ // get_list(v) will return an iterable from the adjacency list at index v 
		        	int newdist =  v.getCost() +this.getDistance(s, v) ;
		        	
		            if(newdist < s.getCost() && Q.contains(s) && !isMarked(s)){
		                
		            	s.setCost(newdist);
		            	s.parent=v;
		                Q.add(s);
		            }else continue;
		        } 
		    }
		    if(this.isMarked(destCity)) {
            	result=this.retracePath(startCity, destCity);
            	this.clearMarks();
            	
            	return result;
           }else return null;
	}

	/**
	 * This method gets the Cost between the two Cities
	 * @param startNode -> the name of the City that is the start 
	 * @param endNode -> the name of the City that is the end
	 * @return -> the calculated Cost
	 */
	public int getCost(City endNode){
		cost = 0;
		cost+=endNode.getCost();
		return cost;
	}
	
	/**
	 * This method retraces the path between the startNode and the endNode
	 * @param startNode -> name of the City that is the start
	 * @param endNode -> the name of the City that is the end
	 * @return -> the route between the startNode City and the endNode City 
	 */
	public ArrayList<String> retracePath(City startNode, City endNode) {	
		City currentNode = endNode;
	    ArrayList<String> path = new ArrayList<String>();
		while (currentNode != startNode && currentNode != null) {
			currentNode.inPath = true;
			path.add(currentNode.getCity());
			currentNode = currentNode.parent;
		}
		path.add(currentNode.getCity());
		return path;
	}
	

}
