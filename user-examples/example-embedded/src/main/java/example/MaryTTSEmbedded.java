package example;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;


public class MaryTTSEmbedded {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("testing embeding");
		MaryInterface marytts = new LocalMaryInterface();
		Set<String> voices = marytts.getAvailableVoices();
		marytts.setVoice(voices.iterator().next());
		AudioInputStream audio = marytts.generateAudio("you are bad boy.");
		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		player.join();
	}

}
