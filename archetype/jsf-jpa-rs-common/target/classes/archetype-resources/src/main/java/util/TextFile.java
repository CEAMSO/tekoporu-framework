package ${package}.util;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A value object containing a cached file and file attributes.
 */
public final class TextFile implements Externalizable {

   /**
    * A time when the file was modified last time.
    */
   private long lastModified;

   /**
    * A content of the text file.
    */
   private String content;


   public TextFile() {

   }


   /**
    * Returns file's last modification time stamp.
    *
    * @return file's last modification time stamp.
    */
   public long getLastModified() {

      return lastModified;
   }


   /**
    * Returns file's content.
    *
    * @return file's content.
    */
   public String getContent() {

      return content;
   }


   /**
    * Sets the content of the file.
    *
    * @param content file's content.
    */
   public void setContent(final String content) {

      this.content = content;
   }


   /**
    * Sets file's last modification time stamp.
    *
    * @param lastModified file's last modification time stamp.
    */
   public void setLastModified(final long lastModified) {

      this.lastModified = lastModified;
   }

   public String toString() {

      return "TextFile{" +
              "lastModified=" + lastModified +
              ", content='" + content + '\'' +
              '}';
   }

   /**
    * Saves this object's content by calling the methods of DataOutput.
    *
    * @param out the stream to write the object to
    * @throws IOException Includes any I/O exceptions that may occur
 */
   /**
    * Devuelve el contenido del file en el parametro out
    */
	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
	
	    out.writeLong(lastModified);
	    out.writeUTF(content);
		
	}
	
   /**
    * Restore this object contents by calling the methods of DataInput.  The readExternal method must 
    * read the values in the same sequence and with the same types as were written 
    * by {@link #writeExternal(ObjectOutput)} .
    *
    * @param in the stream to read data from in order to restore the object
    * @throws IOException if I/O errors occur
    *
 */
	/**
	 * Setea el contenido del file
	 */
	@Override
	public void readExternal(final ObjectInput in) throws IOException,
			ClassNotFoundException {
		lastModified = in.readLong();
	    content = in.readUTF();
		
	}
}
