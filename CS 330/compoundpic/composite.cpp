#include "composite.h"
#include "circle.h"
#include "line.h"
#include "polygon.h"
#include "rectangle.h"
#include "shape.h"
#include <cstdio>

using namespace std;


void Composite::draw(Graphics& g) const {
	if(getEdgeColor() != Color::transparent) {
		for (int i = 0; i < _shapes.size(); i++)
			_shapes[i]->draw(g);
	}
}

void Composite::fill(Graphics& g) const {
	if(getFillColor() != Color::transparent) {
		for (int i = 0; i < _shapes.size(); i) {  /*  UNUSED ENTITY ISSUE - Expression Result Unused  */
			_shapes[i]->fill(g);
		}
	}
}


void Composite::plot(Graphics& g) const {
	for(int x= 0; x < _shapes.size(); x++){
		//_shapes[x]->plot(g);
		_shapes[x]->fill(g);
		_shapes[x]->draw(g);
	}
}
/*
void Composite::plot(Graphics& g) const {
        // cout << "Plot Called" << endl;
	for(int x = _shapes.size()-1; x>=0; x--) {
       // cout << "x: " << x << " Shapes: " << _shapes.size() << endl;
		_shapes[x]->plot(g);
	}
}*/

void Composite::print(ostream& os) const {
	os << "Composite: " << _shapes.size() << " {";
	for (int i = 0; i < _shapes.size(); i++) {
		_shapes[i]->print(os);
		if(i != (_shapes.size() - 1))
			os << " ";
	}
	os << "}";
}

void Composite::scale(const Point& center, double s)  {
	for (int i = 0; i < _shapes.size(); i++)
		_shapes[i]->scale(center, s);
}

void Composite::translate(double x, double y)  {
	for (int i = 0; i < _shapes.size(); i++)
		_shapes[i]->translate(x , y);
}


    //  Control Reaches end of non-void function !!!
RectangularArea Composite::boundingBox() const {
   	for(int x = 0; x < _shapes.size(); x++) {
        return _shapes[x]->boundingBox();
    }

        // Control May Reach End Of Non-Void Function.
}



void Composite::get(std::istream& in) {
	string shapeName;
	int nShapes;

	in >> _edge >> _fill; // read edge and fill colors
	in >> nShapes;

	for (int i = 0; i < nShapes; ++i) {
		in >> shapeName;

		cout << _shapes.size() << " :i: " << i << endl;
		if(shapeName == "Line") {
			Line* l = new Line;
			_shapes.push_back(l);
			_shapes[i]->get(in);

		}

		else if(shapeName == "Rectangle") {
			Rectangle* r = new Rectangle;
			_shapes.push_back(r);
			_shapes[i]->get(in);

		}
		else if(shapeName == "Polygon") {
			Polygon* p = new Polygon;
			_shapes.push_back(p);
			_shapes[i]->get(in);

		}
		else if(shapeName == "Circle") {
			Circle* c = new Circle;
			_shapes.push_back(c);
			_shapes[i]->get(in);

		}
        cout << _shapes.size() << " :i: " << i << endl;
	}
}
