package halfpipe.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterAttachable;
import com.google.common.base.Optional;

import static halfpipe.configuration.LoggingConfiguration.*;

public class LogbackFactory {
    private LogbackFactory() { /* singleton */ }

    public static SyslogAppender buildSyslogAppender(SyslogConfiguration syslog,
                                                     LoggerContext context,
                                                     String name,
                                                     Optional<String> logFormat) {
        final SyslogAppender appender = new SyslogAppender();
        appender.setName(name);
        appender.setContext(context);
        appender.setSyslogHost(syslog.host.get());
        appender.setFacility(syslog.facility.get().toString());
        addThresholdFilter(appender, syslog.threshold.get());

        for (String format : logFormat.asSet()) {
            appender.setSuffixPattern(format);
        }

        appender.start();

        return appender;
    }

    public static FileAppender<ILoggingEvent> buildFileAppender(FileConfiguration file,
                                                                LoggerContext context,
                                                                Optional<String> logFormat) {
        final LogFormatter formatter = new LogFormatter(context, file.timeZone.get());
        for (String format : logFormat.asSet()) {
            formatter.setPattern(format);
        }
        formatter.start();

        final FileAppender<ILoggingEvent> appender =
                file.archive.get() ? new RollingFileAppender<ILoggingEvent>() :
                        new FileAppender<ILoggingEvent>();

        appender.setAppend(true);
        appender.setContext(context);
        appender.setLayout(formatter);
        appender.setFile(file.currentLogFilename.get());
        appender.setPrudent(false);

        addThresholdFilter(appender, file.threshold.get());

        if (file.archive.get()) {

            final DefaultTimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent> triggeringPolicy =
                    new DefaultTimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent>();
            triggeringPolicy.setContext(context);

            final TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
            rollingPolicy.setContext(context);
            rollingPolicy.setFileNamePattern(file.archivedLogFilenamePattern.get());
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(
                    triggeringPolicy);
            triggeringPolicy.setTimeBasedRollingPolicy(rollingPolicy);
            rollingPolicy.setMaxHistory(file.archivedFileCount.get());

            ((RollingFileAppender<ILoggingEvent>) appender).setRollingPolicy(rollingPolicy);
            ((RollingFileAppender<ILoggingEvent>) appender).setTriggeringPolicy(triggeringPolicy);

            rollingPolicy.setParent(appender);
            rollingPolicy.start();
        }

        appender.stop();
        appender.start();

        return appender;
    }

    public static ConsoleAppender<ILoggingEvent> buildConsoleAppender(ConsoleConfiguration console,
                                                                      LoggerContext context,
                                                                      Optional<String> logFormat) {
        final LogFormatter formatter = new LogFormatter(context, console.timeZone.get());
        for (String format : logFormat.asSet()) {
            formatter.setPattern(format);
        }
        formatter.start();

        final ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.setContext(context);
        appender.setLayout(formatter);
        addThresholdFilter(appender, console.threshold.get());
        appender.start();

        return appender;
    }

    private static void addThresholdFilter(FilterAttachable<ILoggingEvent> appender, Level threshold) {
        final ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(threshold.toString());
        filter.start();
        appender.addFilter(filter);
    }
}
