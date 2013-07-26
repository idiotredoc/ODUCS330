package SpreadSheetJ.Model;



public class Sequence {

  // Constructors & assignment
  Sequence()
  {
    theSize = 0;
    theFront = null;
    theBack = null;
  }


  // General info
  int size()
  {
    return theSize;
  }


  boolean equals (Sequence seq)
  {
    if (theSize != seq.theSize)
      return false;

    SequenceNode p1 = theFront;
    SequenceNode p2 = seq.theFront;
    while (p1 != null) {
      if (!p1.data.equals(p2.data))
	return false;
      p1 = p1.next;
      p2 = p2.next;
    }
    return true;
  }



    private class SequenceEnumeration implements SearchEnumeration
    {
	private SequenceNode position;

	SequenceEnumeration (SequenceNode pos)
	{
	    position = pos;
	}

	// The standard Enumeration operations
	public boolean hasMoreElements()
	{
	    return position != null;
	}
	
	public Object nextElement()
	{
	    SequenceNode thisOne = position;
	    position = position.next;
	    return thisOne.data;
	}

	// Extensions : look at the current object without moving
	// (useful when returning a position where something was found
	//  after a search operation).
	public boolean hasElement()
	{
	    return position != null;
	}

	public Object at()
	{
	    return position.data;
	}

	// Move backwards as well as forwards
	public boolean hasPriorElements()
	{
	    return position != null &&
		position.prev != null;
	}
	
	public Object priorElement()
	{
	    SequenceNode thisOne = position;
	    position = position.prev;
	    return thisOne.data;
	}


	public Object clone ()
	{
	    SequenceEnumeration theClone = new SequenceEnumeration(position);
	    return theClone;
	}

	// and for local use only...
	public SequenceNode node() {return position;}
    }
    

    SearchEnumeration front()
    {
	return new SequenceEnumeration(theFront);
    }



    SearchEnumeration back()
    {
	return new SequenceEnumeration(theBack);
    }


    // Searching
    SearchEnumeration find(Object key)
    {
	SequenceNode p = theFront;
	while (p != null && !p.data.equals(key))
	    p = p.next;
	return new SequenceEnumeration(p);
    }


    // Adding and removing elements
    void addToFront (Object value)
    {
	if (theFront == null) {
	    theFront = theBack = new SequenceNode (value, null, null);
	}
	else {
	    SequenceNode oldFirst = theFront;
	    theFront = oldFirst.prev = new SequenceNode (value, null, oldFirst);
	}
	++theSize;
    }

    void addToBack (Object value)
    {
	if (theBack == null) {
	    theFront = theBack = new SequenceNode (value, null, null);
	}
	else {
	    SequenceNode oldLast = theBack;
	    theBack = oldLast.next = new SequenceNode (value, oldLast, null);
	}
	++theSize;
    }


    void addAfter (SearchEnumeration position,
		   Object value)
    {
	SequenceNode pos = ((SequenceEnumeration)position).node();
	SequenceNode newNode = new SequenceNode(value, pos, pos.next);
	if (pos.next != null)
	    pos.next.prev = newNode;
	pos.next = newNode;
	if (theBack == pos)
	    theBack = newNode;
	++theSize;
    }

    void addBefore (SearchEnumeration position,
		    Object value)
    {
	SequenceNode pos = ((SequenceEnumeration)position).node();
	SequenceNode newNode = new SequenceNode(value, pos.prev, pos);
	if (pos.prev != null)
	    pos.prev.next = newNode;
	pos.prev = newNode;
	if (theFront == pos)
	    theFront = newNode;
	++theSize;
    }
  

    void removeFront()
    {
	theFront = theFront.next;
	if (theFront != null)
	    theFront.prev = null;
	else
	    theBack = null;
	--theSize;
    }


    void removeBack()
    {
	theBack = theBack.prev;
	if (theBack != null)
	    theBack.next = null;
	else
	    theFront = null;
	--theSize;
    }

    void remove (SearchEnumeration position)
    {
	SequenceNode pos = ((SequenceEnumeration)position).node();
	if (pos == theFront)
	    removeFront();
	else if (pos == theBack)
	    removeBack();
	else
	    {
		pos.next.prev = pos.prev;
		pos.prev.next = pos.next;
		--theSize;
	    }
    }

    void clear()
    {
	theFront = theBack = null;
	theSize = 0;
    }



    private class SequenceNode {
	public Object data;
	public SequenceNode prev;
	public SequenceNode next;

	SequenceNode (Object value,
		      SequenceNode prevNode,
		      SequenceNode nextNode)
	{
	    data = value;
	    prev = prevNode;
	    next = nextNode;
	}
    }

    private int theSize;
    private SequenceNode theFront;
    private SequenceNode theBack;
}
