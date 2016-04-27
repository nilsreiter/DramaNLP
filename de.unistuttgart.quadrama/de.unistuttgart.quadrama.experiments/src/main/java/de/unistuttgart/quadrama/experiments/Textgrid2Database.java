package de.unistuttgart.quadrama.experiments;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.de.unistuttgart.quadrama.db.orm.DatabaseConsumer;

import com.j256.ormlite.db.MysqlDatabaseType;

import de.unistuttgart.quadrama.io.tei.textgrid.TextgridTEIUrlReader;

public class Textgrid2Database {

	public static void main(String[] args) throws Exception {

		String dbUrl = "jdbc:mysql://localhost/de.unistuttgart.quadrama";

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						TextgridTEIUrlReader.class,
						TextgridTEIUrlReader.PARAM_LANGUAGE, "de",
						TextgridTEIUrlReader.PARAM_URL_LIST,
						"src/main/resources/urls.txt");

		SimplePipeline.runPipeline(crd, AnalysisEngineFactory
				.createEngineDescription(DatabaseConsumer.class,
						DatabaseConsumer.PARAM_DB_URL, dbUrl,
						DatabaseConsumer.PARAM_DB_PASSWORD, "",
						DatabaseConsumer.PARAM_DB_USERNAME, "root",
						DatabaseConsumer.PARAM_DATABASETYPE,
						MysqlDatabaseType.class));

	}
}
