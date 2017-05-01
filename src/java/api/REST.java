/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

/**
 *
 * @author fatpenguino
 */
import beans.RemoteAPI;
import entities.ProcessDefinition;
import entities.ProcessInstance;
import entities.TaskSummary;
import entities.User;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*; 
@Stateless(mappedName = "rest")
public class REST implements RemoteAPI {

    public static void main(String[] args) throws Exception {
		// start a new process instance
		//send("17350@iitu.kz", "beibut rak", "lol kek cheburek");
                URL url = new URL("http://localhost:8080/jbpm-console/rest/runtime/org.jbpm:Evaluation:1.0/process/evaluation/start?map_employee=krisv&map_reason=Need%20a%20raise");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// System.out.println(format(response));
		url = new URL("http://localhost:8080/jbpm-console/rest/task/" + 1 + "/complete?map_performance=acceptable");
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode("mary:mary"));
		String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		// System.out.println(format(response));

		System.out.println("Process instance completed");
	}
	
    @Override
    public  List<TaskSummary> getPotentialTasks(User user){
        List<TaskSummary> tasks=new ArrayList<TaskSummary>();
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/task/query?potentialOwner="+user.getLogin());
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nList = doc.getElementsByTagName("task-summary");
	for (int i=0; i<nList.getLength(); i++){
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
            TaskSummary task=new TaskSummary();
            Element section = (Element) nList.item(i);   
	    task.setStatus(section.getElementsByTagName("status").item(0).getTextContent());
            task.setTaskId(section.getElementsByTagName("id").item(0).getTextContent());
            task.setName(section.getElementsByTagName("name").item(0).getTextContent());
            task.setDescription(section.getElementsByTagName("description").item(0).getTextContent());
            try {
                task.setCreateOn(formatter.parse(section.getElementsByTagName("created-on").item(0).getTextContent()));
            } catch (ParseException ex) {
                Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
            }
            task.setParentId(Integer.parseInt(section.getElementsByTagName("parent-id").item(0).getTextContent()));
            task.setProcessInstance(getProcessInstance(section.getElementsByTagName("process-instance-id").item(0).getTextContent()));
          
            if (task.getStatus().equals("Ready"))
          tasks.add(task);
         
        }
    return tasks;
    }
    
    @Override
    public  List<TaskSummary> getTasks(User user) {
        List<TaskSummary> tasks=new ArrayList<TaskSummary>();
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/task/query?taskOwner="+user.getLogin());
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nList = doc.getElementsByTagName("task-summary");
	for (int i=0; i<nList.getLength(); i++){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
            TaskSummary task=new TaskSummary();
            Element section = (Element) nList.item(i);   
            task.setStatus(section.getElementsByTagName("status").item(0).getTextContent());
            task.setTaskId(section.getElementsByTagName("id").item(0).getTextContent());
            task.setName(section.getElementsByTagName("name").item(0).getTextContent());
            task.setDescription(section.getElementsByTagName("description").item(0).getTextContent());
            try {
                task.setCreateOn(formatter.parse(section.getElementsByTagName("created-on").item(0).getTextContent()));
            } catch (ParseException ex) {
                Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
            }
            task.setActualOwner(section.getElementsByTagName("actual-owner").item(0).getTextContent());
            task.setProcessInstance(getProcessInstance(section.getElementsByTagName("process-instance-id").item(0).getTextContent()));
            task.setParentId(Integer.parseInt(section.getElementsByTagName("parent-id").item(0).getTextContent()));
     
          tasks.add(task);
        }
        return tasks;
}

    @Override
    public  List<ProcessInstance> getProcessInstances(User user) {
        List<ProcessInstance> processInstances=new ArrayList<ProcessInstance>();
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/history/instances");
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nList = doc.getElementsByTagName("process-instance-log");
	for (int i=0; i<nList.getLength(); i++){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
            ProcessInstance processInstance=new ProcessInstance();
            Element section = (Element) nList.item(i);   
            processInstance.setStatus(Integer.parseInt(section.getElementsByTagName("status").item(0).getTextContent()));
            processInstance.setId(section.getElementsByTagName("process-instance-id").item(0).getTextContent());
            processInstance.setProcessId(section.getElementsByTagName("process-id").item(0).getTextContent());
            processInstance.setDescription(section.getElementsByTagName("process-instance-description").item(0).getTextContent());
            try {
                processInstance.setStart(formatter.parse(section.getElementsByTagName("start").item(0).getTextContent()));
            } catch (ParseException ex) {
                Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
            }
            processInstance.setExtarnalId(section.getElementsByTagName("external-id").item(0).getTextContent());
            processInstance.setIdentity(section.getElementsByTagName("identity").item(0).getTextContent());
            processInstance.setParentId(Integer.parseInt(section.getElementsByTagName("parent-process-instance-id").item(0).getTextContent()));
            processInstance.setProcessName(section.getElementsByTagName("process-name").item(0).getTextContent());
     
          processInstances.add(processInstance);
        }
        return processInstances;
}
    
    @Override
    public  ProcessInstance getProcessInstance(String id) {
           List<ProcessInstance> processInstances=new ArrayList<ProcessInstance>();
           URL url;
           Document doc = null;
           try {
               url = new URL("http://localhost:8080/jbpm-console/rest/history/instances");

           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
           conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode("admin:admin"));
           String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
            doc =ParseXML(response);
           }
           catch (MalformedURLException ex) {
               Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
           } catch (Exception ex) {
               Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
           }
           NodeList nList = doc.getElementsByTagName("process-instance-log");
           for (int i=0; i<nList.getLength(); i++){
               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
               ProcessInstance processInstance=new ProcessInstance();
               Element section = (Element) nList.item(i);   
               processInstance.setStatus(Integer.parseInt(section.getElementsByTagName("status").item(0).getTextContent()));
               processInstance.setId(section.getElementsByTagName("process-instance-id").item(0).getTextContent());
               processInstance.setProcessId(section.getElementsByTagName("process-id").item(0).getTextContent());
               processInstance.setDescription(section.getElementsByTagName("process-instance-description").item(0).getTextContent());
               try {
                   processInstance.setStart(formatter.parse(section.getElementsByTagName("start").item(0).getTextContent()));
               } catch (ParseException ex) {
                   Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
               }
               processInstance.setExtarnalId(section.getElementsByTagName("external-id").item(0).getTextContent());
               processInstance.setIdentity(section.getElementsByTagName("identity").item(0).getTextContent());
               processInstance.setParentId(Integer.parseInt(section.getElementsByTagName("parent-process-instance-id").item(0).getTextContent()));
               processInstance.setProcessName(section.getElementsByTagName("process-name").item(0).getTextContent());

             processInstances.add(processInstance);
           }
           
        for (ProcessInstance pi: processInstances){
        if (pi.getId().equals(id))
        return pi;
         }
           return null;
   }

    @Override
    public  ProcessDefinition getProcessDefinition(String id) {
        List<ProcessDefinition> ProcessDefs=new ArrayList<ProcessDefinition>();
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/deployment/processes");
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode("admin:admin"));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nList = doc.getElementsByTagName("process-definition");
	for (int i=0; i<nList.getLength(); i++){
            ProcessDefinition ProcessDef=new ProcessDefinition();
            Element section = (Element) nList.item(i);   
            ProcessDef.setId(section.getElementsByTagName("id").item(0).getTextContent());
            ProcessDef.setName(section.getElementsByTagName("name").item(0).getTextContent());
            ProcessDef.setPackageName(section.getElementsByTagName("package-name").item(0).getTextContent());
            ProcessDef.setDeploymentId(section.getElementsByTagName("deployment-id").item(0).getTextContent());
            ProcessDef.setVersion(section.getElementsByTagName("version").item(0).getTextContent());
     
          ProcessDefs.add(ProcessDef);
        }
        for (ProcessDefinition pd: ProcessDefs){
        if (pd.getId().equals(id))
        return pd;
         }
        return null;
}
    
    @Override
    public  List<ProcessDefinition> getProcessDefinitions(User user) {
        List<ProcessDefinition> ProcessDefs=new ArrayList<ProcessDefinition>();
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/deployment/processes");
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nList = doc.getElementsByTagName("process-definition");
	for (int i=0; i<nList.getLength(); i++){
            ProcessDefinition ProcessDef=new ProcessDefinition();
            Element section = (Element) nList.item(i);   
            ProcessDef.setId(section.getElementsByTagName("id").item(0).getTextContent());
            ProcessDef.setName(section.getElementsByTagName("name").item(0).getTextContent());
            ProcessDef.setPackageName(section.getElementsByTagName("package-name").item(0).getTextContent());
            ProcessDef.setDeploymentId(section.getElementsByTagName("deployment-id").item(0).getTextContent());
            ProcessDef.setVersion(section.getElementsByTagName("version").item(0).getTextContent());
     
          ProcessDefs.add(ProcessDef);
        }
        return ProcessDefs;
}
            
    @Override
    public boolean claimTask(User user,String id) {
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/task/"+id+"/claim");
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    return true;
    }

    @Override
    public boolean startTask(User user,String id) {
        /*       Status         Expected Action
               -------------------------------
               Ready      --> Claim
               Reserved   --> Start
               InProgress --> Complete
         */      
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/task/"+id+"/start");
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
       return true;
    }
   
     @Override
    public boolean completeTask(User user,String id) {
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/task/"+id+"/complete");
        
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
       return true;
    }
   
    @Override
    public int startProcess(User user,ProcessDefinition processDef, String param) {
        URL url;
        Document doc = null;
        try {
          if (processDef.getId().equals("hiring"))
            url = new URL("http://localhost:8080/jbpm-console/rest/runtime/"+processDef.getDeploymentId()+"/process/"+processDef.getId()+"/start?map_name="+param);
           else
            url = new URL("http://localhost:8080/jbpm-console/rest/runtime/"+processDef.getDeploymentId()+"/process/"+processDef.getId()+"/start");
              
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
       try {   
            NodeList nList= doc.getElementsByTagName("process-instance-response");
            Element section = (Element) nList.item(0);   

           if ( section.getElementsByTagName("status").item(0).getTextContent().equals("SUCCESS"))
          {
              return 0;
        }
       }
       catch (Exception e)
       {
       System.out.println("Saipal:"+e);
       }
    return -1;
    }
   
     @Override
    public String getSvgResponse(ProcessInstance processInstance){
        URL url;
        Document doc = null;
        String response="";
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/runtime/"+processInstance.getExtarnalId()+"/process/"+processInstance.getProcessId()+"/image/"+processInstance.getId());
              
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode("admin:admin"));
	response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
    return response;
    }
    
    @Override
    public int abortProcess(User user,ProcessInstance processInstance) {
        URL url;
        Document doc = null;
        try {
            url = new URL("http://localhost:8080/jbpm-console/rest/runtime/"+processInstance.getExtarnalId()+"/process/instance/"+processInstance.getId()+"/abort");
 	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Basic " + Base64Util.encode(user.getLogin()+":"+user.getPassword()));
	String response = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
         doc =ParseXML(response);
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(REST.class.getName()).log(Level.SEVERE, null, ex);
        }
         NodeList nList= doc.getElementsByTagName("response");
	  Element section = (Element) nList.item(0);   
          
          if ( section.getElementsByTagName("status").item(0).getTextContent().equals("SUCCESS"))
          {
              return 0;
        }
        
    return -1;
    }
    
    
    
    
    
    public static Document ParseXML(String xml) throws Exception
{
  	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
	  return xmlDocument;
}
    public static String format(String xml) {
        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();
            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
            return writer.writeToString(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getXPathValue(String xml, String expression) throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		XPath xPath =  XPathFactory.newInstance().newXPath();
		return xPath.compile(expression).evaluate(xmlDocument);
    }
	
	/**
	 * <p>
	 * Encodes and decodes to and from Base64 notation.
	 * </p>
	 * <p>
	 * I am placing this code in the Public Domain. Do with it as you will. This software comes with no guarantees or warranties but
	 * with plenty of well-wishing instead! Please visit <a href="http://iharder.net/base64">http://iharder.net/base64</a>
	 * periodically to check for updates or to contribute improvements.
	 * </p>
	 *
	 * @author Robert Harder
	 * @author rob@iharder.net
	 * @version 2.3.7
	 */
	private static class Base64Util {

	    /** The equals sign (=) as a byte. */
	    private final static byte EQUALS_SIGN = (byte) '=';

	    /** Preferred encoding. */
	    private final static String PREFERRED_ENCODING = "US-ASCII";

	    /** The 64 valid Base64 values. */
	    private final static byte[] _STANDARD_ALPHABET = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
	            (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O',
	            (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X',
	            (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g',
	            (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
	            (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y',
	            (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
	            (byte) '8', (byte) '9', (byte) '+', (byte) '/' };

	    /** Defeats instantiation. */
	    private Base64Util() {
	    }

	    /**
	     * <p>
	     * Encodes up to three bytes of the array <var>source</var> and writes the resulting four Base64 bytes to
	     * <var>destination</var>. The source and destination arrays can be manipulated anywhere along their length by specifying
	     * <var>srcOffset</var> and <var>destOffset</var>. This method does not check to make sure your arrays are large enough to
	     * accomodate <var>srcOffset</var> + 3 for the <var>source</var> array or <var>destOffset</var> + 4 for the
	     * <var>destination</var> array. The actual number of significant bytes in your array is given by <var>numSigBytes</var>.
	     * </p>
	     * <p>
	     * This is the lowest level of the encoding methods with all possible parameters.
	     * </p>
	     *
	     * @param source the array to convert
	     * @param srcOffset the index where conversion begins
	     * @param numSigBytes the number of significant bytes in your array
	     * @param destination the array to hold the conversion
	     * @param destOffset the index where output will be put
	     * @return the <var>destination</var> array
	     * @since 1.3
	     */
	    private static byte[] encode3to4( byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset ) {

	        byte[] ALPHABET = _STANDARD_ALPHABET;

	        int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
	                | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
	                | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

	        switch ( numSigBytes ) {
	        case 3:
	            destination[destOffset] = ALPHABET[(inBuff >>> 18)];
	            destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
	            destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
	            destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
	            return destination;

	        case 2:
	            destination[destOffset] = ALPHABET[(inBuff >>> 18)];
	            destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
	            destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
	            destination[destOffset + 3] = EQUALS_SIGN;
	            return destination;

	        case 1:
	            destination[destOffset] = ALPHABET[(inBuff >>> 18)];
	            destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
	            destination[destOffset + 2] = EQUALS_SIGN;
	            destination[destOffset + 3] = EQUALS_SIGN;
	            return destination;

	        default:
	            return destination;
	        }
	    }

	    /**
	     * Encode string as a byte array in Base64 annotation.
	     *
	     * @param string
	     * @return The Base64-encoded data as a string
	     */
	    public static String encode( String string ) {
	        byte[] bytes;
	        try {
	            bytes = string.getBytes(PREFERRED_ENCODING);
	        } catch( UnsupportedEncodingException uee ) {
	            bytes = string.getBytes();
	        }
	        return encodeBytes(bytes);
	    }

	    /**
	     * Encodes a byte array into Base64 notation.
	     *
	     * @param source The data to convert
	     * @return The Base64-encoded data as a String
	     * @throws NullPointerException if source array is null
	     * @throws IllegalArgumentException if source array, offset, or length are invalid
	     * @since 2.0
	     */
	    public static String encodeBytes( byte[] source ) {
	        return encodeBytes(source, 0, source.length);
	    }

	    /**
	     * Encodes a byte array into Base64 notation.
	     *
	     * @param source The data to convert
	     * @param off Offset in array where conversion should begin
	     * @param len Length of data to convert
	     * @return The Base64-encoded data as a String
	     * @throws NullPointerException if source array is null
	     * @throws IllegalArgumentException if source array, offset, or length are invalid
	     * @since 2.0
	     */
	    public static String encodeBytes( byte[] source, int off, int len ) {
	        byte[] encoded = encodeBytesToBytes(source, off, len);
	        try {
	            return new String(encoded, PREFERRED_ENCODING);
	        } catch( UnsupportedEncodingException uee ) {
	            return new String(encoded);
	        }
	    }

	    /**
	     * Similar to {@link #encodeBytes(byte[], int, int)} but returns a byte
	     * array instead of instantiating a String. This is more efficient if you're
	     * working with I/O streams and have large data sets to encode.
	     *
	     *
	     * @param source The data to convert
	     * @param off Offset in array where conversion should begin
	     * @param len Length of data to convert
	     * @return The Base64-encoded data as a String if there is an error
	     * @throws NullPointerException if source array is null
	     * @throws IllegalArgumentException if source array, offset, or length are invalid
	     * @since 2.3.1
	     */
	    public static byte[] encodeBytesToBytes( byte[] source, int off, int len ) {

	        if( source == null )
	            throw new NullPointerException("Cannot serialize a null array.");

	        if( off < 0 )
	            throw new IllegalArgumentException("Cannot have negative offset: " + off);

	        if( len < 0 )
	            throw new IllegalArgumentException("Cannot have length offset: " + len);

	        if( off + len > source.length )
	            throw new IllegalArgumentException(String.format(
	                    "Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));

	        // Bytes needed for actual encoding
	        int encLen = (len / 3) * 4 + (len % 3 > 0 ? 4 : 0);

	        byte[] outBuff = new byte[encLen];

	        int d = 0;
	        int e = 0;
	        int len2 = len - 2;
	        for( ; d < len2; d += 3, e += 4 )
	            encode3to4(source, d + off, 3, outBuff, e);

	        if( d < len ) {
	            encode3to4(source, d + off, len - d, outBuff, e);
	            e += 4;
	        }

	        if( e <= outBuff.length - 1 ) {
	            byte[] finalOut = new byte[e];
	            System.arraycopy(outBuff, 0, finalOut, 0, e);
	            return finalOut;
	        } else
	            return outBuff;
	    }
	}

    @Override    
    public void send(String to,String sub,String msg){  
              //Get properties object    
              Properties props = new Properties();    
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
             //get Session   
            Session ses = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("iitu.bpm", "Diploma322");
                }
            });    
              //compose message    
              try {    
               MimeMessage message = new MimeMessage(ses);    
               message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
               message.setSubject(sub);    
               message.setText(msg);    
               //send message  
               Transport.send(message);    
               System.out.println("message sent successfully");    
              } catch (MessagingException e) {throw new RuntimeException(e);}    

        }

}
