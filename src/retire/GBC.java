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
   Zadaniem tej klasy jest u�atwienie korzystania z klasy GridBagConstraints.
*/
public class GBC extends GridBagConstraints 
{
   /**
      Tworzy obiekt GBC dla podanych parametr�w gridx i gridy.
      Pozosta�e ograniczenia otrzymuj� warto�ci domy�lne.
      @param gridx pozycja gridx
      @param gridy pozycja gridy
   */
   public GBC(int gridx, int gridy)
   {
      this.gridx = gridx;
      this.gridy = gridy;
   }

   /**
      Tworzy obiekt GBC dla podanych parametr�w gridx, gridy, gridwidth, gridheight.
      Pozosta�e ograniczenia otrzymuj� warto�ci domy�lne.
      @param gridx pozycja gridx
      @param gridy pozycja gridy
      @param gridwidth kom�rki w kierunku x
      @param gridheight kom�rki w kierunku y
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
      @param anchor warto�� parametru anchor
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setAnchor(int anchor) 
   { 
      this.anchor = anchor; 
      return this;
   }
   
   /**
      Konfiguruje parametr fill.
      @param fill warto�� parametru fill
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setFill(int fill) 
   { 
      this.fill = fill; 
      return this;
   }

   /**
      Konfiguruje parametry weightx i weighty.
      @param weightx warto�� parametru weightx
      @param weighty warto�� parametru weighty
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setWeight(double weightx, double weighty) 
   { 
      this.weightx = weightx; 
      this.weighty = weighty; 
      return this;
   }

   /**
      Konfiguruje odst�py pomi�dzy kom�rkami.
      @param distance odst�p we wszystkich kierunkach
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setInsets(int distance) 
   { 
      this.insets = new Insets(distance, distance, distance, distance);
      return this;
   }

   /**
      Konfiguruje odst�py pomi�dzy kom�rkami.
      @param top odst�p w g�r�
      @param left odst�p w lewo
      @param bottom odst�p w d�
      @param right odst�p w prawo
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setInsets(int top, int left, int bottom, int right) 
   { 
      this.insets = new Insets(top, left, bottom, right);
      return this;
   }

   /**
      Konfiguruje parametry ipadx i ipady.
      @param ipadx warto�� parametru ipadx
      @param ipady warto�� parametru ipady
      @return obiekt GBC do dalszej modyfikacji
   */
   public GBC setIpad(int ipadx, int ipady) 
   { 
      this.ipadx = ipadx; 
      this.ipady = ipady; 
      return this;
   }
}
