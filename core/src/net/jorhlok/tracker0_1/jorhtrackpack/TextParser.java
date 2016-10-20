package net.jorhlok.tracker0_1.jorhtrackpack;

import java.util.ArrayList;

/**
 * Simply run the text files through this and it should make it easier to parse.
 * Single line "#:" or "#="
 * Multi line "{}" each line is a string with leading and trailing whitespace removed.
 * @author joshm
 */
public class TextParser {
    public String str;
    public ArrayList<ArrayList<String>> Elements;
        //Elements[0][0] = "#:";     //type and seperator
        //Elements[0][1] = "Name";  //name
        //Elements[0][2] = "\"An example name\""; //value
        //Elements[1][0] = "{}";
        //Elements[1][1] = "Pattern 00";
        //Elements[1][2] = "::::::,,,.. //";
        //Elements[1][3] = ":C:1::3ff00 //";
        //Elements[2][0] = ""; //might not be insignificant
        //Elements[3][0] = "about the concept of ..."; //kept but probably useless
    
    public TextParser() {
    }
    
    public TextParser(String s) {
        str = s;
        parse();
    }
    
    public void Parse(String s) {
        str = s;
        parse();
    }
    
    private void parse() {
        Elements = new ArrayList<ArrayList<String>>();
        int i = 0, j, k, l;
        String s;
        ArrayList<String> tmp;
        while (i < str.length()) {
            j = str.indexOf('\n', i);
            s = str.substring(i, j).trim();
            if (s.charAt(0) == '#') {
                Elements.add(new ArrayList<String>());
                tmp = Elements.get(Elements.size()-1);
                k = s.indexOf('=');
                l = s.indexOf(':');
                if (k < 0 && l < 0) { //no seperator
                    tmp.add("#");
                    tmp.add(s.substring(1).trim());
                }
                else if (k > 0 && l > 0) { //both are present, find the first one
                    tmp.add( "#" + s.charAt(k = Math.min(k, l)) );
                }
                else { //one of them is present
                    tmp.add( "#" + s.charAt(k = Math.max(k, l)) );
                }
                if (k > 0) { //a seperator is present
                    tmp.add(s.substring(1, k).trim()); //name
                    if (s.length() > k+1) tmp.add(s.substring(k+1).trim()); //value
                    else tmp.add(""); //no value present?
                }
            }
            else if ( (k = s.indexOf('{')) >= 0 ) {
                Elements.add(new ArrayList<String>());
                tmp = Elements.get(Elements.size()-1);
                tmp.add("{}");
                tmp.add(s.substring(0, k).trim()); //name
                l = s.indexOf('}');
                if (s.length() > k+1 && l < 0) tmp.add(s.substring(k+1).trim()); //if the space after the { isn't empty it gets to be a line
                else if (s.length() > k+1) {
                    //there IS a } on this line and maybe stuff in between and/or after
                    if (l > k+1) tmp.add(s.substring(k+1,l).trim()); //stuff in between
                    else tmp.add(""); //nothing in between
                    if (s.length() > l+1) { //stuff after } is its own worthless text element
                        Elements.add(new ArrayList<String>());
                        tmp = Elements.get(Elements.size()-1);
                        tmp.add(s.substring(l+1).trim());
                    }
                }
                else { //iterate through following lines to find crap that matters
                    boolean closed = false;
                    while(!closed) {
                        i = j;
                        j = str.indexOf('\n', i);
                        s = str.substring(i, j).trim();
                        if ( (k = s.indexOf('}')) >= 0 ) {
                            closed = true;
                            if (k > 0) tmp.add(s.substring(0, k)); //line before }
                            else tmp.add("");
                            if (s.length() > k+1) { //stuff after } is its own worthless text element
                                Elements.add(new ArrayList<String>());
                                tmp = Elements.get(Elements.size()-1);
                                tmp.add(s.substring(k+1).trim());
                            }
                        }
                        else tmp.add(s);
                    }
                }
            }
            else {
                Elements.add(new ArrayList<String>());
                tmp = Elements.get(Elements.size()-1);
                tmp.add(s);
            }
            i = j;
        }
    }
}
