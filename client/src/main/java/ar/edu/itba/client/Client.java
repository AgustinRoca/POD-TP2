package ar.edu.itba.client;

import ar.edu.itba.api.Tree;
import ar.edu.itba.api.queryResults.*;
import ar.edu.itba.api.utils.CommandUtils;
import ar.edu.itba.client.queries.*;
import ar.edu.itba.client.utils.*;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static ar.edu.itba.api.utils.CommandUtils.JAVA_OPT;
import static ar.edu.itba.client.utils.CSVUtils.CSV_EXTENSION;

public class Client {
    private static Logger logger;

    //all the option/parameters/properties
    private static final String QUERY_OPT = "query";
    private static final String CITY_OPT = "city";
    private static final String NODES_ADDRS_OPT = "addresses";
    private static final String IN_PATH_OPT = "inPath";
    private static final String OUT_PATH_OPT = "outPath";
    private static final String MIN_OPT = "min";
    private static final String N_OPT = "n";
    private static final String NAME_OPT = "name";

    private static final String CABA_CITY = "BUE";
    private static final String VANCOUVER_CITY = "VAN";

    private static final String TREES_FILENAME = "arboles";
    private static final String CITIES_FILENAME = "barrios";

    private static final String TIME_FILENAME = "time";
    private static final String TXT_EXTENSION = "txt";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, ParseException{

        //get properties from command line
        Properties props = parseCommandLine(args);
        createLogger(FileUtils.formatFilePath(props.getProperty(OUT_PATH_OPT), TIME_FILENAME + props.getProperty(QUERY_OPT), TXT_EXTENSION));
        logger.info("Start");

        //get addresses
        String[] clientAddresses = props.getProperty(NODES_ADDRS_OPT).split(";");

        HazelcastInstance hz = connect(clientAddresses);
        logger.info("Inicio de lectura de archivos");
        loadFiles(props, hz);
        logger.info("Fin de lectura de archivos");

        logger.info("Inicio map/reduce");
        try {
            runQuery(props, hz);
        } catch (Exception e) {
            disconnect(hz);
            throw e;
        }
        logger.info("Fin map/reduce");

        disconnect(hz);
    }

    private static void runQuery(Properties props, HazelcastInstance hz) throws InterruptedException, ExecutionException, IOException {
        int query = Integer.parseInt(props.getProperty(QUERY_OPT));
        String outPath = props.getProperty(OUT_PATH_OPT);

        switch (query) {
            case 1:
                Query1 query1 = new Query1(hz);
                List<Query1Result> results1 = query1.getResult();
                Query1Writer query1Writer = new Query1Writer(outPath);
                query1Writer.write(results1);
                break;
            case 2:
                Query2 query2 = new Query2(hz, Long.parseLong(props.getProperty(MIN_OPT)));
                List<Query2Result> results2 = query2.getResult();
                Query2Writer query2Writer = new Query2Writer(outPath);
                query2Writer.write(results2);
                break;
            case 3:
                Query3 query3 = new Query3(hz, Long.parseLong(props.getProperty(N_OPT)));
                List<Query3Result> results3 = query3.getResult();
                Query3Writer query3Writer = new Query3Writer(outPath);
                query3Writer.write(results3);
                break;
            case 4:
                Query4 query4 = new Query4(hz, props.getProperty(NAME_OPT), Long.parseLong(props.getProperty(MIN_OPT)));
                List<Query4Result> results4 = query4.getResult();
                Query4Writer query4Writer = new Query4Writer(outPath);
                query4Writer.write(results4);
                break;
            case 5:
                Query5 query5 = new Query5(hz);
                List<Query5Result> results5 = query5.getResult();
                Query5Writer query5Writer = new Query5Writer(outPath);
                query5Writer.write(results5);
                break;
        }
    }

    private static void loadFiles(Properties props, HazelcastInstance hz) throws IOException {
        String city = props.getProperty(CITY_OPT);
        CSVParser csvParser;
        if (city.equals(CABA_CITY)) {
            csvParser = new CABACSVParser();
        } else if (city.equals(VANCOUVER_CITY)) {
            csvParser = new VancouverCSVParser();
        } else {
            disconnect(hz);
            throw new IllegalArgumentException("Supplied city value is unsupported: " + city);
        }
        try {
            csvParser.parseTrees(FileUtils.formatFilePath(props.getProperty(IN_PATH_OPT), TREES_FILENAME + city, CSV_EXTENSION));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            disconnect(hz);
            throw e;
        }

        //load tree list provided above
        IList<Tree> iTreeList = hz.getList("tree-list");
        try {
            iTreeList.addAll(csvParser.getTrees());
        } catch (Exception e) {
            // Heap out of memory exception and others
            disconnect(hz);
            throw e;
        }

        //if query1 add the population map
        if(props.getProperty(QUERY_OPT).equals("1")){
            csvParser.parseCities(FileUtils.formatFilePath(props.getProperty(IN_PATH_OPT), CITIES_FILENAME + city, CSV_EXTENSION));

            IMap<String, Long> populationsIMap = hz.getMap("populations-map");
            try {
                populationsIMap.putAll(csvParser.getPopulation());
            } catch (Exception e) {
                // Heap out of memory exception and others
                disconnect(hz);
                throw e;
            }
        }
    }

    private static HazelcastInstance connect(String[] clientAddresses) {
        // Config and get hazelcast instance
        logger.info("tp2hazelcast Client Starting ...");
        ClientConfig cfg = new ClientConfig();
        GroupConfig groupConfig = cfg.getGroupConfig();
        groupConfig.setName("tp2-g5");
        groupConfig.setPassword("angi"); //AgusNicoGuidoIgnacio --> ANGI
        ClientNetworkConfig clientNetworkConfig = cfg.getNetworkConfig();
        for (String addr : clientAddresses) {
            if(addr.charAt(0) == '\''){
                addr = addr.substring(1);
            }
            if(addr.charAt(addr.length() - 1) == '\''){
                addr = addr.substring(0, addr.length() - 1);
            }
            System.out.println(addr);
            clientNetworkConfig.addAddress(addr);
        }

        return HazelcastClient.newHazelcastClient(cfg);
    }

    private static void disconnect(HazelcastInstance hz) {
        //remove all added objects
        hz.getDistributedObjects().forEach(DistributedObject::destroy);
        // disconnect from cluster
        hz.shutdown();
    }

    //TODO: check this vs System.getProperty("property name");
    private static Properties parseCommandLine(String[] args) throws ParseException {
        // basic options needed
        Option queryOption = new Option(JAVA_OPT, "specifies the query to execute");
        queryOption.setArgName(QUERY_OPT);
        queryOption.setRequired(true);

        Option cityOption = new Option(JAVA_OPT, "specifies the city dataset used");
        cityOption.setArgName(CITY_OPT);
        cityOption.setRequired(true);

        Option addressesOption = new Option(JAVA_OPT, "specifies the ip addresses of the nodes");
        addressesOption.setArgName(NODES_ADDRS_OPT);
        addressesOption.setRequired(true);

        Option inPathOption = new Option(JAVA_OPT, "specifies the path to the input files");
        inPathOption.setArgName(IN_PATH_OPT);
        inPathOption.setRequired(true);

        Option outPathOption = new Option(JAVA_OPT, "specifies the path to the output files");
        outPathOption.setArgName(OUT_PATH_OPT);
        outPathOption.setRequired(true);

        //return the properties given
        Properties properties = CommandUtils.parseCommandLine(
                args,
                queryOption, cityOption,addressesOption,inPathOption,outPathOption
        );

        properties.putAll(parseQueryArgsCommandLine(Integer.parseInt(properties.getProperty(QUERY_OPT)), args));

        return properties;
    }

    // Apache Commons CLI necesita que parseemos los opcionales antes, porque
    // el parser siempre chequea requeridos, y si mezclamos opciones requeridas
    // y no requeridas estas ultimas se tomaran como requeridas
    private static Properties parseQueryArgsCommandLine(int query, String[] args) throws ParseException {
        //specific options

        Collection<Option> options = new LinkedList<>();
        if (query == 2 || query == 4) {
            Option minOpt = new Option(JAVA_OPT, "min parameter: specific to queries 2 and 4");
            minOpt.setArgName(MIN_OPT);
            minOpt.setRequired(true);
            options.add(minOpt);
        }
        if (query == 3) {
            Option nOpt = new Option(JAVA_OPT, "n parameter: specific to query 3");
            nOpt.setArgName(N_OPT);
            nOpt.setRequired(true);
            options.add(nOpt);
        }
        if (query == 4) {
            Option nameOpt = new Option(JAVA_OPT, "name parameter: specific to query 4");
            nameOpt.setArgName(NAME_OPT);
            nameOpt.setRequired(true);
            options.add(nameOpt);
        }

        return CommandUtils.parseCommandLine(
                args,
                options.toArray(new Option[0])
        );
    }

    private static void createLogger(String logFilePath) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        Layout layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, null, config,
                null,null, false, false, null, null);
        Appender appender = FileAppender.createAppender(logFilePath, "false", "false", "File", "true",
                "false", "false", "4000", layout, null, "false", null, config);
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
        AppenderRef[] refs = new AppenderRef[] {ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, Level.INFO, "Client",
                "true", refs, null, config, null );
        loggerConfig.addAppender(appender, null, null);
        config.addLogger("Client", loggerConfig);
        ctx.updateLoggers();
        logger = ctx.getLogger("Client");
    }
}
