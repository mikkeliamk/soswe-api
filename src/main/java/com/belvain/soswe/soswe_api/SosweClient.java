package com.belvain.soswe.soswe_api;

import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import com.belvain.soswe.soswe_api.TypeNotSupportedException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SosweClient {
	
	private String host;
	private int port;
	private Client client;
	
	/**
	 * Create a new soswe client
	 * @param host Host where soswe is running ie. http://localhost
	 * @param port Port where soswe is running
	 */
	public SosweClient(String host, int port){
		this.client = new Client();
		this.setHost(host);
		this.setPort(port);
	}

	/**
	 * Starts workflow
	 * @param workflowname Name of a workflow to be started
	 * @param opts Possible options to be added to workflow context
	 */
	public String startWorkflow(String workflowname, Map<String, Object> opts){
		
		String reguest = "start/"+workflowname;
		if(opts != null){
		    Set<String> keys = opts.keySet();
			reguest += "?";
			for(String key : keys){
				reguest += key+"="+opts.get(key)+"&";
			}
			
			reguest = reguest.substring(0, reguest.lastIndexOf("&"));
		}
		return sendRequest(reguest, MediaType.TEXT_PLAIN);
	}
	
	/**
	 * @return nodes currently in cluster
	 */
	public String getNodes(){
		String request = "nodes";
		return sendRequest(request, MediaType.TEXT_PLAIN);
	}
	
	/**
	 * 
	 * @return Workflow currently scheduled
	 */
	public String getCurrentJobs(){
		String request = "currentjobs";
		return sendRequest(request, MediaType.APPLICATION_XML);
	}
	
	/**
	 * 
	 * @return Status of workflow engine
	 */
	public String getStatus(){
		String request = "status";
		return sendRequest(request, MediaType.TEXT_PLAIN);
	}
	
	/**
	 * Returns list of available workflows in selected format
	 * @param type Type to return the list in, currently accepted application/json, application/xml
	 * @return List of available workflows
	 * @throws TypeNotSupportedException If type is not accepted
	 */
	public String getWorkflows(String type) throws TypeNotSupportedException{
		
		String format = "";
		
		if(type.equals(MediaType.APPLICATION_JSON)){
			format = "json";
		}else if(type.equals(MediaType.APPLICATION_XML)){
			format = "xml";
		}else{
			throw new TypeNotSupportedException("Type not supported");
		}
		
		String reguest = "list/"+format;
		
		return sendRequest(reguest, type);
		
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	private String sendRequest(String reguest, String type){
		
		WebResource webres = client.resource(this.host+":"+this.port+"/soswe/"+reguest);
		ClientResponse resp = webres.accept(type).get(ClientResponse.class);
		
		if(resp.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ resp.getStatus());
		}
		
		return resp.getEntity(String.class);
	}

}
