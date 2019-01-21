/**
 * <p>
 * Plugin internationalization text identifiers.
 * </p>
 * 
 * <p>
 * Each constant field in this interface denotes the name of a property in the
 * corresponding properties file.
 * </p>
 * 
 * <p>
 * The property file must be named with the same name as this class, so in this
 * case the default localization is named <code>EchoMessages.properties</code>.
 * </p>
 * 
 * <p>
 * Language localizations have the locale name at the end of the name, for
 * example the german localization is named:
 * <code>EchoMessages_de.properties</code>.
 * </p>
 * 
 * @author cdivilly
 * 
 */
public interface EchoMessages {

  static final String DESCRIPTION = "DESCRIPTION";
  static final String CASE        = "CASE";
  static final String TEXT        = "TEXT";

}
