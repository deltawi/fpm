package com.fpm.backend;

import javax.annotation.PostConstruct;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eclipsesource.json.JsonObject;
import com.fpm.backend.data.SystemX;

public class BluemixService {

	public static String url = "https://ibm-watson-ml.mybluemix.net/pm/v1/score/anomaly_detection1";
	public static String accessKey = "RrKz4w6jJTr39yEiLpE6AiHXO91SbHvumHeMclVAf0jqs24Sa2ve/LPyw/GfesVKHxGxQ3pIogjgEOjN0TGDTcL0h32gVzPkwMbmHXNpi+FQYUqQmv73SQJrb1WXWeZv";
	public static String username = "cbbd673c-bcc5-4153-93a0-f8a60a15ee96";
	public static String password = "af0da8d4-0812-4ea6-9d2c-1a8a8853a728";


	public static SystemX callAnomalyService(SystemX systemX, WebTarget target){
		
		System.out.println(url);
		String jsonData = "{\"tablename\": \"test.csv\", \"header\": [\"P1\",\"P2\",\"P3\"], \"data\":[[\""+ (int) systemX.p1 +"\" , \""+ (int) systemX.p2+"\" , \""+ (int) systemX.p3+"\""+"]]}";
		System.out.println(jsonData);
		try{
			System.out.println("here 1");

			String response = target
			        .request().post(Entity.entity(jsonData, MediaType.APPLICATION_JSON_TYPE),String.class);
			
			//String response = "[{\"header\":[\"AnomalyIndex\",\"Field_1\",\"ANOMALY\"],\"data\":[[566.4496785919277,\"P3\",\"T\"]]}]";
			System.out.println("here 2");

			System.out.println(response);
			
			try{
				JSONArray json = new JSONArray(response);
				JSONObject jsonobj = json.getJSONObject(0);
				System.out.println(jsonobj.getJSONArray("data").getJSONArray(0).toString());
				JSONArray jsonDatarsp = jsonobj.getJSONArray("data").getJSONArray(0);
				systemX.setAnomalyIndex(jsonDatarsp.getDouble(0));
				systemX.setAnomalousField(jsonDatarsp.getString(1));
				
			}catch( JSONException e){
				e.printStackTrace();
			}
			System.out.println("here 3");
			
			

			
			return systemX;
		}catch (WebApplicationException e) {

			WebApplicationException exx = (WebApplicationException) e;
		    exx.printStackTrace();
		    System.out.println(e.getMessage());
		    System.out.println(e.getCause());
		    System.out.println(e.getResponse().getHeaders());
		    System.out.println(e.getResponse().readEntity(String.class));
		    System.out.println(e.getResponse().getStringHeaders());
		    System.out.println(e.getResponse().getMetadata());

		    System.out.println(e.getStackTrace().toString());
		    System.out.println(e.getSuppressed().toString());
	        return null;
	      }
		
		/*catch(WebApplicationException e){
			System.out.println("here 4");

			System.out.println("Nothing :(");
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
			System.out.println(e.getResponse().serverError().toString());
			System.out.println("here 5");
			return null;
		}*/

	}

}
