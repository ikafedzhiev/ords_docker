import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;

import oracle.dbtools.plugin.api.cmdline.CommandProvider;
import oracle.dbtools.plugin.api.cmdline.annotations.Argument;
import oracle.dbtools.plugin.api.cmdline.annotations.Command;
import oracle.dbtools.plugin.api.cmdline.annotations.Option;
import oracle.dbtools.plugin.api.di.annotations.Provides;
import oracle.dbtools.plugin.api.i18n.annotations.TranslatableText;

/**
 * This example plugin demonstrates how to create a command line plugin.
 * 
 * <ul>
 * <li>This example command echoes the arguments passed to the console.</li>
 * <li>This example shows how to use the <code>java.util.logging.Logger</code>
 * API.</code></li>
 * <li>This example shows to define internationalized help documentation for the
 * command.</code></li>
 * </ul>
 * 
 * <h4>Testing the command plugin</h4>
 * <ul>
 * <li>Build this project:
 * 
 * <pre>
 * $ ant
 * </pre>
 * 
 * </li>
 * <li>Add the compiled jar to <code>ords.war</code>
 * 
 * <pre>
 * $ java -jar ../../../ords.war plugin built/plugin-echo-cmd.jar
 * </pre>
 * 
 * </li>
 * <li>Invoke the command:
 * 
 * <pre>
 * $ java -jar ../../../ords.war echo "Hello World"
 * </pre>
 * 
 * This should produce output like:
 * 
 * <pre>
 * Jun 17, 2007 10:33:00 AM EchoCommand execute
 * INFO: Hello World
 * </pre>
 * 
 * </li>
 * <li>Review the help for the command:
 * 
 * <pre>
 * $ java -jar ../../../ords.war help echo
 * </pre>
 * This should produce output like:
 * <pre>
 * java -jar ords.war echo [--case] &lt;text&gt;
 *
 * Options:
 *           [--case]              Modify the case of the text,
 *                                 values: upper|lower|preserve,
 *                                 default value: preserve
 *
 * Arguments:
 *           &lt;text&gt;*               The text to echo to the
 *                                 console
 * </pre>
 * </li>
 * </ul>
 * 
 * @author cdivilly
 * @see EchoMessages
 */
@Provides
@Command(name = "echo", description = @TranslatableText(
    type = EchoMessages.class, id = EchoMessages.DESCRIPTION),
    arguments = { @Argument(name = "text", type = String[].class,
        description = @TranslatableText(type = EchoMessages.class,
            id = EchoMessages.TEXT)) }, options = @Option(name = "case",
        description = @TranslatableText(type = EchoMessages.class,
            id = EchoMessages.CASE)))
class EchoCommand implements CommandProvider {
  @Inject
  EchoCommand(Logger logger) {
    this.logger = logger;
  }

  @Override
  public void execute(Map<String, Object> values) throws Exception {

    /* Get the value of the --case option */
    final TextCase textCase = TextCase.parse((String) values.get("case"));
    /* Get the values of the text argument */
    final String[] text = (String[]) values.get("text");

    /* Build the message */
    StringBuilder msg = new StringBuilder();
    for (String segment : text) {
      msg.append(segment);
      msg.append(' ');
    }
    String fullMsg = msg.toString();

    /* Adjust the case of the message */
    switch (textCase) {
    case UPPER:
      fullMsg = fullMsg.toUpperCase();
      break;
    case LOWER:
      fullMsg = fullMsg.toLowerCase();
    default:
    }
    /* Echo the message */
    logger.info(fullMsg);
  }

  private final Logger logger;

  /**
   * Enum representing the legal values of the --case option
   * 
   * @author cdivilly
   * 
   */
  private enum TextCase {
    LOWER, PRESERVE, UPPER;

    static TextCase parse(String value) {
      TextCase textCase = PRESERVE;

      if (value != null) {
        try {
          textCase = valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
        }
      }
      return textCase;
    }
  };
}