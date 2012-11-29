World.load("Testing Map", "tiles/basic.json");

// A simple jQuery-like wrapper
var $ = function (x) { 
    if (x[0] == '#') {
        return World.asTile(World.$(x));
    } else if (x[0] == '%') {
        return World.asEntity(World.$(x));
    }

    return null;
};

// Handle logic for the first trigger
(function () {
    var $trigger1 = $('#trigger_diglett');

    $trigger1.touch(function (e) {
        if (!e.is(':controlledPlayer'))
            return;

        $trigger1.remove();

        var saved = World.save();

        var team_a = World.createTeam("Diglett", [$('%diglett')]);
        var team_b = World.createTeam("You", [e]);
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

// Enemy handling
(function () {
    // var $diglett = World.spawn("diglett", 180, 375);
    // World.fightable(180, 375, 64, 64, [$diglett], function (e) { $diglett.remove(); });
})();
