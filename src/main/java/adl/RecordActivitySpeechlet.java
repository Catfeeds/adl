/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package adl;

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
        return getAskResponse("Welcome to Activities of Daily Life, you can say activities in your daily life such as I go to the gym, I sing a song, I take a shower and so on.", "Welcome Response");
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (Activity.SingSongIntent.equals(intentName)) {
            return getAskResponse("You sing well. Singing is a good way to relax and make you feel confident.", "SingSong Response");
        } else if(Activity.TurnOnTVIntent.equals(intentName)){
        	return getAskResponse("Enjoy your TV time. Watching TV is a good way to lift your mood and dissolve the depression", "TurnOnTV Response");
        } else if(Activity.TakeShowerIntent.equals(intentName)){
        	return getAskResponse("Great! You are living a healthy lifestyle. Enjoy your bathtime!", "TakeShower Response");
        } else if(Activity.GoGymIntent.equals(intentName)){
        	return getAskResponse("Excellent! Wish you will be stronger the next time!", "GoGym Response");
        } else if(Activity.HaveLunchIntent.equals(intentName)){
        	return getAskResponse("Keep going! You will be healthy if you keep a balanced diet every day!", "HaveLunch Response");
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getAskResponse("This skill is a recorder of activities in your daily life and feed back some interactions with your activities. You can say activities like I go to the gym, I sing a song, I take a shower, I turn on the TV and so on.", "Help Response");
        } else if ("AMAZON.CancelIntent".equals(intentName) || "AMAZON.StopIntent".equals(intentName)) {
            return getTellResponse("Goodbye. See you next time", "Exit Response");
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

    	String promptText = "And what's your next activity?";
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
}
