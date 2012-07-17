package org.roguepanda.mod.util;

import org.compass.core.Compass;
import org.compass.gps.device.jpa.AbstractCompassJpaEntityListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class CompassEntityListener extends AbstractCompassJpaEntityListener
{
	@Autowired
	private Compass compass;
	
	@Override
	protected Compass getCompass()
	{
		return compass;
	}

}
