package com.bookstore.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AUConstants {

	public final static String AU = "AU";
	public final static Map<String, String> mapOfAuStates = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("VIC", "Victoria");
			put("NSW", "New SouthWales");
			put("NT", "Northern Territory");
			put("QLD", "Queensland");
			put("SA", "South Australia");
			put("WA", "Western Australia");
			put("ACT", "Australian Captical Territory");
		}

	};

	public final static List<String> listOfAuStateAbbrevCodes = new ArrayList<>(mapOfAuStates.keySet());
	public final static List<String> listOfAuStateFullNames = new ArrayList<>(mapOfAuStates.values());
}
