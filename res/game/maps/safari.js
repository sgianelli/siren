World.load("Testing Map", "tiles/safari.json");

// World.music("dark-world");

// A simple jQuery-like wrapper
var $ = function(x) {
	if (x[0] == '#') {
		return World.asTile(World.$(x));
	} else if (x[0] == '%') {
		return World.asEntity(World.$(x));
	}
	return null;
};

$("#exit").touch(function (e) {
    World.exit();
});
