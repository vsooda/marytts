package marytts.language.zh;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.traversal.NodeIterator;

import marytts.datatypes.MaryData;
import marytts.datatypes.MaryDataType;
import marytts.datatypes.MaryXML;
import marytts.exceptions.MaryConfigurationException;
import marytts.server.MaryProperties;
import marytts.util.dom.MaryDomUtils;

import marytts.util.pinyin.ConvertZh2Pinyin;

/**
 * The phonemiser module -- java implementation.
 * change the input text to pinyin for phone.. but the text remain   zh.
 *
 * @author sooda
 */

public class JPhonemiser  extends marytts.modules.JPhonemiser {
	
	public JPhonemiser() throws IOException, MaryConfigurationException {
		super("JPhonemiser_zh", MaryDataType.PARTSOFSPEECH, MaryDataType.PHONEMES, "zh.allophoneset", "de.userdict",
				"zh.lexicon", "zh.lettertosound");
	}
	
	
	public boolean maybePronounceable(String text) {
		// does text contain anything at all?
		if (text == null || text.isEmpty()) {
			return false;
		}

		// does text contain at least one word character?
		if (text.matches(".*\\w.*")) {
			return true;
		}

		// does POS tag indicate punctuation?
		if (isPosPunctuation(text)) {
			return false;
		}
		
		return true;
	}

	
	public MaryData process(MaryData d) throws Exception {
		Document doc = d.getDocument();
		boolean useNewPhoneset = true;

		NodeIterator it = MaryDomUtils.createNodeIterator(doc, doc, MaryXML.TOKEN);
		Element t = null;
		while ((t = (Element) it.nextNode()) != null) {
			String text;

			// Do not touch tokens for which a transcription is already
			// given (exception: transcription contains a '*' character:
			if (t.hasAttribute("ph") && !t.getAttribute("ph").contains("*")) {
				continue;
			}
			if (t.hasAttribute("sounds_like"))
				text = t.getAttribute("sounds_like");
			else
				text = MaryDomUtils.tokenText(t);

			System.out.println("in phonemisze_zh: " + text);
			text = ConvertZh2Pinyin.convert2Pinyin(text);
			// use part-of-speech if available
			String pos = null;
			if (t.hasAttribute("pos")) {
				pos = t.getAttribute("pos");
			}

			//if (maybePronounceable(text, pos)) {
			if (maybePronounceable(text)) {
				// If text consists of several parts (e.g., because that was
				// inserted into the sounds_like attribute), each part
				// is transcribed separately.
				StringBuilder ph = new StringBuilder();
				StringBuilder tones = new StringBuilder();
				String g2pMethod = null;
				StringTokenizer st = new StringTokenizer(text, " -"); //切分
				while (st.hasMoreTokens()) {
					String graph = st.nextToken();
					StringBuilder helper = new StringBuilder();
					String phon = phonemise(graph, pos, helper);		
					
					// null result should not be processed
					if (phon == null) {
						continue;
					}
					if (ph.length() == 0) { // first part
						// The g2pMethod of the combined beast is
						// the g2pMethod of the first constituant.
						g2pMethod = helper.toString();
						ph.append(phon);
						if (Character.isDigit(graph.charAt(graph.length()-1))) {
							tones.append(graph.charAt(graph.length()-1));
						}
					} else { // following parts
						ph.append(" - "); //这个导致trichphone的产生
						// Reduce primary to secondary stress:
						ph.append(phon.replace('\'', ','));
						tones.append(" - ");
						if (Character.isDigit(graph.charAt(graph.length()-1))) {
							tones.append(graph.charAt(graph.length()-1));
						}
					}
				}

				if (ph != null && ph.length() > 0) {
					setPh(t, ph.toString());
					t.setAttribute("g2p_method", g2pMethod);
					t.setAttribute("toneseq", tones.toString());
				}
			}
		}
		MaryData result = new MaryData(outputType(), d.getLocale());
		result.setDocument(doc);
		return result;
	}
	
}
