package SpreadSheetJ.Model;

//Time values in the spreadsheet.
public class TimeValue extends Value {

    private static final String theValueKindName = new String("TimeValue");
    private int days;
    private int hours;
    private int minutes;
    private int seconds;

public int gethours(){return hours;}
public int getminutes(){return minutes;}
public int getdays(){return days;}
public int getseconds(){return seconds;}

    private void normalize()
{

	if(Math.abs(seconds) >=60)
	{minutes+=seconds/60;
	  int a = seconds%60;
	  seconds = a;
	}
	
	if (Math.abs(minutes) >=60)
	{hours +=minutes/60;
	  int b = minutes%60;
	 minutes = b;
	 }
	
	if(Math.abs(hours) >=24)
	{days+=hours/24;
	  int c = hours%24;
	  hours =c;
	}
}
     public Object clone()
    {
	return new TimeValue(days,hours,minutes,seconds);
    }

    public TimeValue(String t) 
{
	String[] tokens = t.split("\\:");

        if (tokens.length == 4) {
            days = Integer.parseInt(tokens[0]);
            hours = Integer.parseInt(tokens[1]);
            minutes = Integer.parseInt(tokens[2]);
            seconds = Integer.parseInt(tokens[3]);
	  
        }
        if (tokens.length == 3) {
            hours = Integer.parseInt(tokens[0]);
            minutes = Integer.parseInt(tokens[1]);
            seconds = Integer.parseInt(tokens[2]);
        }
        if (tokens.length == 2) {
            minutes = Integer.parseInt(tokens[0]);
            seconds = Integer.parseInt(tokens[1]);
        } if (tokens.length == 0) {
            seconds = 0;
        }
	
    
	normalize();
}


    public static String valueKindName() {
        return theValueKindName;
    }

    public TimeValue(int d, int h, int m, int s) {
        days = d;
        hours = h;
        minutes = m;
        seconds = s;
	normalize();
    }

   

    @Override
    public String valueKind() {
        return theValueKindName;
    }

@Override 
    public String render (int maxWidth)
    {
	 String result = "";
        	
        
	if(days !=0)
	{result += days + ":";
	   if (hours > 9)
		{
		result+= hours +":";
		}
	else
	    {
		result += "0" + hours + ":";
	     }
	if(minutes > 9)
	 	{ 
		result += minutes + ":";
		}
	else
		{
		result+= "0" + minutes + ":";
		}
	if (seconds > 9)
		{
		result += seconds;
		}
	else
	      {
		result+= "0" + seconds;
		}
	}
else if (hours != 0)
	{
	 result += hours + ":";
	
	if (minutes > 9)
	{
	 result += minutes + ":";
	}
	else 
	{
	  result += "0" + minutes + ":";
	}
	if (seconds > 9)
	{
	  result += "" +seconds;
	}
	else
	{
	result += "0" + seconds;
	}
}
else if (minutes !=0)
	{
	result += "" + minutes+ ":";
	
   if (seconds >9)
	{
	result +="" + seconds;
	}
else
	{
	result +="0" + seconds;
	}
  }
else 
{
	if (Integer.toString(seconds).length() < 2)
	 result = "" + new ErrorValue();
		
else
	{
	if (seconds<0)
	{
	result = "*";
	}
	if (seconds == 0)
	{
	 return result = "0:00";
	}
	if (Integer.toString(seconds).length() < 2)
	{
	 result = "" + new ErrorValue();
	}
	if (seconds > 9)
	{	
	result = "0:" + seconds;
	}
	else 
	{
	result ="0:0" + seconds;
	}
}
}

   if ((maxWidth == 0) || (result.length() > maxWidth))
      {
        return result;  
      }
      else if (maxWidth == 1)
	{
	result = "*";
	return result;
	 }
    else
      {
          String clipped = result.substring(0, maxWidth);
          if (!(result.endsWith(":")))
          {
            return clipped;  
          }
	else
          {
            return new String(new char[maxWidth]).replace('\0', '*');  
          }
	} 	
 }

        

   @Override
    public boolean isEqual(Value v) {
        TimeValue tv = (TimeValue) v;
        return ((days ==tv.days) &&( hours==tv.hours) && (minutes==tv.minutes) && (seconds==tv.seconds));

    }
}

