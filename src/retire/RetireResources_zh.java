package retire;

import java.awt.*;

/**
 * Zasoby dla j�zyka chi�skiego, 
 * kt�re nie s� �a�cuchami znak�w.
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
