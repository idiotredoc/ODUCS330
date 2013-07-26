#ifndef CIRCLE_H
#define CIRCLE_H


#include "shape.h"
#include "point.h"


class Circle : public Shape
{
public:
  Circle(Color edgeColor = Color::black,
	    Color fillColor = Color::transparent);
  Circle(Point center,double rad,
	    Color edgeColor = Color::black,
	    Color fillColor = Color::transparent);


  virtual void scale(const Point& center, double s);
  virtual void translate(double x, double y);
  virtual void draw(Graphics& g) const;
  virtual void fill(Graphics& g) const;
  virtual void print(std::ostream& os) const;
  virtual RectangularArea boundingBox() const;
  virtual void get(std::istream& in);

  //Point bottomright() const;

  virtual Shape* clone() const {return new Circle(*this);}




private:
  Point cent;
  double radius;
  Point _bottomright;
  Point _topleft;
};

#endif
