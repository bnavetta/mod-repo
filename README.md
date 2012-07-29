Mod Repository
========

An online repository to host and Minecraft mods in an installable format.
Eventually, there will be a client program to install these mods.

[Site on Cloud Foundry](modrepository.cloudfoundry.com)

REST Api
--------

*    `/api/search.json?q={query}` - Search for mods matching `query`

*    `/api/details/{id}.json` - Get extra details for the mod with id `id`

*    `/api/recent.json` - Get a list of recent mods (10 by default, the `count` parameter can be used also)

Search and Recent calls return JSON in the following format:

	[
		{
			name: {name of first mod},
			id: {first mod's id}
		},
		
		{
			name: {name of second mod},
			id: {second mod's id}
		}
		...
	]

Details calls return JSON in the following format:

	{
		name: {name of mod},
		id: {mod's id},
		description: {description of mod},
		created: {date the mod was created as epoch timestamp (milliseconds since January 1st, 1970 UTC)},
		lastModified: {last time the mod was edited as epoch timestamp (see above)},
		home: {homepage of mod (URL)},
		authorName: {name of mod's author},
		authorId: {mod's author's id}
	}