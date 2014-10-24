package ${package}.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cacheonix.Cacheonix;
import cacheonix.cache.Cache;

/**
 * Clase que hace uso de la implementación de cache proveída por la librería
 * cacheonix para la lectura de archivos
 * 
 * @author desa3
 * 
 */
public class FileCache {

	/**
	 * Obtiene un archivo de la chache. Lo pone en la cache si todavía no lo
	 * está.
	 * 
	 * 
	 * @param pathName
	 *            la ruta al archivo.
	 * @return el contenido del archivo o null si no es encontrado.
	 * @throws IOException
	 *             si un error de I/O ocurre.
	 */
	public String getFileFromCache(String pathName) throws IOException {

		Cacheonix cacheonix = Cacheonix.getInstance();
		Cache<String, TextFile> cache = cacheonix
				.getCache("configuration.cache");

		File file = new File(pathName);
		if (!file.exists()) {

			cache.remove(pathName);
			return null;
		}

		TextFile textFile = cache.get(pathName);

		if (textFile == null) {

			textFile = readFile(file);
			cache.put(pathName, textFile);

		} else {

			if (textFile.getLastModified() != file.lastModified()) {

				textFile = readFile(file);
				cache.put(pathName, textFile);
			}
		}

		return textFile.getContent();
	}

	/**
	 * Lee el contenido del archivo y lo guarda en un objeto TextFile.
	 * 
	 * @param file
	 *            el archivo a ser leido.
	 * @return un nuevo objeto TextFile.
	 * @throws IOException
	 *             si un error de I/O ocurre.
	 */
	private static TextFile readFile(File file) throws IOException {

		FileReader fileReader = new FileReader(file);
		StringBuilder fileContent = new StringBuilder((int) file.length());
		BufferedReader br = new BufferedReader(fileReader);
		String s;
		while ((s = br.readLine()) != null) {
			fileContent.append("\r\n" + s);
		}

		fileReader.close();

		TextFile textFile = new TextFile();
		textFile.setContent(fileContent.toString());
		textFile.setLastModified(file.lastModified());

		return textFile;
	}

}
