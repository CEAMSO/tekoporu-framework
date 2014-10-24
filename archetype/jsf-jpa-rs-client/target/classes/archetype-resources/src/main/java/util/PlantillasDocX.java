package ${package}.util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Br;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;
import org.slf4j.Logger;

/**
 * @author desa1
 * 
 */

public class PlantillasDocX {
	static boolean saveFO = true;

	@Inject
	private static Logger log;
	/*
	 * Obtiene todos los elementos de del obj JAXBElement según la clase
	 * toSearch
	 */
	public static List<Object> getAllElementFromObject(Object obj,
			Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}

	/*
	 * Busca la cadena toFind en el documento doc y lo reemplaza con replacer
	 */
	public static void findAndReplaceString(WordprocessingMLPackage doc,
			String toFind, String replacer) {
		List<Object> paragraphs = getAllElementFromObject(
				doc.getMainDocumentPart(), P.class);
		for (Object par : paragraphs) {
			P p = (P) par;
			List<Object> texts = getAllElementFromObject(p, Text.class);
			for (Object text : texts) {
				Text t = (Text) text;
				if (t.getValue().contains(toFind)) {
					t.setValue(t.getValue().replace(toFind, replacer));
				}
			}
		}
	}

	/*
	 * Busca y reemplaza los tags de toFindList y reemplaza con su valor en el
	 * diccionario values
	 */
	public static void replaceAllWithString(Set<String> toFindList,
			Map<String, String> values, WordprocessingMLPackage template) {
		List<Object> paragraphs = getAllElementFromObject(
				template.getMainDocumentPart(), P.class);

		for (Object par : paragraphs) {
			P p = (P) par;
			List<Object> runs = getAllElementFromObject(p, R.class);

			for (Object run : runs) {
				R r = (R) run;
				List<Object> texts = getAllElementFromObject(r, Text.class);
				for (Object text : texts) {
					Text t = (Text) text;
					for (String toFind : toFindList) {

						if (t.getValue() != null
								&& t.getValue().contains(toFind)) {
							if (values.get(toFind) != null) {
								t.setValue(t.getValue().replace(toFind,
										values.get(toFind)));
							} else {
								log.info("No se pasó valor para "
										+ toFind);
							}
						}
					}

				}
			}
		}
	}

	/*
	 * Busca y reemplaza los tags de toFindList y reemplaza con su valor en el
	 * diccionario values
	 */
	public static void replaceWithXhtml(Object p, Object r,
			List<Object> toInsert, Boolean replaceWithP, String placeholder,
			String value, WordprocessingMLPackage template)
			throws Docx4JException, JAXBException {
		String[] parts = value.split(Pattern.quote(placeholder));
		List<String> partsList = new ArrayList<String>(Arrays.asList(parts));

		if (partsList.size() > 0) {
			if (parts[0].compareTo("") != 0 && parts.length == 1) {
				partsList.add("");
			}

			List<Object> insertPoints = new ArrayList<Object>();
			for (int i = 0; i < partsList.size(); i++) {

				R copy;

				// sacar tabs si tiene si no es la última parte
				if (i < partsList.size() - 1) {
					copy = new R();
					RPr copyRpr = XmlUtils.deepCopy(((R) r).getRPr());
					copy.setRPr(copyRpr);
					Text textOfRun = (Text) XmlUtils
							.deepCopy(((JAXBElement) ((R) r).getContent()
									.get(0)).getValue());
					textOfRun.setValue(partsList.get(i));
				} else {
					copy = (R) XmlUtils.deepCopy(r);
					Text textOfRun = (Text) ((JAXBElement) copy.getContent()
							.get(0)).getValue();
					textOfRun.setValue(partsList.get(i));
				}

				insertPoints.add(copy);

			}

			replaceElement(r, insertPoints, ((P) p).getContent());

			for (int i = 0; i < insertPoints.size() - 1; i++) {
				// Si hay más de un párrafo
				if (toInsert.size() > 1) {
					// if (replaceWithP) {
					// insertAtElement(p, toInsert,
					// template.getMainDocumentPart().getJaxbElement().getContent());
					// } else {
					List<Object> runs = paragraphsToRuns(toInsert);
					mergeProperties((R) r, runs);
					insertAtElement(insertPoints.get(i), runs,
							((P) p).getContent());
				} else if (toInsert.size() == 1) {
					List<Object> runs = ((P) toInsert.get(0)).getContent();
					mergeProperties((R) r, runs);
					insertAtElement(insertPoints.get(i), runs,
							((P) p).getContent());
				}
			}
		} else {
			if (replaceWithP) {
				mergeProperties((P) p, (R) r, toInsert);
				/*
				 * System.out.println(
				 * "AFTER To Insert+++++++++++++++++++++++++++++++++++++"); for
				 * (Object obj : toInsert) {
				 * System.out.println(XmlUtils.marshaltoString(obj, true,
				 * true)); }
				 */
				replaceElement(p, toInsert, template.getMainDocumentPart()
						.getJaxbElement().getContent());
			} else {
				List<Object> runs = paragraphsToRuns(toInsert);
				mergeProperties((R) r, runs);
				replaceElement(r, runs, ((P) p).getContent());
			}
		}
	}

	/*
	 * Busca y reemplaza los tags de xhtml y reemplaza con su valor en el
	 * diccionario values, que se espar sea una porción xhtml
	 */
	public static void replaceAllWithXhtml(Set<String> xhtml,
			Map<String, String> values, WordprocessingMLPackage template)
			throws Docx4JException, JAXBException {

		List<Object> paragraphs = getAllElementFromObject(template
				.getMainDocumentPart().getJaxbElement().getBody(), P.class);

		// WordprocessingMLPackage wordMLPackage =
		// WordprocessingMLPackage.createPackage();
		// xHtmlImporter.setRunFormatting(FormattingOption.CLASS_PLUS_OTHER);
		// xHtmlImporter.setParagraphFormatting(FormattingOption.CLASS_PLUS_OTHER);

		List<Object> toInsert;

		ListIterator<Object> it = paragraphs.listIterator();
		while (it.hasNext()) {
			Object p = it.next();

			List<Object> runs = getAllElementFromObject(p, R.class);
			boolean replaceWithP = false;

			if (runs.size() == 1) {
				// se pueden insertar párrafos
				replaceWithP = true;
			}

			/*
			 * System.out.println("+++++++ P +++++++++++++++++++++++++++++++++++++"
			 * ); System.out.println(XmlUtils.marshaltoString(p, true, true));
			 */
			for (Object r : runs) {
				List<Object> rContent = getAllElementFromObject(r, Text.class);

				for (Object t : rContent) {
					Text content = (Text) t;
					String value = content.getValue();
					// System.out.println("Value+++++++++++" + value);
					for (String placeholder : xhtml) {
						
						int index = value.indexOf(placeholder);

						if (index >= 0) {

							// covertir desde XHTML
							XHTMLImporter xHtmlImporter = new XHTMLImporterImpl(
									template);
							toInsert = xHtmlImporter.convert(
									values.get(placeholder), null);

							/*System.out
									.println("P+++++++++++++++++++++++++++++++++++++");
							System.out.println(XmlUtils.marshaltoString(p,
									true, true));
							System.out
									.println("R+++++++++++++++++++++++++++++++++++++");
							System.out.println(XmlUtils.marshaltoString(r,
									true, true));
							for (Object obj : toInsert) {
								System.out
										.println("To Insert+++++++++++++++++++++++++++++++++++++");
								System.out.println(XmlUtils.marshaltoString(
										obj, true, true));
							}*/

							replaceWithXhtml(p, r, toInsert, replaceWithP,
									placeholder, value, template);
						}
					}
				}
			}
		}
	}

	/*
	 * Copia las propiedades del parráfo pFrom al párrafo pTo Solo conserva la
	 * numeración y la alineación del pTo
	 */
	public static void mergePropertiesParagraph(P pFrom, P pTo) {
		PPr pprAux;
		if (pFrom.getPPr() != null) {
			pprAux = XmlUtils.deepCopy(pFrom.getPPr());
		} else {
			pprAux = new PPr();
		}

		if (pTo.getPPr() != null) {
			PPr pprTo = XmlUtils.deepCopy(pTo.getPPr());// XmlUtils.deepCopy(pTo.getPPr());

			pprAux.setJc(pprTo.getJc());
			if (pprTo.getNumPr() != null) {
				pprAux.setNumPr(pprTo.getNumPr());
			}
		}

		pTo.setPPr(pprAux);
	}

	/*
	 * Copia las propiedades del run rFrom al run rTo Solo conserva los estilos
	 * de Negrita, Cursiva, Subrayado y Tachado
	 */
	public static void mergePropertiesRun(R rFrom, R rTo) {

		RPr rprAux;
		if (rFrom.getRPr() != null) {
			rprAux = XmlUtils.deepCopy(rFrom.getRPr());
		} else {
			rprAux = new RPr();
		}

		if (rTo.getRPr() != null) {
			RPr rprTo = XmlUtils.deepCopy(rTo.getRPr());

			rprAux.setB(rprTo.getB());
			rprAux.setI(rprTo.getI());
			rprAux.setU(rprTo.getU());
			rprAux.setStrike(rprTo.getStrike());
		}

		rTo.setRPr(rprAux);
	}

	/*
	 * Copia las propiedades del run rFrom al todos los runs de la lista rTos
	 * Solo conserva los estilos de Negrita, Cursiva, Subrayado y Tachado
	 */
	public static void mergeProperties(R rFrom, List<Object> rTos) {
		for (Object r : rTos) {
			mergePropertiesRun(rFrom, ((R) r));
		}
	}

	/*
	 * Copia las propiedades del parráfo pFrom al todos los párrafos de pTos
	 * Solo conserva la numeración y la alineación del pTo Además por cada run
	 * del los párrafos de pTos
	 */
	public static void mergeProperties(P pFrom, R rFrom, List<Object> pTos) {
		for (Object p : pTos) {
			if (p instanceof P) {
				mergePropertiesParagraph(pFrom, ((P) p));
			}
			
			List<Object> runs = getAllElementFromObject(p, R.class);
			for (Object r : runs) {
				mergePropertiesRun(rFrom, ((R) r));
			}
		}
	}

	/*
	 * Convierte los párrafos paragraphs en un sólo párrafo, agrupando todos los
	 * runs de ellos, separando los subgrupos con un BR
	 */
	public static List<Object> paragraphsToRuns(List<Object> paragraphs) {
		// convertir a Runs
		List<Object> runsToInsert = new ArrayList<Object>();
		int pIndex = 0;
		for (Object pConvert : paragraphs) {
			pIndex++;
			List<Object> runsConvert = getAllElementFromObject(pConvert,
					R.class);
			for (Object rc : runsConvert) {
				runsToInsert.add(rc);
			}
			// insertar BRs entre entre los runs de párrafos distintos

			if (!runsToInsert.isEmpty() && pIndex < paragraphs.size()) {
				// Text espacio = new Text();
				// espacio.setValue(" ");
				((R) runsToInsert.get(runsToInsert.size() - 1)).getContent()
						.add(new Br());
			}

		}

		return runsToInsert;
	}

	/*
	 * Inserta la lista de elementos de insertions despues de current en la
	 * lista content
	 */
	public static void insertAtElement(Object current, List insertions,
			List content) {
		int index = content.indexOf(current);
		if (index > -1) {
			content.addAll(index + 1, insertions);
		} else {
			// Not found
			log.info("++++++++++++++++++++++Couldn't find target to insert.");
		}
	}

	/*
	 * Inserta la lista de elementos de insertions en el lugar de current en la
	 * lista content
	 */
	public static void replaceElement(Object current, List insertions,
			List content) {
		int index = content.indexOf(current);
		if (index > -1) {
			content.addAll(index + 1, insertions);
			Object removed = content.remove(index);
			// sanity check
			if (!current.equals(removed)) {
				log.info("+++++++++++++++removed wrong object?");
			}
		} else {
			// Not found
			log.info("+++++++++++++++++++Couldn't find replacement target.");
		}
	}

	public static void savePdf(WordprocessingMLPackage wordMLPackage,
			String inputfilepath) throws Exception {
		// Font regex (optional)
		// Set regex if you want to restrict to some defined subset of fonts
		// Here we have to do this before calling createContent,
		// since that discovers fonts
		String regex = null;
		// Windows:
		// String
		// regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
		regex = ".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
		// Mac
		// String
		// regex=".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
		PhysicalFonts.setRegex(regex);

		// Load .docx or Flat OPC .xml
		// System.out.println("Loading file from " + inputfilepath);
		// wordMLPackage = WordprocessingMLPackage.load(new
		// java.io.File(inputfilepath));

		// Set up font mapper (optional)
		Mapper fontMapper = new IdentityPlusMapper();
		wordMLPackage.setFontMapper(fontMapper);

		// .. example of mapping font Times New Roman which doesn't have certain
		// Arabic glyphs
		// eg Glyph "ي" (0x64a, afii57450) not available in font
		// "TimesNewRomanPS-ItalicMT".
		// eg Glyph "ج" (0x62c, afii57420) not available in font
		// "TimesNewRomanPS-ItalicMT".
		// to a font which does
		PhysicalFont font = PhysicalFonts.getPhysicalFonts().get(
				"Arial Unicode MS");
		// make sure this is in your regex (if any)!!!
		if (font != null) {
			fontMapper.getFontMappings().put("Times New Roman", font);
		}

		fontMapper.getFontMappings().put("Libian SC Regular",
				PhysicalFonts.getPhysicalFonts().get("SimSun"));

		// FO exporter setup (required)
		// .. the FOSettings object
		FOSettings foSettings = Docx4J.createFOSettings();
		if (saveFO) {
			foSettings.setFoDumpFile(new java.io.File(inputfilepath + ".fo"));
		}

		foSettings.setWmlPackage(wordMLPackage);

		// Document format:
		// The default implementation of the FORenderer that uses Apache Fop
		// will output
		// a PDF document if nothing is passed via
		// foSettings.setApacheFopMime(apacheFopMime)
		// apacheFopMime can be any of the output formats defined in
		// org.apache.fop.apps.MimeConstants eg
		// org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
		// FOSettings.INTERNAL_FO_MIME if you want the fo document as the
		// result.
		// foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);

		// exporter writes to an OutputStream.
		String outputfilepath;
		if (inputfilepath == null) {
			outputfilepath = System.getProperty("user.dir")
					+ "/OUT_FontContent.pdf";
		} else {
			outputfilepath = inputfilepath + ".pdf";
		}

		OutputStream os = new java.io.FileOutputStream(outputfilepath);

		// Specify whether PDF export uses XSLT or not to create the FO
		// (XSLT takes longer, but is more complete).

		// Don't care what type of exporter you use
		Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

		// Prefer the exporter, that uses a xsl transformation
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		// Prefer the exporter, that doesn't use a xsl transformation (= uses a
		// visitor)
		// .. faster, but not yet at feature parity
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);
		log.info("Saved: " + outputfilepath);

	}

}
