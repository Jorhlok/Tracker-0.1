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
        Elements = new ArrayList<>();
        int i = 0, j, k, l;
        String s;
        ArrayList<String> tmp;
        while (i < str.length()) {
            j = str.indexOf('\n', i);
            if (j < 0 || j > str.length()) j = str.length();
            s = str.substring(i, j).trim();
            if (s.length() > 0 && s.charAt(0) == '#') {
                Elements.add(new ArrayList<String>());
                tmp = Elements.get(Elements.size()-1);
                tmp.add("#");
                tmp.add(s.substring(1));
            }
            else if ( s.length() > 0 && (k = s.indexOf('{')) >= 0 ) {
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
                    if (s.length() > l+1) { //stuff after } is processed as its own line
                        j = str.indexOf('}', i) + 1;
                    }
                }
                else { //iterate through following lines to find crap that matters
                    boolean closed = false;
                    while(!closed) {
                        i = j + 1;
                        j = str.indexOf('\n', i);
                        s = str.substring(i, j).trim();
                        if ( (k = s.indexOf('}')) >= 0 ) {
                            closed = true;
                            if (k > 0) tmp.add(s.substring(0, k)); //line before }
                            else tmp.add("");
                            if (s.length() > k+1) { //stuff after } is processed as its own seperate line
                                j = str.indexOf('}', i)+1;
                                closed = true;
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
            i = j + 1;
        }
    }
    
    static public String[] ParseVar(String str, String operands) {
        if (str == null || operands == null) return null;
        String[] ret = new String[3];
        int j = -1;
        for(int i=0; i<operands.length(); ++i) {
            j = str.indexOf(operands.charAt(i));
            if ( j >= 0 && j < (str.length()-1) ) break;
        }
        if (j < 0) return null;
        ret[0] = str.substring(0, j).trim();
        ret[1] = str.substring(j+1, j+1);
        ret[2] = str.substring(j+2, str.length()).trim();
        return ret;
    }
    
    static public ArrayList<String> ParseArray(String str, char delimiter) {
        if (str == null || str.equals("")) return null;
        ArrayList<String> ret = new ArrayList<>();
        int i=0, j=0;
        while (j >= 0 && j < str.length()) {
            j = str.indexOf(delimiter, i);
            if (j < 0) ret.add(str.substring(i));
            else ret.add(str.substring(i, j));
            i = j+1;
        }
        return ret;
    }
}
