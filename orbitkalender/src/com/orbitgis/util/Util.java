package com.orbitgis.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.orbitgis.entities.Gebruiker;

public class Util {
	static List<Gebruiker> shuffleList(List<Gebruiker> list){
		List<Gebruiker> listShuffled = new ArrayList<>();
		SecureRandom random = new SecureRandom();
		 for(int i=0; i<list.size(); i++) {
			 int randomInt = random.nextInt(list.size()-i);
			 listShuffled.add(list.get(randomInt));
			 list.remove(randomInt);
		 }
		 return listShuffled;
	 }
}
