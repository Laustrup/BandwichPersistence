package laustrup.bandwichpersistence.core.persistence;

import lombok.Getter;

@Getter
public enum DataType {

    BIT(-7),
    TINYINT(-6),
    SMALLINT(5),
    INTEGER(4),
    BIGINT(-5),
    FLOAT(6),
    REAL(7),
    DOUBLE(8),
    NUMERIC(2),
    DECIMAL(3),
    CHAR(1),
    VARCHAR(12),
    LONGVARCHAR(-1),
    DATE(91),
    TIME(92),
    TIMESTAMP(93),
    BINARY(-2),
    VARBINARY(-3),
    LONGVARBINARY(-4),
    NULL(0),
    OTHER(1111),
    JAVA_OBJECT(2000),
    DISTINCT(2001),
    STRUCT(2002),
    ARRAY(2003),
    BLOB(2004),
    CLOB(2005),
    REF(2006),
    DATALINK(70),
    BOOLEAN(16),
    ROWID(-8),
    NCHAR(-15),
    NVARCHAR(-9),
    LONGNVARCHAR(-16),
    NCLOB(2011),
    SQLXML(2009),
    REF_CURSOR(2012),
    TIME_WITH_TIMEZONE(2013),
    TIMESTAMP_WITH_TIMEZONE(2014);

    private int _sqlType;

    public static DataType valueOf(int sqlType) {
        return switch (sqlType) {
            case -7 -> BIT;
            case -6 -> TINYINT;
            case 5 -> SMALLINT;
            case 4 -> INTEGER;
            case -5 -> BIGINT;
            case 6 -> FLOAT;
            case 7 -> REAL;
            case 8 -> DOUBLE;
            case 2 -> NUMERIC;
            case 3 -> DECIMAL;
            case 1 -> CHAR;
            case 12 -> VARCHAR;
            case -1 -> LONGVARCHAR;
            case 91 -> DATE;
            case 92 -> TIME;
            case 93 -> TIMESTAMP;
            case -2 -> BINARY;
            case -3 -> VARBINARY;
            case -4 -> LONGVARBINARY;
            case 0 -> NULL;
            case 2000 -> JAVA_OBJECT;
            case 2001 -> DISTINCT;
            case 2002 -> STRUCT;
            case 2003 -> ARRAY;
            case 2004 -> BLOB;
            case 2005 -> CLOB;
            case 2006 -> REF;
            case 70 -> DATALINK;
            case 16 -> BOOLEAN;
            case -8 -> ROWID;
            case -15 -> NCHAR;
            case -9 -> NVARCHAR;
            case -16 -> LONGNVARCHAR;
            case 2011 -> NCLOB;
            case 2009 -> SQLXML;
            case 2012 -> REF_CURSOR;
            case 2013 -> TIME_WITH_TIMEZONE;
            case 2014 -> TIMESTAMP_WITH_TIMEZONE;
            default -> OTHER;
        };
    }

    DataType(int sqlType) {
        _sqlType = sqlType;
    }
}
