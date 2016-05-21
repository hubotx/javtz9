package retire;

import java.awt.*;

/**
 * Zasoby dla języka niemieckiego,
 * które nie są łańcuchami znaków.
 * @author Cay Horstmann
 */
public class RetireResources extends java.util.ListResourceBundle
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
