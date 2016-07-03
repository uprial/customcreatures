package com.gmail.uprial.customcreatures.helpers;

import com.gmail.uprial.customcreatures.CreaturesConfig;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import com.google.common.collect.Lists;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import static com.gmail.uprial.customcreatures.common.Utils.joinStrings;

public abstract class TestConfigBase {
    protected void loadConfig(String content) throws InvalidConfigurationException, InvalidConfigException {
        loadConfig(new String[]{content});
    }

    protected void loadConfig(String ... contents) throws InvalidConfigurationException, InvalidConfigException {
        loadConfig(getCustomLogger(), contents);
    }

    protected void loadConfig(TestCustomLogger testCustomLogger, String ... contents) throws InvalidConfigurationException, InvalidConfigException {
        new CreaturesConfig(getPreparedConfig(contents), testCustomLogger);
    }

    protected YamlConfiguration getPreparedConfig(String ... contents) throws InvalidConfigurationException {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.loadFromString(joinStrings("\n", Lists.newArrayList(contents)));

        return yamlConfiguration;
    }

    protected TestCustomLogger getCustomLogger() {
        return new TestCustomLogger();
    }

    protected TestCustomLogger getDebugFearingCustomLogger() {
        TestCustomLogger testCustomLogger = new TestCustomLogger();
        testCustomLogger.doFailOnDebug();

        return testCustomLogger;
    }

    protected TestCustomLogger getParanoiacCustomLogger() {
        TestCustomLogger testCustomLogger = new TestCustomLogger();
        testCustomLogger.doFailOnAny();

        return testCustomLogger;
    }
}
