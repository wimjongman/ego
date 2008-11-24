package com.weltevree.ego.knoei;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] lines = new String[9];
		
		lines[0] ="42   Taka555  <none>          Japan    18k+ 119/ 198  -   -   18s    -- default";
		lines[1] ="42      mojo  1/3 - 1/5       COM       6k+ 1119/1176  -   95   3s    -- default";
		lines[2] ="42    takasi  <none>          Japan     6k+ 1677/1852  -   -    1s    -- default";
		lines[3] ="42  IWAKURA1  ƒpƒs‹êŽè        Japan     2k+ 1549/ 803  -  128   5s    -- default";
		lines[4] ="42  guest649                  --        BC    0/   0 106  -   20s    -- default";
		lines[5] ="42     SYSAN  1-7/10          Japan     3d  559/ 513  -   -   23s    -- default";
		lines[6] ="42     kibi2  <None>hello     Japan     4d+ 233/ 176 106  -    9s    -X default";
		lines[7] ="42   whe2003  <none>          USA       3k   54/  67  -  196   5s    -- default";
		lines[8] ="42 asdfg12345  <None           Japan    13k  195/ 201  -   -   14s    -- default";


			String patCapUS = "42 *([^ ]*).."; // Captures the users
			String patCapIN = "(.{16}) *?"; // Captures Info String
			String patCapCO = "(.{7}) *"; // Capture Country
			String patCapRA = "([^ ]*) *"; // Captures Rank
			String patCapGW = "([^ ]*)/ *"; // Captures Games Won
			String patCapGL = "([^ ]*) *"; // Captures Games Lost
			String patCapOB = "([^ ]*) *"; // Captures observing
			String patCapPL = "([^ ]*) *"; // Captures playing
			String patCapID = "([^ ]*) *"; // Captures Idle
			String patCapFL = "([^ ]*) *"; // Captures Flags
			String patCapLA = "([^ ]*)"; // Captures language

			String pat = patCapUS + patCapIN + patCapCO + patCapRA + patCapGW
					+ patCapGL + patCapOB + patCapPL + patCapID + patCapFL
					+ patCapLA;

			for (int ix = 0; ix < lines.length; ix++) {
			Matcher m = Pattern.compile(pat).matcher(lines[ix]);

			m.find();

			for (int iy = 0; iy < m.groupCount(); iy++)
				System.out.print(m.group(iy + 1) + "|");
			System.out.println("");
		}

	}

}
