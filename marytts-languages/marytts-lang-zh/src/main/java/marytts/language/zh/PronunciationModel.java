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
					String firstPhone ;
					String secondPhone;
					if (phoneArray.length == 3) {
						firstPhone = phoneArray[1].trim();
						secondPhone = phoneArray[2].trim();
						
					} else if (phoneArray.length == 2) {
						firstPhone = phoneArray[0].trim();
						secondPhone = phoneArray[1].trim();
					} else {
						continue;
					}
					System.out.println("ph " + phones + " " + phoneArray.length + " " +  firstPhone + " " + secondPhone);
					
					if (firstPhone.endsWith("M")) {
						syl.setAttribute("accent", "L*");
						syl.setAttribute("zhtone", "5");
						System.out.println(phones + "=> 5");
					} else if (firstPhone.endsWith("L")) {
						if (secondPhone.endsWith("L")) {
							syl.setAttribute("accent", "L*");
							syl.setAttribute("zhtone", "3");
							System.out.println(phones + "=> 3");
						} else {
							syl.setAttribute("accent", "L*+H");
							syl.setAttribute("zhtone", "2");
							System.out.println(phones + "=> 2");
						}
					} else if (firstPhone.endsWith("H")) {
						if (secondPhone.endsWith("L")) {
							syl.setAttribute("accent", "!H*");
							syl.setAttribute("stress", "2");
							syl.setAttribute("zhtone", "4");
							System.out.println(phones + "=> 4");
						} else {
							syl.setAttribute("accent", "H*");
							syl.setAttribute("stress", "1");
							syl.setAttribute("zhtone", "1");
							System.out.println(phones + "=> 1");
						}
					}
					System.out.println("syysla.." + phones);	
				}
			}
		}
	}
	
	void processSyllableTonePhonesetPY(Document doc) {
		NodeIterator tIt = ((DocumentTraversal) doc).createNodeIterator(doc, NodeFilter.SHOW_ELEMENT, new NameNodeFilter(
				MaryXML.TOKEN), false);
		Element t = null;
		while ((t = (Element) tIt.nextNode()) != null) {
			if (t.hasAttribute("accent")) {
				NodeIterator sylIt = ((DocumentTraversal) doc).createNodeIterator(t, NodeFilter.SHOW_ELEMENT, new NameNodeFilter(
						MaryXML.SYLLABLE), false);

				Element syl = null;
				String tonesStr = t.getAttribute("toneseq");
				if (tonesStr.isEmpty()) {
					continue;
				}
				int index = 0;
				String [] tones = tonesStr.split("-");

				while ((syl = (Element) sylIt.nextNode()) != null) {
					String phones = syl.getAttribute("ph");
					int toneValue = Integer.parseInt(tones[index].trim());
					if (toneValue == 5) {
						//syl.setAttribute("accent", "L*");
						syl.setAttribute("zhtone", "5");
						System.out.println(phones + "=> 5");
					} else if (toneValue == 3) {
						//syl.setAttribute("accent", "L*");
						syl.setAttribute("zhtone", "3");
						System.out.println(phones + "=> 3");
					} else if (toneValue == 2){
						//syl.setAttribute("accent", "L*+H");
						syl.setAttribute("zhtone", "2");
						System.out.println(phones + "=> 2");
					} else if (toneValue == 4) {
						//syl.setAttribute("accent", "!H*");
						syl.setAttribute("stress", "2");
						syl.setAttribute("zhtone", "4");
						System.out.println(phones + "=> 4");
					} else if (toneValue == 1){
						//syl.setAttribute("accent", "H*");
						syl.setAttribute("stress", "1");
						syl.setAttribute("zhtone", "1");
						System.out.println(phones + "=> 1");
					}
					syl.setAttribute("accent", t.getAttribute("accent"));
					index++;
					System.out.println("syysla.." + phones);	
				}
			}
		}
	}

	
	public MaryData process(MaryData d) throws Exception {
		MaryData result = super.process(d);
		System.out.println("对每个单词的准确读音进行标注");
		Document doc = result.getDocument();
		boolean useNewPhoneset = true;
		if (useNewPhoneset) {
			processSyllableTonePhonesetPY(doc);
		} else {
			processSyllableTone(doc);
		}
		System.out.println("process syslable tone ok in pronunciationModel");
		result.setDocument(doc);
		return result;
	}
}
