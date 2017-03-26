/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package adl;

import java.sql.Timestamp;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import constants.MyIntent;
import constants.MyResponse;
import data.ActionBean;
import data.ActionMapper;
import data.SqlSessionHelper;

/**
 * This sample shows how to create a simple speechlet for handling speechlet requests.
 */
public class RecordActivitySpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(RecordActivityServlet.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getAskResponse(MyResponse.Welcome, MyIntent.Welcome);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (MyIntent.SingSong.equals(intentName)) {
        	writeAction(intentName);
            return getAskResponse(MyResponse.SingSong, MyIntent.SingSong);
        } else if(MyIntent.TurnOnTV.equals(intentName)){
        	writeAction(intentName);
        	return getAskResponse(MyResponse.TurnOnTV, MyIntent.TurnOnTV);
        } else if(MyIntent.TakeShower.equals(intentName)){
        	writeAction(intentName);
        	return getAskResponse(MyResponse.TakeShower, MyIntent.TakeShower);
        } else if(MyIntent.GoGym.equals(intentName)){
        	writeAction(intentName);
        	return getAskResponse(MyResponse.GoGym, MyIntent.GoGym);
        } else if(MyIntent.HaveLunch.equals(intentName)){
        	writeAction(intentName);
        	return getAskResponse(MyResponse.HaveLunch, MyIntent.HaveLunch);
        } else if (MyIntent.Help.equals(intentName)) {
        	return getAskResponse(MyResponse.Help, MyIntent.Help);
        } else if (MyIntent.Cancel.equals(intentName) || MyIntent.Stop.equals(intentName)) {
            return getTellResponse(MyResponse.Exit, MyIntent.Exit);
        } else {
            throw new SpeechletException("Invalid Intent");
        }
        
        
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getAskResponse(String speechText, String title) {

    	String promptText = MyResponse.AskNext;
    	speechText += promptText;
    	
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getTellResponse(String speechText, String title) {

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
    
    //将用户输入的动作存入数据库
    public void writeAction(String intent) {
    	
    	SqlSession session = null;
		try {
			session = SqlSessionHelper.getSessionFactory().openSession();
			ActionMapper mapper = session.getMapper(ActionMapper.class);
			
			ActionBean actionBean = new ActionBean();
			actionBean.setName(camelToUtterance(intent));//将驼峰字符串转成小写+空格形式的的日常表达话语
			actionBean.setTime(new Timestamp(System.currentTimeMillis()));
			mapper.addActionBean(actionBean);

			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
    }
    
	 /**
     * 将驼峰字符串转成小写+空格形式的的日常表达话语
     */
    public static String camelToUtterance(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(' ');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }
}
