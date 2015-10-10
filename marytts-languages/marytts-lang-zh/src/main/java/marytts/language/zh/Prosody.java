package marytts.language.zh;

import java.util.Locale;

import marytts.datatypes.MaryDataType;
import marytts.modules.ProsodyGeneric;
import marytts.server.MaryProperties;

public class Prosody extends ProsodyGeneric {
	public Prosody() {
		super(MaryDataType.PHONEMES, MaryDataType.INTONATION, Locale.CHINESE, MaryProperties.localePrefix(Locale.CHINESE)
				+ ".prosody.tobipredparams", MaryProperties.localePrefix(Locale.ENGLISH) + ".prosody.accentPriorities",
				MaryProperties.localePrefix(Locale.CHINESE) + ".prosody.syllableaccents", MaryProperties
						.localePrefix(Locale.CHINESE) + ".prosody.paragraphdeclination");
		System.out.println("in chinese prosody init..");
	}
}
