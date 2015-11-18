/**
 * chinese feature manager -- java implementation.
 * add some new feature and question set for chinese tts
 * copy from marytts.language.de.features;
 * @author sooda
 */
package marytts.language.zh.features;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import marytts.exceptions.MaryConfigurationException;
import marytts.features.MaryFeatureProcessor;
import marytts.features.MaryGenericFeatureProcessors;
import marytts.features.MaryLanguageFeatureProcessors;
import marytts.modules.phonemiser.AllophoneSet;
import marytts.modules.synthesis.Voice;
import marytts.server.MaryProperties;
import marytts.util.MaryRuntimeUtils;

public class FeatureProcessorManager extends marytts.features.FeatureProcessorManager {

	/**
	 * Builds a new manager. This manager uses the english phoneset of FreeTTS and a PoS conversion file if the english PoS tagger
	 * is used. All feature processors loaded are language specific.
	 * @throws MaryConfigurationException 
	 */
	public FeatureProcessorManager() throws MaryConfigurationException {
		super(Locale.CHINESE);
		setupAdditionalFeatureProcessors();
		System.out.println("zh feature manageer constructor 1");
	}

	/**
	 * Constructor called from a Voice in Locale zh that has its own acoustic models
	 * 
	 * @param voice
	 */
	public FeatureProcessorManager(Voice voice) throws MaryConfigurationException {
		super(voice.getLocale());
		setupAdditionalFeatureProcessors();
		registerAcousticModels(voice);
		System.out.println("zh feature manageer constructor 2");
	}

	private void setupAdditionalFeatureProcessors() {
		try {
			MaryGenericFeatureProcessors.TargetElementNavigator segment = new MaryGenericFeatureProcessors.SegmentNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator prevSegment = new MaryGenericFeatureProcessors.PrevSegmentNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator nextSegment = new MaryGenericFeatureProcessors.NextSegmentNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator syllable = new MaryGenericFeatureProcessors.SyllableNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator prevSyllable = new MaryGenericFeatureProcessors.PrevSyllableNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator nextSyllable = new MaryGenericFeatureProcessors.NextSyllableNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator nextNextSyllable = new MaryGenericFeatureProcessors.NextNextSyllableNavigator();
			MaryGenericFeatureProcessors.TargetElementNavigator lastWord = new MaryGenericFeatureProcessors.LastWordInSentenceNavigator();
			
			addFeatureProcessor(new MaryGenericFeatureProcessors.Zhtone("zh_tone", syllable));
			addFeatureProcessor(new MaryGenericFeatureProcessors.Zhtone("next_zhtone", nextSyllable));
			addFeatureProcessor(new MaryGenericFeatureProcessors.Zhtone("nextnext_zh_tone", nextNextSyllable));
			addFeatureProcessor(new MaryGenericFeatureProcessors.Zhtone("prev_zh_tone", prevSyllable));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("Problem building in zh featureManager");
		}
	}

	@Override
	public Locale getLocale() {
		return Locale.CHINESE;
	}

}
