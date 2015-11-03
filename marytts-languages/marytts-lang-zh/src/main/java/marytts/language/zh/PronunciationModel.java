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

				Element syl = null;
				while ((syl = (Element) sylIt.nextNode()) != null) {
					String phones = syl.getAttribute("ph");
					String phoneArray[] = phones.split(" ");
					String firstPhone = phoneArray[1].trim();
					String secondPhone = phoneArray[2].trim();
					if (phoneArray.length == 3) {
						if (firstPhone.endsWith("M")) {
							syl.setAttribute("accent", "L*");
							System.out.println(phones + "=> 5");
						} else if (firstPhone.endsWith("L")) {
							if (secondPhone.endsWith("L")) {
								syl.setAttribute("accent", "L*");
								System.out.println(phones + "=> 3");
							} else {
								syl.setAttribute("accent", "L*+H");
								System.out.println(phones + "=> 2");
							}
						} else if (firstPhone.endsWith("H")) {
							if (secondPhone.endsWith("L")) {
								syl.setAttribute("accent", "!H*");
								System.out.println(phones + "=> 4");
							} else {
								syl.setAttribute("accent", "H*");
								System.out.println(phones + "=> 1");
							}
						}
						
					}
					System.out.println("syysla.." + phones);
					
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
