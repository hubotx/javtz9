package retire;

/*
GBC - A convenience class to tame the GridBagLayout

Copyright (C) 2002 Cay S. Horstmann (http://horstmann.com)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

import java.awt.*;

/**
   Zadaniem tej klasy jest u³atwienie korzystania z klasy GridBagConstraints.
*/
public class GBC extends GridBagConstraints 
{
   /**
      Tworzy obiekt GBC dla podanych parametrów gridx i gridy.
      Pozosta³e ograniczenia otrzymuj¹ wartoœci domyœlne.
      @param gridx pozycja gridx
      @param gridy pozycja gridy
   */
   public GBC(int gridx, int gridy)
   {
      this.gridx = gridx;
      this.gridy = gridy;
   }

   /**
      Tworzy obiekt GBC dla podanych parametrów gridx, gridy, gridwidth, gridheight.
      Pozosta³e ograniczenia otrzymuj¹ wartoœci domyœlne.
      @param gridx pozycja gridx
      @param gridy pozycja gridy
      @param gridwidth komórki w kierunku x
      @param gridheight komórki w kierunku y
   */
   public GBC(int gridx, int gridy, int gridwidth, int gridheight)
   {
      this.gridx = gridx;
      this.gridy = gridy;
      this.gridwidth = gridwidth; 
      this.gridheight = gridheight; 
   }

   /**
      Konfiguruje parametr anchor.
      @param anchor wartoœæ parametru anchor
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setAnchor(int anchor) 
   { 
      this.anchor = anchor; 
      return this;
   }
   
   /**
      Konfiguruje parametr fill.
      @param fill wartoœæ parametru fill
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setFill(int fill) 
   { 
      this.fill = fill; 
      return this;
   }

   /**
      Konfiguruje parametry weightx i weighty.
      @param weightx wartoœæ parametru weightx
      @param weighty wartoœæ parametru weighty
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setWeight(double weightx, double weighty) 
   { 
      this.weightx = weightx; 
      this.weighty = weighty; 
      return this;
   }

   /**
      Konfiguruje odstêpy pomiêdzy komórkami.
      @param distance odstêp we wszystkich kierunkach
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setInsets(int distance) 
   { 
      this.insets = new Insets(distance, distance, distance, distance);
      return this;
   }

   /**
      Konfiguruje odstêpy pomiêdzy komórkami.
      @param top odstêp w górê
      @param left odstêp w lewo
      @param bottom odstêp w dó³
      @param right odstêp w prawo
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setInsets(int top, int left, int bottom, int right) 
   { 
      this.insets = new Insets(top, left, bottom, right);
      return this;
   }

   /**
      Konfiguruje parametry ipadx i ipady.
      @param ipadx wartoœæ parametru ipadx
      @param ipady wartoœæ parametru ipady
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setIpad(int ipadx, int ipady) 
   { 
      this.ipadx = ipadx; 
      this.ipady = ipady; 
      return this;
   }
}
