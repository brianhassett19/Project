package nb_classifier;

/*
 * Contains an overloaded method 'clean' for cleaning a String or StringBuilder.
 */
public class PreProcessor {

	public StringBuilder clean(StringBuilder sb) {
		sb = convertToLowerCase(sb);
		sb = removeFullStops(sb);
		sb = removeCommas(sb);
		sb = removeColons(sb);
		sb = removeSemiColons(sb);

		return sb;
	}
	
	public String clean(String s) {
		StringBuilder sb = new StringBuilder(s);
	
		sb = clean(sb);
		s = sb.toString();
		
		return s;
	}
	
	private StringBuilder convertToLowerCase(StringBuilder sb) {
		String s = sb.toString();
		sb = new StringBuilder(s.toLowerCase());
		return sb;
	}

	private StringBuilder removeFullStops(StringBuilder sb) {
		String s = sb.toString();
		sb = new StringBuilder(s.replace(".", ""));
		return sb;
	}

	private StringBuilder removeCommas(StringBuilder sb) {
		String s = sb.toString();
		sb = new StringBuilder(s.replace(",", ""));
		return sb;
	}

	private StringBuilder removeColons(StringBuilder sb) {
		String s = sb.toString();
		sb = new StringBuilder(s.replace(":", ""));
		return sb;
	}

	private StringBuilder removeSemiColons(StringBuilder sb) {
		String s = sb.toString();
		sb = new StringBuilder(s.replace(";", ""));
		return sb;
	}
}
