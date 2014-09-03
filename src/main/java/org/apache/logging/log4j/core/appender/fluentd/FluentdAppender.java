package org.apache.logging.log4j.core.appender.fluentd;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.fluentd.logger.FluentLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yosuketsuboi
 *
 */
@Plugin(name = "Fluentd", category = "Core", elementType = "appender", printObject = true)
public class FluentdAppender extends AbstractAppender {

    private static final Logger _log = LoggerFactory
            .getLogger("FluentdAppender");

    private final FluentLogger _fluentLogger;

    private final String _label;

    protected FluentdAppender(String name, Filter filter) {
        super(name, filter, null);
        // TODO
        String tagPrefix = "debug";
        String host = "192.168.56.3";
        int port = 24224;
        int timeout = 60;
        int bufferCapacity = 1024;
        this._fluentLogger = FluentLogger.getLogger(tagPrefix, host, port,
                timeout, bufferCapacity);
        // TODO
        this._label = "label";
    }

    @PluginFactory
    public static FluentdAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filters") Filter filter) {
        return new FluentdAppender(name, filter);
    }

    @Override
    public void stop() {
        super.stop();
        _fluentLogger.close();
        _log.info("stop()");
    }

    @Override
    public void append(LogEvent event) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("level", event.getLevel().toString());
        data.put("loggerName", event.getLoggerName());
        data.put("thread", event.getThreadName());
        data.put("message", event.getMessage().toString());
        _fluentLogger.log(_label, data, event.getTimeMillis());
    }

}
