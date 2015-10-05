package marytts.language.zh;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

import marytts.datatypes.MaryData;
import marytts.datatypes.MaryDataType;
import marytts.datatypes.MaryXML;
import marytts.fst.FSTLookup;
import marytts.modules.InternalModule;
import marytts.server.MaryProperties;
import marytts.util.MaryUtils;
import marytts.util.dom.MaryDomUtils;

public class PosTagger extends InternalModule {

	/**
	 * Constructor which can be directly called from init info in the config file. Different languages can call this code with
	 * different settings.
	 * 
	 * @param locale
	 *            a locale string, e.g. "en"
	 * 
	 * @throws Exception
	 */
	public PosTagger(String locale) throws Exception {
		super("PosTagger_zh", MaryDataType.WORDS, MaryDataType.PARTSOFSPEECH, MaryUtils.string2locale(locale));
	}

	public void startup() throws Exception {
		super.startup();
	}

	public MaryData process(MaryData d) throws Exception {
		System.out.println("nothing_to_do in PosTagger");
		return d;
	}

}