package de.unistuttgart.quadrama.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.unistuttgart.quadrama.core.DramaSpeechSegmenter;
import de.unistuttgart.quadrama.core.FigureMentionDetection;
import de.unistuttgart.quadrama.core.FigureSpeechStatistics;
import de.unistuttgart.quadrama.core.SpeakerIdentifier;
import de.unistuttgart.quadrama.core.UtteranceLinker;
import de.unistuttgart.quadrama.graph.NetworkExtractor;
import de.unistuttgart.quadrama.io.dot.DotExporter;
import de.unistuttgart.quadrama.io.html.ConfigurationHTMLExporter;

public class VisualiseFreisch {

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		System.setProperty("java.util.logging.config.file",
				"src/main/resources/logging.properties");

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						"src/main/resources/freischuetz/*.xmi");

		SimplePipeline
				.runPipeline(
						crd,
						DramaSpeechSegmenter
								.getWrappedSegmenterDescription(LanguageToolSegmenter.class),
						createEngineDescription(SpeakerIdentifier.class),
						createEngineDescription(UtteranceLinker.class),
				createEngineDescription(StanfordPosTagger.class),
						// createEngineDescription(StanfordNamedEntityRecognizer.class),
						createEngineDescription(FigureSpeechStatistics.class),
						createEngineDescription(FigureMentionDetection.class),
						/*
						 * Export html view of configuration
						 */
						createEngineDescription(
								ConfigurationHTMLExporter.class,
								ConfigurationHTMLExporter.PARAM_TARGET_LOCATION,
								"target/f/html/"),
						/*
						 * Extract copresence network
						 */
						createEngineDescription(NetworkExtractor.class),
						/*
						 * extract mention network
						 */
						createEngineDescription(NetworkExtractor.class,
								NetworkExtractor.PARAM_VIEW_NAME,
								"MentionNetwork",
								NetworkExtractor.PARAM_NETWORK_TYPE,
								"MentionNetwork"),
						/*
						 * print xmi
						 */
						createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION,
								"target/f/xmi/"),
						createEngineDescription(DotExporter.class,
								DotExporter.PARAM_VIEW_NAME, "MentionNetwork",
								DotExporter.PARAM_TARGET_LOCATION,
								"target/f/net/mentions/"),
												createEngineDescription(DotExporter.class,
														DotExporter.PARAM_VIEW_NAME, "Copresence",
														DotExporter.PARAM_TARGET_LOCATION,
														"target/f/net/copresence/"));
	}
}