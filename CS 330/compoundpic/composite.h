#ifndef COMPOSITE_H
#define COMPOSITE_H

#include "shape.h"
#include "point.h"
#include <vector>
using namespace std;
class Composite : public Shape
{
public:
	Composite(Color edgeColor = Color::black,
		    Color fillColor = Color::transparent)
			: Shape(edgeColor, fillColor){};

/*
	Composite(const Composite& c) {
        _shapes = c._shapes;
            // c._shapes = _shapes;
	}*/
	Composite( const Composite& c)
	{
	   for(int x = 0; x < c._shapes.size(); x++)
        {
            _shapes.push_back(c._shapes[x]);
        }
	}
    // Assignment Operator
    Composite& operator = ( Composite& c) {
        for(int i=0; i< _shapes.size(); i++)
            {
            _shapes[i] = c._shapes[i];
           // c._shapes[i] = _shapes[i];
            }
		return *this;
    }
        //  ~Composite() { _shapes.clear(); }
	virtual void scale(const Point& center, double s) ;
	virtual void translate(double x, double y) ;
	virtual void draw(Graphics& g) const;
	virtual void fill(Graphics& g) const;
	virtual void print(std::ostream& os) const;
    virtual void plot(Graphics& g) const;
	int numberOfShapes () const {return _shapes.size();} // Implicit conversion loses integer precision: Unsigned long to int
	virtual Shape* clone() const { return new Composite(*this); }
    virtual RectangularArea boundingBox() const;
	virtual void get(std::istream& in);

private:
	std::vector<Shape*> _shapes;
};

#endif
