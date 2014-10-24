package ${package}.util;


/**
 *  @clase      DrFileManager
 *  @author     Lic. David Reese
 *  @version    Plantilla 1.0 21/04/2009
 *              Plantilla 1.2 23/04/2009
 *              Plantilla 1.5 20/12/2010
 *
 *   This code is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   any later version.
 *
 *   DrFileManager is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with DrFileManager; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 **/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;


public class FileManager {

    public static String PATH_ARCHIVOS = "";

    /*************************************************************************************************************************************************************
     *                                         Recuperamos el archivo requerido de la ubicacion especificada y lo devolvemos                                     
     *                                                    como un arreglo de bytes que la pagina pueda interpretar.                                              
     *************************************************************************************************************************************************************/
    public static byte[] recuperarArchivo(String archivo, String modulo, String tipoDocumento, Date fechaAlta) throws Exception {
        byte[] resultado = null;

        try {
            InputStream stream = null;
            int size = 0;

            /**
             *  Tomamos el path del archivo segun el tipo especificado
             **/
            String directorio = PATH_ARCHIVOS;

            /**
             *  Completamos el path para la lectura del archivo
             **/
            String filename = getPathForDownloadFile(modulo, tipoDocumento, directorio, fechaAlta) + archivo;
            //Log.debug("Leyendo archivo: " + filename);
            File f = new File(filename);
            size = (int) f.length();

            /**
             *  Creamos el arreglo de bytes seg�n el tama�o de la cadena de archivo
             *  y cargamos el archivo le�do dentro de un inputStream en memoria para
             *  finalmente devolver el resultado como un arreglo de bytes.
             **/
            resultado = new byte[size];
            stream = new BufferedInputStream(new FileInputStream(f));
            stream.read(resultado);

        } catch (Exception e) {
            throw new Exception("Error al recuperar archivo. Error: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Se encarga de ubicar el archivo dado en la ubicación deseada tomando en cuenta
     * parámetros específicos para su correcto orden de almacenamiento
     *
     * @return El nombre formateado del Archivo en la nueva ubicación.
     *
     * @throws Exception de Archivo
     **/
    public static String moveFile(File file, String name, String modulo, String tipoDocumento) throws Exception {
        try {
            String path = PATH_ARCHIVOS;
            String finalUrl = getPathForUploadedFile(modulo, tipoDocumento, path);

            name = fileNameFormatter(name);

            String nombre = finalUrl + System.getProperty("file.separator") + name;
            nombre = nombre.replace("//", "/");

            //file.renameTo(new File(nombre));
            
            File to = new File(nombre);
            Path fromPath = file.toPath();
            Path toPath = to.toPath();
            Files.move(fromPath, toPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return name;
        } catch (Exception e) {
            throw new Exception("Excepción de Archivo: \n" + e.getMessage());
        }
    }

    /**
     * Devuelve el path final correspondiente al archivo especificado
     **/
    private static String getFinalPath(File file, String name, String modulo, String tipoDocumento, String path) throws Exception {
        try {
            String finalUrl = getPathForUploadedFile(modulo, tipoDocumento, path);
            name = finalUrl + System.getProperty("file.separator") + fileNameFormatter(name);
            name = name.replace("//", "/");

            return name;
        } catch (Exception e) {
            throw new Exception("Excepción Path: \n" + e.getMessage());
        }
    }

    /**
     * Se encarga de armar el Path para el archivo a subir tomando en cuenta parámetros
     * de ordenamiento como son fecha, año y tipo de documento (Número de Expediente)
     *
     * @return Path
     **/
    private static String getPathForUploadedFile(String modulo, String tipoDocumento, String path) {
        /**
         * Se toma el año de realización de las operaciones
         **/
        SimpleDateFormat sdfSoloAnho = new SimpleDateFormat("yyyy");

        /**
         * Se designa un formato de fecha a utilizar para la creación de los ficheros
         * de almacenamiento
         **/
        SimpleDateFormat sdfFechaCompleta = new SimpleDateFormat("yyyy.MM.dd");

        Date fecha = new Date();
        String anho = sdfSoloAnho.format(fecha);
        String fechaCompleta = sdfFechaCompleta.format(fecha);

        /**
         * Se concatenan las partes para crear el Path a utilizar para el archivo de
         * los adjuntos
         **/
        String finalPath = path
                + System.getProperty("file.separator")
                + modulo
                + System.getProperty("file.separator")
                + anho
                + System.getProperty("file.separator")
                + fechaCompleta
                + System.getProperty("file.separator")
                + tipoDocumento
                + System.getProperty("file.separator");

        File directorio = new File(finalPath);

        /**
         * Se verifica que el directorio exista.
         * De no existir es creado.
         **/
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        /**
         * Se devuelve el Path a utilizar
         **/
        return finalPath;
    }

    /**
     * Se encarga de armar el Path para el archivo a subir tomando en cuenta parámetros
     * de ordenamiento como son fecha, año y tipo de documento (Número de Expediente)
     *
     * @return Path
     **/
    private static String getPathForDownloadFile(String modulo, String tipoDocumento, String path, Date fechaAlta) {
        /**
         * Se toma el año de realización de las operaciones
         **/
        SimpleDateFormat sdfSoloAnho = new SimpleDateFormat("yyyy");

        /**
         * Se designa un formato de fecha a utilizar para la creación de los ficheros
         * de almacenamiento
         **/
        SimpleDateFormat sdfFechaCompleta = new SimpleDateFormat("yyyy.MM.dd");

        Date fecha = fechaAlta;
        String anho = sdfSoloAnho.format(fecha);
        String fechaCompleta = sdfFechaCompleta.format(fecha);

        /**
         * Se concatenan las partes para crear el Path a utilizar para el archivo de
         * los adjuntos
         **/
        String finalPath = path
                + System.getProperty("file.separator")
                + modulo
                + System.getProperty("file.separator")
                + anho
                + System.getProperty("file.separator")
                + fechaCompleta
                + System.getProperty("file.separator")
                + tipoDocumento
                + System.getProperty("file.separator");

        File directorio = new File(finalPath);

        /**
         * Se verifica que el directorio exista.
         * De no existir es creado.
         **/
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        /**
         * Se devuelve el Path a utilizar
         **/
        return finalPath;
    }

    /**
     * Se encarga de dar formato al nombre de archivo eliminando los espacios
     * y caracteres especiales por "_" (Guión bajo) de tal manera que pueda
     * ser leído por cualquier tipo de sistema.
     *
     * @return Nombre de archivo formateado
     **/
    private static String fileNameFormatter(String nombreArchivo) {

        /**
         * Se toma el nombre original del archivo quitándole el path
         **/
        String nombreOriginal = nombreArchivo.substring(nombreArchivo.lastIndexOf(System.getProperty("file.separator")) + 1);
        String nombreNuevo = "";

		/**
		 * Tomamos la extensión del archivo y la separamos (Si no hay extension,
		 * mantenemos el nombre)
		 **/
        String extension = "";
		int posIniExtension = nombreOriginal.lastIndexOf(".");
		if (posIniExtension != -1) {
			extension = nombreOriginal.substring(posIniExtension + 1);
			nombreOriginal = nombreOriginal.substring(0, posIniExtension);
		}

        /**
         * Reemplazamos todos los espacios y caracteres especiales por "-"
         * a fin de estandarizar el mapa de caracteres utililzado en el nombre
         * del archivo
         **/
        for (int i = 0; i < nombreOriginal.length(); i++) {
            if (Pattern.matches("[a-zA-Z0-9_]", Character.toString(nombreOriginal.charAt(i)))) {
                nombreNuevo += Character.toString(nombreOriginal.charAt(i)).toLowerCase();
            } else {
                nombreNuevo += "_";
            }
        }

		/**
		 * Colocamos la hora al final del nombre y le devolvemos la extension.
		 * (Si no hay extension mantenemos el nombre)
		 **/
		String ret = nombreNuevo + "_" + System.currentTimeMillis();
		if (posIniExtension != -1) {
			ret = ret + "." + extension;
		}
		return ret;
    }
}