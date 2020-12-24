package com.meilisearch.sdk;

/**
 * Settings Object for index customization
 * Refer https://docs.meilisearch.com/references/settings.html
 */
public class Settings {
	private final MeiliSearchHttpRequest meilisearchHttpRequest;

	/**
	 * Constructor for the Meilisearch Settings object
	 *
	 * @param config Meilisearch configuration
	 */
	public Settings(Config config) {
		meilisearchHttpRequest = new MeiliSearchHttpRequest(config);
	}

	/**
	 * Gets the settings of a given index
	 * Refer https://docs.meilisearch.com/references/settings.html#get-settings
	 *
	 * @param uid Index identifier
	 * @return settings of a given uid as String
	 * @throws Exception if something goes wrong
	 */
	public String getSettings(String uid) throws Exception {
		return meilisearchHttpRequest.get("/indexes/" + uid + "/settings");
	}

	/**
	 * Updates the settings of a given index
	 * Refer https://docs.meilisearch.com/references/settings.html#update-settings
	 *
	 * @param uid             Index identifier
	 * @param settingsRequest the data that contains the new settings
	 * @return updateId is the id of the update
	 * @throws Exception if something goes wrong
	 */
	public String updateSettings(String uid, SettingsRequest settingsRequest) throws Exception {
		return meilisearchHttpRequest.post("/indexes/" + uid + "/settings", settingsRequest.getUpdateQuery());
	}

	/**
	 * Resets the settings of a given index
	 * Refer https://docs.meilisearch.com/references/settings.html#reset-settings
	 *
	 * @param uid Index identifier
	 * @return updateId is the id of the update
	 * @throws Exception
	 */
	public String resetSettings(String uid) throws Exception {
		return meilisearchHttpRequest.delete("/indexes/" + uid + "/settings");
	}
}


