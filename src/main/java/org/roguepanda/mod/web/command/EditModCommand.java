package org.roguepanda.mod.web.command;

import java.net.URL;

import org.roguepanda.mod.domain.Mod;

public class EditModCommand
{
	private String name;
	private String description;
	private String installScript;
	private URL home;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstallScript() {
		return installScript;
	}

	public void setInstallScript(String installScript) {
		this.installScript = installScript;
	}

	public URL getHome() {
		return home;
	}

	public void setHome(URL home) {
		this.home = home;
	}

	public void applyTo(Mod mod)
	{
		mod.setDescription(description);
		mod.setHome(home);
		mod.setInstallScript(installScript);
		mod.setName(name);
	}
}
