package com.dbbest.kirilenko.interactionWithDB.constants;

/**
 * this class contains constants for working with MySQL database
 */
public class MySQLConstants {

    /**
     * class with names of nodes constants
     */
    public static class NodeNames {
        public final static String TABLES = "tables";
        public final static String VIEWS = "views";
        public final static String FUNCTIONS = "functions";
        public final static String PROCEDURES = "procedures";
        public final static String COLUMNS = "columns";
        public final static String INDEXES = "indexes";
        public final static String PRIMARY_KEYS = "primary_keys";
        public final static String FOREIGN_KEYS = "foreign_keys";
        public final static String TRIGGERS = "triggers";
        public final static String PARAMETERS = "parameters";
    }

    /**
     * class contains database entity names
     */
    public static class DBEntity {
        public final static String SCHEMA = "schema";
        public final static String TABLE = "table";
        public final static String VIEW = "view";
        public final static String FUNCTION = "function";
        public final static String PROCEDURE = "procedure";
        public final static String COLUMN = "column";
        public final static String INDEX = "index";
        public final static String PRIMARY_KEY = "primary_key";
        public final static String FOREIGN_KEY = "foreign_key";
        public final static String TRIGGER = "trigger";
        public final static String PARAMETER = "parameter";

    }
    /**
     * class contains INFORMATION_SCHEMA column names
     */
    public static class AttributeName{
        public final static String NAME = "NAME";
        public final static String SCHEMA_NAME = "SCHEMA_NAME";
        public final static String TABLE_SCHEMA = "TABLE_SCHEMA";
        public final static String DEFAULT_CHARACTER_SET_NAME = "DEFAULT_CHARACTER_SET_NAME";
        public final static String DEFAULT_COLLATION_NAME = "DEFAULT_COLLATION_NAME";
        public final static String TABLE_NAME = "TABLE_NAME";
        public final static String ENGINE = "ENGINE";
        public final static String TABLE_COLLATION = "TABLE_COLLATION";
        public final static String SECURITY_TYPE = "SECURITY_TYPE";
        public final static String VIEW_DEFINITION = "VIEW_DEFINITION";
        public final static String ROUTINE_NAME = "ROUTINE_NAME";
        public final static String PARAMETER_MODE = "PARAMETER_MODE";
        public final static String PARAMETER_NAME = "PARAMETER_NAME";
        public final static String DTD_IDENTIFIER = "DTD_IDENTIFIER";
        public final static String IS_DETERMINISTIC = "IS_DETERMINISTIC";
        public final static String ROUTINE_COMMENT = "ROUTINE_COMMENT";
        public final static String SQL_DATA_ACCESS = "SQL_DATA_ACCESS";
        public final static String ROUTINE_DEFINITION = "ROUTINE_DEFINITION";
        public final static String TRIGGER_NAME = "TRIGGER_NAME";
        public final static String ACTION_TIMING = "ACTION_TIMING";
        public final static String EVENT_MANIPULATION = "EVENT_MANIPULATION";
        public final static String EVENT_OBJECT_TABLE = "EVENT_OBJECT_TABLE";
        public final static String ACTION_ORIENTATION = "ACTION_ORIENTATION";
        public final static String ACTION_STATEMENT = "ACTION_STATEMENT";
        public final static String COLUMN_NAME = "COLUMN_NAME";
        public final static String NON_UNIQUE = "NON_UNIQUE";
        public final static String COLUMNS_NAME = "COLUMNS_NAME";
        public final static String INDEX_TYPE = "INDEX_TYPE";
        public final static String INDEX_NAME = "INDEX_NAME";
        public final static String CONSTRAINT_NAME = "CONSTRAINT_NAME";
        public final static String REFERENCED_TABLE_NAME = "REFERENCED_TABLE_NAME";
        public final static String REFERENCED_COLUMN_NAME = "REFERENCED_COLUMN_NAME";
        public final static String DELETE_RULE = "DELETE_RULE";
        public final static String UPDATE_RULE = "UPDATE_RULE";
        public final static String COLUMN_TYPE = "COLUMN_TYPE";
        public final static String IS_NULLABLE = "IS_NULLABLE";
        public final static String COLUMN_DEFAULT = "COLUMN_DEFAULT";
        public final static String EXTRA = "EXTRA";
        public final static String DATA_TYPE = "DATA_TYPE";
        public final static String ROUTINE_SCHEMA = "ROUTINE_SCHEMA";
        public final static String SPECIFIC_NAME = "SPECIFIC_NAME";
    }
    /**
     * class contains delimiters for printers
     */
    public static class Delimiters {
        public final static String NEW_DELIMITER = "DELIMITER $$";
        public final static String OLD_DELIMITER = "DELIMITER ;";
        public final static String DELIMITER = " $$";
        public final static String COMA = ",";
        public final static String SEMICOLON = ";";

    }
}
