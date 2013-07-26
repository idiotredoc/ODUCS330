#include "font.h"

using namespace std;

std::string Font::toString() const
{
  static const char* FamilyNames[] = {"Helvectica", "Courier", "Times-Roman"};
  static const char* StyleNames[] = {"", "-Bold", "-Oblique"};
  return string(FamilyNames[_family]) + string(StyleNames[_style]);
}
