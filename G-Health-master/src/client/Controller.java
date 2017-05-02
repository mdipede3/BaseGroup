package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import enums.DoctorSpeciallity;
import enums.task;
import models.*;

/**
 * 
 * Taking care of all connection and transportation in client side
 * including file management
 * @author G5 lab group
 */
public class Controller {
	 	private static Socket socket = null;
	    private static ObjectInputStream inputStream = null;
	    private static ObjectOutputStream outputStream = null;
	    private static boolean isConnected = false;
	    private static Envelope En = null;
	    private static Envelope GetEn = null;
	    
	    
	    /**
	     * Encapsulate in Envelope struct type
	     * @param obj
	     * @param ts
	     * @return
	     */
	    public static Envelope Control(Object obj,task ts)
	    {
	    	Envelope En = new Envelope();
	    	
	    	if(obj instanceof List<?>)
	    	{
	    		/* This case is for sending list and not object. */
	    		List<Object> objList = (List<Object>) obj;
	    		En.setobjList(objList);
	    	}
	    	else En.addobjList(obj);
	        En.setType(ts);
	        En = communicate(En);
	    	return En;
	    }

	   /**
	    * Sending the envelope
	    * @param En
	    * @return
	    */
	public static Envelope communicate(Envelope En) {
	    	
	    	String ip = "127.0.0.1";
	    	

	        while (!isConnected) { //loop not used, for future purposes
	            try {
	            	
	            	/* Connection details + socket creation */
	            	socket = new Socket(ip,5555);
	          
	                System.out.println("Client->Controller: Socket created");
	                
	                /* Output stream creation and related object sending */
	                outputStream = new ObjectOutputStream(socket.getOutputStream());
	               
	                
	                
	                System.out.println("Object to be written = " + 	En);
	                
	                
	                /* Object sending */
	                outputStream.writeObject(En);
	                isConnected = true;
	                

	                
	            } catch (SocketException se) {
	                se.printStackTrace();
	                // System.exit(0);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	            /* Receiving response from server */
	            try {
	            	
	                /* Choose if get file or object */
	                if(En.getType() == task.SEND_FILE_TO_CLIENT)
	                {
	                	LabSettings ls = (LabSettings)En.getSingleObject();
	                	saveFile(ls.getFileExt());
	                }
	                else if(En.getType() == task.UPLOAD_FILE_TO_LAB_RECORD)
	                	sendFile(((LabSettings)En.getSingleObject()).getFilePath());
	                	//sendFile("src//client//files//afasdf.jpg");
	                else
	                {
		                inputStream = new ObjectInputStream(socket.getInputStream());
		                try {
		                	
		                	GetEn  = (Envelope) inputStream.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	     
	                }
	                
	         
	                System.out.println("Client: Object received = " + GetEn);
	            
	                /* Flushing and closing stream */
	                outputStream.flush();
	                outputStream.close();
	                
	            } catch (SocketException se) {
	                se.printStackTrace();
	                // System.exit(0);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }//end while
	        
	        isConnected = false; 
	        return GetEn;
	        
	        
	    }//end function

	    
	    /**
	     * Sending the file
	     * @param filename
	     * @throws IOException
	     */
	    public static void sendFile(String filename) throws IOException 
	    {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			FileInputStream fis = new FileInputStream(filename);
			byte[] buffer = new byte[16*1024]; //16 kb buffer

			int filesize = 2097152; // Send file up to 2 mb size in separate msg
			int read = 0;
			int totalRead = 0;
			int remaining = filesize;
			while((read = fis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
				totalRead += read;
				remaining -= read;
				System.out.println("read " + totalRead + " bytes.");
				dos.write(buffer, 0, read);
			}
	        
	        /*
			while (fis.read(buffer) > 0) {
				dos.write(buffer);
			}
			*/
			
			
			fis.close(); //Git Test
			dos.close();	
			
			
			
		}
		
		
		/**
		 * Saving file in client storage
		 * @throws IOException
		 */
		private static void saveFile(String ext) throws IOException {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			FileOutputStream fos = new FileOutputStream("src//images//lab_file."+ext);
			byte[] buffer = new byte[16*1024]; // 16 kb buffer
			
			int filesize = 2097152; // Send file up to 2mb size in separate msg
			int read = 0;
			int totalRead = 0;
			int remaining = filesize;
			while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
				totalRead += read;
				remaining -= read;
				System.out.println("read " + totalRead + " bytes.");
				fos.write(buffer, 0, read);
			}
			
			fos.close();
			dis.close();
		} 
	    
	    
}
