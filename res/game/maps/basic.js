World.load("Testing Map", "tiles/basic.json");

// A simple jQuery-like wrapper
var $ = function (x) { 
    return World.$(x);
};

// Handle logic for the first trigger
(function () {
    var $trigger1 = $('#trigger1');
    if ($trigger1 === null) {
        return;
    }

    $trigger1.touch(function (e) {
        /*
        if (e.is(':player')) {
        } 
        */
    });
})();

// Enemy handling
(function () {
    // var $diglett = World.spawn("diglett", 180, 375);
    // World.fightable(180, 375, 64, 64, [$diglett], function (e) { $diglett.remove(); });
})();
