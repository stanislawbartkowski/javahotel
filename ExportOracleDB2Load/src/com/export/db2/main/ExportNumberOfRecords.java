package com.export.db2.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.export.db2.main.util.NumberOfRecords;

public class ExportNumberOfRecords {

	private static Logger log = Logger.getLogger(ExportNumberOfRecords.class.getName());

	private static void P(String s) {
		System.out.println(s);
	}

	private static void printHelp() {
		P("Parameters:");
		P(" <propertyfile> <input table> <output table> ");
		P("Example: ");
		P(" /home/sb/export/prop.properties table.list oracle.number");
	}

	public static void main(String[] args) {

		if (args.length != 3) {
			P("Incorrect parameters.");
			printHelp();
			System.exit(10);
		}
		log.info("Extract number of records, input list " + args[1] + " output file " + args[2]);
		RunMain.doMain(args[0], new RunMain.RunTask() {

			@Override
			public void doTask(Connection con, Properties prop) throws SQLException, IOException {
				NumberOfRecords.exportNumbers(con, prop, args[1], args[2]);
			}
		});

	}

}
