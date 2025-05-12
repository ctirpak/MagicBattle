package com.github.ctirpak.magicbattle.kits;

import com.github.ctirpak.magicbattle.kititems.WandArrow;
import com.github.ctirpak.magicbattle.kititems.WandTNT;
import com.github.ctirpak.magicbattle.kititems.WandFire;
import com.github.ctirpak.magicbattle.kititems.WandIceBolt;
import com.github.ctirpak.magicbattle.kititems.WandPoison;
import com.github.ctirpak.magicbattle.kititems.WandZoom;

public class WandKit extends Kit {
	public WandKit(String name) {
		super(name);
		addItem(new WandArrow());
		addItem(new WandTNT());
		addItem(new WandFire());
		addItem(new WandIceBolt());
		addItem(new WandPoison());
		addItem(new WandZoom());
	}
}
