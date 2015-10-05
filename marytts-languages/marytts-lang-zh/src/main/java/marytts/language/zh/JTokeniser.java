package marytts.language.zh;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

import de.dfki.lt.tools.tokenizer.JTok;
import de.dfki.lt.tools.tokenizer.annotate.AnnotatedString;
import de.dfki.lt.tools.tokenizer.annotate.FastAnnotatedString;
import marytts.datatypes.MaryData;
import marytts.datatypes.MaryDataType;
import marytts.datatypes.MaryXML;
import marytts.util.MaryUtils;
import marytts.util.dom.DomUtils;
import marytts.util.dom.MaryDomUtils;
import marytts.util.dom.NameNodeFilter;

/**
 * The phonemiser module -- java implementation.
 * change the input text to pinyin for phone.. but the text remain   zh.
 *
 * @author sooda
 */

public class JTokeniser  extends marytts.modules.JTokeniser {
	private JiebaSegmenter segmenter ;
	public JTokeniser() {
		super(MaryDataType.RAWMARYXML, MaryDataType.TOKENS, Locale.CHINESE);
		segmenter = new JiebaSegmenter();
	}
	
	public MaryData process(MaryData d) throws Exception {
		MaryData result = super.process(d);
		//segment(result);
		System.out.println("zh_token");
		return result;
	}
	
	protected void segment(MaryData d) {
		Document doc = d.getDocument();
		NodeIterator ni = ((DocumentTraversal) doc).createNodeIterator(doc, NodeFilter.SHOW_ELEMENT, new NameNodeFilter(
				MaryXML.TOKEN), false);
		Element t = null;
		while ((t = (Element) ni.nextNode()) != null) {
			String words = MaryDomUtils.tokenText(t);
			//segment it..
			// Insert the new token element
			List<SegToken> tokens = segmenter.process(words, SegMode.INDEX);
	        for (SegToken token : tokens) {
	        	//System.out.println(token.word);
	        	Element tnew = MaryXML.createElement(doc, MaryXML.TOKEN);
				MaryDomUtils.setTokenText(tnew, token.word);
				t.getParentNode().insertBefore(tnew, t);
	        }

			t.getParentNode().removeChild(t);
		}
	}
	
}
