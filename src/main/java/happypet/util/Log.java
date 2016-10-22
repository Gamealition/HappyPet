package happypet.util;

import java.util.logging.Logger;

/** Utility class for logging syntactic sugar */
public class Log
{
    private final Logger logger;

    public Log(Logger logger)
    {
        this.logger = logger;
    }

    /** Logs a formattable finest message using the given objects */
    public Log finest(String msg, Object... args)
    {
        logger.finest(String.format(msg, args));
        return this;
    }

    /** Logs a formattable finer message using the given objects */
    public Log finer(String msg, Object... args)
    {
        logger.finer(String.format(msg, args));
        return this;
    }

    /** Logs a formattable fine message using the given objects */
    public Log fine(String msg, Object... args)
    {
        logger.fine(String.format(msg, args));
        return this;
    }

    /** Logs a formattable info message using the given objects */
    public Log info(String msg, Object... args)
    {
        logger.info( String.format(msg, args) );
        return this;
    }

    /** Logs a formattable warning message using the given objects */
    public Log warn(String msg, Object... args)
    {
        logger.warning( String.format(msg, args) );
        return this;
    }

    /** Logs a formattable severe message using the given objects */
    public Log err(String msg, Object... args)
    {
        logger.severe( String.format(msg, args) );
        return this;
    }
}
