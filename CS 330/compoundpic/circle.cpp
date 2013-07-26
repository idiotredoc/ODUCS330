#include "circle.h"
#include "shape.h"
#include <algorithm>

using namespace std;



/*-------------------------------------------------------------*/

Circle::Circle(Color edgeColor, Color fillColor)
  : Shape(edgeColor, fillColor)
{
}

/*.............................................................*/


Circle::Circle(Point cntr, double rad,
		     Color edgeColor, Color fillColor)
  : Shape(edgeColor, fillColor)
{
//radius = rad;
//center = cntr;
}

/*.............................................................*/

void Circle::draw(Graphics& g) const
{
    cout<< "circle"<< endl;
  if (getEdgeColor() != Color::transparent)
    {
      g.setColor(getEdgeColor());
      g.fillOval(round(cent.x()- radius),round(cent.y()-radius),
                 round(radius*2), round (radius*2));
    }
}

/*.............................................................*/

RectangularArea Circle::boundingBox()const
{
    return RectangularArea(cent.x()- radius, cent.y() + radius);
}

/*.............................................................*/

void Circle::fill(Graphics& g) const
{
  if (getFillColor() != Color::transparent)
   {
     g.setColor(getFillColor());
     g.fillOval(round(cent.x() - radius),
		round(cent.y() - radius),
		round(radius*2),round(radius*2));

   }
}

/*.............................................................*/

void Circle::print(ostream& os) const
{
  os << "Circle: " << cent << " " <<radius;
}

/*.............................................................*/

void Circle::scale(const Point& center, double s)
{
    cent = cent.scale(center, s);

   radius = radius*s;
}

/*.............................................................*/

void Circle::translate(double x, double y)
{
  cent = cent.translate(x, y);

}


/*.............................................................*/

void Circle::get(std::istream& in)
{
  in >> _edge >> _fill;   // read edge and fill colors
  in >> cent >> radius;

}
