package adl;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * 接收alexa语音服务入口
 */
public class RecordActivityServlet extends SpeechletServlet {
	
	
	public RecordActivityServlet() {
	    this.setSpeechlet(new RecordActivitySpeechlet());
	}
	
	
	
}
