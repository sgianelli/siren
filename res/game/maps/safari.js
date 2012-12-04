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

(function () {
    var $trigger1 = $('#trigger_diglett');

    $trigger1.touch(function (e) {
        if (!e.is(':controlledPlayer'))
            return;

        World.music("pokemon-battle");
        $trigger1.remove();

        var saved = World.save();

        var team_a = World.createTeam("You", [e]);
        var team_b = World.createTeam("Diglett", [$('%diglett')]);
        var battle_world = World.fightable("tiles/intro-battle.json", 
            team_a, team_b, 

            // If a member from team A dies
            function (e, members) { },

            // team A wins
            function (members) { },

            // If a member from team B dies
            function (e, members) { },

            // team B wins
            function (members) {
                delete battle_world;
                saved.resume();
                $('%diglett').remove();
            }
        );
    });
})();
