package com.websocket.server;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
 
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;



 
@ServerEndpoint("/websocket")
public class WebSocketForPad {
    private static int onlineCount = 0;
     
    private static CopyOnWriteArraySet<WebSocketForPad> webSocketSet = new CopyOnWriteArraySet<WebSocketForPad>();
     
    private Session session;
    
    private String id;
    
    @OnOpen
    public void onOpen(Session session){
    	addOnlineCount();           
  
    	
        this.session = session;
        webSocketSet.add(this);     
    }
     
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  
        subOnlineCount();
    }
     
    @OnMessage
    public void onMessage(String message, Session session) {
        
        for(WebSocketForPad item: webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
    
    
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("Conntion Error!");
        error.printStackTrace();
    }
     

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

//    public static void sendMessageTo(String message,String toList) {
//      for(WebSocketForPad item: webSocketSet) {
//    	  if(!toList.contains(item.getId()))
//      		continue;
//          try {
//              item.sendMessage(message);
//          } catch (IOException e) {
//              e.printStackTrace();
//              continue;
//          }
//      }
//    }
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
 
    public static synchronized void addOnlineCount() {
        WebSocketForPad.onlineCount++;
    }
     
    public static synchronized void subOnlineCount() {
        WebSocketForPad.onlineCount--;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
}
