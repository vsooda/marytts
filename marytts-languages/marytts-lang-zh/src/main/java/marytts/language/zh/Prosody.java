package marytts.language.zh;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import marytts.datatypes.MaryData;
import marytts.datatypes.MaryDataType;
import marytts.datatypes.MaryXML;
import marytts.modules.ProsodyGeneric;
import marytts.server.MaryProperties;
import marytts.util.dom.MaryDomUtils;
import marytts.util.dom.NameNodeFilter;

public class Prosody extends ProsodyGeneric {
	public Prosody() {
		super(MaryDataType.PHONEMES, MaryDataType.INTONATION, Locale.CHINESE, MaryProperties.localePrefix(Locale.CHINESE)
				+ ".prosody.tobipredparams", MaryProperties.localePrefix(Locale.CHINESE) + ".prosody.accentPriorities",
				MaryProperties.localePrefix(Locale.CHINESE) + ".prosody.syllableaccents", MaryProperties
						.localePrefix(Locale.CHINESE) + ".prosody.paragraphdeclination");
		System.out.println("in chinese prosody init..");
	}
	
//	public MaryData process(MaryData d) throws Exception {
//		MaryData result = super.process(d);
//		System.out.println("in zh_Prosody process.." );
//		Document doc = result.getDocument();
//		processSyllablesTone(doc);
//		System.out.println("process syslable tone ok");
//		result.setDocument(doc);
//		return result;
//	}
	
	/**
	 * Go through all tokens in a document, and copy any accents to the first accented syllable.
	 */
	void processSyllablesTone(Document doc) {
		NodeIterator tIt = ((DocumentTraversal) doc).createNodeIterator(doc, NodeFilter.SHOW_ELEMENT, new NameNodeFilter(
				MaryXML.TOKEN), false);
		Element t = null;
		while ((t = (Element) tIt.nextNode()) != null) {
			if (t.hasAttribute("accent")) {
				NodeIterator sylIt = ((DocumentTraversal) doc).createNodeIterator(t, NodeFilter.SHOW_ELEMENT, new NameNodeFilter(
						MaryXML.SYLLABLE), false);
				boolean assignedAccent = false;
				Element syl = null;
				while ((syl = (Element) sylIt.nextNode()) != null) {
					System.out.println("syysla.." + syl.getAttribute("ph"));
					if (syl.getAttribute("stress").equals("1")) {
						// found
						syl.setAttribute("accent", t.getAttribute("accent"));
						System.out.println("setting accent  1");
						assignedAccent = true;
						break; // done for this token
					}
				}
				if (!assignedAccent) {
					// Hmm, this token does not have a stressed syllable --
					// take the first syllable then:
					syl = MaryDomUtils.getFirstElementByTagName(t, MaryXML.SYLLABLE);
					if (syl != null) {
						syl.setAttribute("accent", t.getAttribute("accent"));
						System.out.println("setting accent 2");
					}
				}
			}
		}
	}
}
