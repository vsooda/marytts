package marytts.language.zh;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import marytts.datatypes.MaryData;
import marytts.datatypes.MaryDataType;
import marytts.datatypes.MaryXML;
import marytts.exceptions.MaryConfigurationException;
import marytts.modules.InternalModule;
import marytts.util.MaryRuntimeUtils;
import marytts.util.dom.MaryDomUtils;
import marytts.util.dom.NameNodeFilter;


/**
 * The Preprocess module -- java implementation.
 * for word segment, year rule, number rule..
 *
 * @author sooda
 */

public class Preprocess  extends InternalModule {
	public Preprocess() {
		super("Preprocess", MaryDataType.TOKENS, MaryDataType.WORDS, Locale.CHINESE);
	}

	public MaryData process(MaryData d) throws Exception {
		Document doc = d.getDocument();
		expand(doc);
		MaryData result = new MaryData(getOutputType(), d.getLocale());
		result.setDocument(doc);
		return result;
	}
	
	/***
	 * processes a document in mary xml format, from Tokens to Words which can be phonemised.
	 * @param doc
	 * @throws ParseException
	 * @throws IOException
	 * @throws MaryConfigurationException
	 */
	protected void expand(Document doc) throws ParseException, IOException, MaryConfigurationException {
		
	}
	
}
