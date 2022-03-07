package de.b3ttertogeth3r.walhalla.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import de.b3ttertogeth3r.walhalla.firebase.Crashlytics;

public class MyLog {

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;
    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    private MyLog () {
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     */
    public static void v (String tag, String msg) {
        Crashlytics.verbose(tag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     * @param tr
     *         An exception to log
     */
    public static void v (String tag, String msg, Throwable tr) {
        Crashlytics.verbose(tag, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     */
    public static void d (String tag, String msg) {
        Crashlytics.debug(tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     * @param tr
     *         An exception to log
     */
    public static void d (String tag, String msg, Throwable tr) {
        Crashlytics.debug(tag, msg, tr);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     */
    public static void i (String tag, String msg) {
        Crashlytics.info(tag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     * @param tr
     *         An exception to log
     */
    public static void i (String tag, String msg, Throwable tr) {
        Crashlytics.info(tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     */
    public static void w (String tag, String msg) {
        Crashlytics.warn(tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     * @param tr
     *         An exception to log
     */
    public static void w (String tag, String msg, Throwable tr) {
        Crashlytics.warn(tag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param tr
     *         An exception to log
     */
    public static void w (String tag, Throwable tr) {
        Crashlytics.warn(tag, "warning error found", tr);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     */
    public static void e (String tag, String msg) {
        Crashlytics.error(tag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag
     *         Used to identify the source of a log message.  It usually identifies
     *         the class or activity where the log call occurs.
     * @param msg
     *         The message you would like logged.
     * @param tr
     *         An exception to log
     */
    public static void e (String tag, String msg, Throwable tr) {
        Crashlytics.error(tag, msg, tr);
    }
}
