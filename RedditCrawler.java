package com.panzoto.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedditCrawler {

	public RedditCrawler() {

	}

	// crawl the reddit.com front page and get the html as a string
	public String readFirstPage() {
		String firstPage = "";
		try {
			URL my_url = new URL("http://reddit.com");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			String strTemp = "";
			while ((strTemp = br.readLine()) != null) {
				firstPage = strTemp;
				System.out.println(strTemp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return firstPage;
	}

	// read the topics on the front page
	public ArrayList<String> readTopic(String whole) {

		ArrayList<String> topics = new ArrayList<String>();

		// use pattern "1" >.*?</a> where .*? is the topic
		Pattern pattern = Pattern.compile("(\"1\" >.*?</a>)");
		
		// find patterns that match the front page in html
		Matcher matcher = pattern.matcher(whole);
		
		// find all the text that matches
		while (matcher.find()) {
			
			// take out the extra characters used for pattern and leave the topic
			String topic = matcher.group(1).substring(5,
					matcher.group(1).length() - 4);
			
			// replace the topic and replace &quot; with "
			topic = topic.replaceAll("&quot;","\"");
			topic = topic.replaceAll("\'","\'\'");
			topic = topic.replaceAll("’","\'\'");

			System.out.println(topic);
			topics.add(topic);
		}

		return topics;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		
		// make a crawler that works for reddit.com
		RedditCrawler crawler = new RedditCrawler();
		String firstPage = crawler.readFirstPage();

		ArrayList<String> topics = new ArrayList<String>();

		// return topics in an arrayList
		topics = crawler.readTopic(firstPage);

		// make a table in the database
		Sqlite database = new Sqlite();
		
		// create a table for the Reddit 
		database.createTable("Reddit");

		// insert a records for each topic
		database.insertRecord("Reddit","Topic", topics);
		
		/*
		for (int i = 0; i < topics.size(); i++) {
			database.insertRecord("Reddit", "Topic", topics.get(i));
		}
		*/
	}

}