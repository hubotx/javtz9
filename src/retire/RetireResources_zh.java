package retire;

import java.awt.*;

/**
 * Zasoby dla jêzyka chiñskiego, 
 * które nie s¹ ³añcuchami znaków.
 * @version 1.21 2001-08-27
 * @author Cay Horstmann
 */
public class RetireResources_zh extends java.util.ListResourceBundle
{
   private static final Object[][] contents = {
   // BEGIN LOCALIZE
         { "colorPre", Color.red }, { "colorGain", Color.blue }, { "colorLoss", Color.yellow }
   // END LOCALIZE
   };

   public Object[][] getContents()
   {
      return contents;
   }
}
