#include "course.h"
#include "section.h"
#include "split.h"

#include <algorithm>
#include <cassert>
#include <cstdlib>

using namespace std;


Section::Section (std::string title, std::string callNumber,
		  std::string instructor)
  : theTitle(title),
    theCallNumber(callNumber),
    theInstructor(instructor),
    numStudent(0),
    firstStudent(0),
    lastStudent(0)
{
}
Section::Section()
{
    theTitle = "";
    theCallNumber = "";
    theInstructor = "";
    numStudent = 0;
    firstStudent = 0;
    lastStudent = 0;
}


Section::StudentNode::StudentNode (const Student& stu)
  : data(stu), next(0)
{
}


/**** insert your operations for Sections here  ***/

void Section::addStudent(const Student& stu)
{
    stu.getSurname();
    stu.getGivenName();

}

int Section::numberOfStudents() const
{
    return numStudent;
}


bool Section::operator==(const Section& sections) const
{
    return (
            (sections.theTitle==theTitle)&&
            (sections.theCallNumber==theCallNumber)&&
            (sections.theInstructor==theInstructor)&&
            (sections.numStudent==numStudent)&&
            (sections.firstStudent==firstStudent)&&
            (sections.lastStudent==lastStudent)
            );
}

bool Section::operator<(const Section& sections) const
{
    return (
            (sections.theTitle<theTitle)&&
            (sections.theCallNumber<theCallNumber)&&
            (sections.theInstructor<theInstructor)&&
            (sections.numStudent<numStudent)&&
            (sections.firstStudent<firstStudent)&&
            (sections.lastStudent<lastStudent)
            );
}

