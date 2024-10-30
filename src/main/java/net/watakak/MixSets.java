package net.watakak;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MixSets implements ModInitializer {
	public static final String MOD_ID = "mixsets";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		String loaderName = Utils.getLoader();
		if (Config.bool("Logs")) {
			LOGGER.info("Hello " + loaderName + " World!");
		}
	}
}