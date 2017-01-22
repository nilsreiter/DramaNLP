package de.unistuttgart.quadrama.core;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.unistuttgart.ims.drama.api.Figure;
import de.unistuttgart.ims.drama.api.figure.Description;
import de.unistuttgart.ims.drama.api.figure.Name;
import de.unistuttgart.ims.uimautil.AnnotationUtil;

@TypeCapability(inputs = { "de.unistuttgart.ims.drama.api.Figure" }, outputs = {
		"de.unistuttgart.ims.drama.api.figure.Name", "de.unistuttgart.ims.drama.api.figure.Description" })
public class FigureDetailsAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Name fName = null;
		Description fDesc = null;
		Set<Figure> waitingForDescription = new HashSet<Figure>();
		for (Figure figure : JCasUtil.select(jcas, Figure.class)) {
			if (figure.getCoveredText().split(",").length <= 2) {
				int b = figure.getBegin();
				int commaPosition = figure.getCoveredText().indexOf(',');
				if (figure.getCoveredText().endsWith(",")) {
					// the line ends with a comma
					fName = AnnotationFactory.createAnnotation(jcas, b, b + commaPosition, Name.class);

					AnnotationUtil.trim(fName);
					figure.setName(fName);
					waitingForDescription.add(figure);
				} else if (commaPosition != -1) {
					fName = AnnotationFactory.createAnnotation(jcas, b, b + commaPosition, Name.class);
					fDesc = AnnotationFactory.createAnnotation(jcas, b + commaPosition + 1, figure.getEnd(),
							Description.class);

					AnnotationUtil.trim(fName);
					AnnotationUtil.trim(fDesc);

					figure.setName(fName);
					figure.setDescription(fDesc);
					for (Figure f : waitingForDescription) {
						f.setDescription(fDesc);
					}
				} else {
					// if no comma is contained in the line, we assume it to be
					// a name (for the time being)
					fName = AnnotationFactory.createAnnotation(jcas, b, figure.getEnd(), Name.class);
					figure.setName(fName);

				}
			}
		}
	}

}