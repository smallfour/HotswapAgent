package org.hotswap.agent.plugin.spring.scanner;

import org.hotswap.agent.command.MergeableCommand;
import org.hotswap.agent.logging.AgentLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class PropertiesRefreshCommand extends MergeableCommand {
    private static final AgentLogger LOGGER = AgentLogger.getLogger(PropertiesRefreshCommand.class);

    private final URL url;
    private final ClassLoader appClassLoader;

    public PropertiesRefreshCommand(ClassLoader classLoader, URL url) {
        this.url = url;
        this.appClassLoader = classLoader;
    }

    @Override
    public void executeCommand() {
        try {
            Class<?> clazz = Class.forName("org.hotswap.agent.plugin.spring.scanner.XmlBeanDefinitionScannerAgent", true, appClassLoader);
            Method method = clazz.getDeclaredMethod("reloadProperty", URL.class);
            method.invoke(null, url);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            LOGGER.error("Error refreshing property file {} in classLoader {}", e, this.url, appClassLoader);
        }
    }
}
