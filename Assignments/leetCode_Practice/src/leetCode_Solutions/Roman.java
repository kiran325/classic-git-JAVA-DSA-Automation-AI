package leetCode_Solutions;

import java.util.HashMap;
import java.util.Map;

public class Roman {
	
	public int romanToInt(String s) {
        // Roman symbol mapping
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int result = 0;

        for (int i = 0; i < s.length(); i++) {
            int value = map.get(s.charAt(i));
            
            // Look ahead for subtractive case
            if (i + 1 < s.length() && value < map.get(s.charAt(i + 1))) {
                result -= value; // subtract if smaller than next
            } else {
                result += value; // else add normally
            }
        }

        return result;
    }

}
