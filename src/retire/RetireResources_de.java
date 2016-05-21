package retire;

import java.awt.*;

/**
 * Zasoby dla jêzyka niemieckiego, 
 * które nie s¹ ³añcuchami znaków.
 * @version 1.21 2001-08-27
 * @author Cay Horstmann
 */
public class RetireResources_de extends java.util.ListResourceBundle
{
   private static final Object[][] contents = {
   // BEGIN LOCALIZE
         { "colorPre", Color.yellow }, { "colorGain", Color.black }, { "colorLoss", Color.red }
   // END LOCALIZE
   };

   public Object[][] getContents()
   {
      return contents;
   }
}
