package adl;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * ����alexa�����������
 */
public class RecordActivityServlet extends SpeechletServlet {
	
	
	public RecordActivityServlet() {
	    this.setSpeechlet(new RecordActivitySpeechlet());
	}
	
	
	
}
