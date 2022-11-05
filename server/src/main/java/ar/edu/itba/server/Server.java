package ar.edu.itba.server;

import ar.edu.itba.api.utils.CommandUtils;
import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

import static ar.edu.itba.api.utils.CommandUtils.JAVA_OPT;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final String INTERFACES_OPT = "i";
    private static final String HZ_CONFIG_OPT = "hzPath";

    public static void main(String[] args) throws ParseException,FileNotFoundException {
        logger.info("tp2-g5 Server Starting ...");

        Config config = getConfiguration(args);
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
    }

    private static Config getConfiguration( String[] args) throws ParseException, FileNotFoundException {
        Config config;
        Properties properties = parseCommandLine(args);
        if(properties.getProperty(HZ_CONFIG_OPT) != null){
            config = (new XmlConfigBuilder(properties.getProperty(HZ_CONFIG_OPT))).build();
        }else{
            config = (new XmlConfigBuilder()).build();
        }

        if (properties.getProperty(INTERFACES_OPT) != null) {
            InterfacesConfig interfacesConfig = new InterfacesConfig();
            interfacesConfig.setEnabled(true);

            Arrays.stream(properties.getProperty(INTERFACES_OPT).split(";")).forEach(interfacesConfig::addInterface);

            config.getNetworkConfig().setInterfaces(interfacesConfig);
        }
        return config;
    }

    //TODO: check this vs System.getProperty("property name");
    private static Properties parseCommandLine(String[] args) throws ParseException {
        Option interfaceOpt = new Option(JAVA_OPT, "specifies the interfaces to bind to ; separated");
        interfaceOpt.setArgName(INTERFACES_OPT);
        interfaceOpt.setRequired(false);

        Option hzConfigOpt = new Option(JAVA_OPT, "specifies the interfaces to bind to ; separated");
        hzConfigOpt.setArgName(HZ_CONFIG_OPT);
        hzConfigOpt.setRequired(false);

        //return the properties given
        return CommandUtils.parseCommandLine(
                args,
                interfaceOpt,
                hzConfigOpt
        );
    }
}
