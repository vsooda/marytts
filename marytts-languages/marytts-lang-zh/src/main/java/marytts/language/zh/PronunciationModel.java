package marytts.language.zh;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

import marytts.cart.StringPredictionTree;
import marytts.datatypes.MaryData;
import marytts.datatypes.MaryXML;
import marytts.modules.phonemiser.AllophoneSet;
import marytts.unitselection.select.Target;
import marytts.util.MaryRuntimeUtils;
import marytts.util.dom.MaryDomUtils;
import marytts.util.dom.NameNodeFilter;

public class PronunciationModel extends marytts.modules.PronunciationModel {
	
	public PronunciationModel() {
		super(Locale.CHINESE);
	}
	
	/**
	 * Go through all tokens in a document, and copy any accents to the first accented syllable.
	 */
	void processSyllableTone(Document doc) {
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
						//syl.setAttribute("accent", t.getAttribute("accent"));
						syl.setAttribute("accent", "L*+!H");
						assignedAccent = true;
						break; // done for this token
					}
				}
				if (!assignedAccent) {
					// Hmm, this token does not have a stressed syllable --
					// take the first syllable then:
					syl = MaryDomUtils.getFirstElementByTagName(t, MaryXML.SYLLABLE);
					if (syl != null) {
						//syl.setAttribute("accent", t.getAttribute("accent"));
						syl.setAttribute("accent", "!H*");
					}
				}
			}
		}
	}

	
	public MaryData process(MaryData d) throws Exception {
		MaryData result = super.process(d);
		System.out.println("对每个单词的准确读音进行标注");
		Document doc = result.getDocument();
		processSyllableTone(doc);
		System.out.println("process syslable tone ok in pronunciationModel");
		result.setDocument(doc);
		return result;
	}
}
