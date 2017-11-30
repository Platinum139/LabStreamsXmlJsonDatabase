package LabJDBC;

import java.io.FileInputStream;
import java.util.Properties;

class PropertyManager {

    private static final String DEFAULT_PROPERTIES_FILE = "db.config";

    private PropertyManager() {
    }

    private static void loadProperties(Properties props) {
        try {
            FileInputStream in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
            props.load(in);
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Properties getProperties() {

        Properties props = new Properties();
        loadProperties(props);
        return props;
    }
}
